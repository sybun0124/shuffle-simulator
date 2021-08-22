package com.simulator.domain.service;

import com.simulator.application.common.enums.Flg;
import com.simulator.application.common.enums.ShuffleType;
import com.simulator.application.controller.form.ShuffleForm;
import com.simulator.domain.model.mapper.Decklist;
import com.simulator.domain.model.mapper.Shuffle;
import com.simulator.domain.repository.mapper.DecklistMapper;
import com.simulator.domain.repository.mapper.ShuffleMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 *  シャッフル処理を行うサービスクラス。
 *  <br>@author asou
 *
 */
@PropertySource("classpath:application.properties")
@Service
public class ShuffleService {
    private final DecklistMapper decklistMapper;
    private final ShuffleMapper shuffleMapper;
    private final LoggingService loggingService;
    private final Environment env;

    /**
     * デフォルトコンストラクタ。
     * @param env 定数取得用インスタンスです
     * @param loggingService LoggingServiceインスタンス
     * @param decklistMapper DecklistMapperインスタンス
     * @param shuffleMapper ShuffleMapperインスタンス
     */
    public ShuffleService(Environment env,
                           LoggingService loggingService,
                           DecklistMapper decklistMapper,
                           ShuffleMapper shuffleMapper) {
        this.env = env;
        this.loggingService = loggingService;
        this.decklistMapper = decklistMapper;
        this.shuffleMapper = shuffleMapper;
    }

    /**
     * シャッフル処理。Formから受け取ったシャッフル種別で処理を分岐する
     * <br>@param deck デッキ情報
     * <br>@param shuffleForm シャッフル種別を持ったForm
     * <br>@return シャッフル処理後のデッキ情報
     */
    public List<String> doShuffle(List<String> deck, ShuffleForm shuffleForm, int sessionId) {
        switch (shuffleForm.getShuffleType()) {
          case DEAL:
              deck = doDeal(deck, Integer.parseInt(shuffleForm.getNumber()));
              break;
          case HINDU:
              deck = doHindu(deck);
              break;
          case RIFFLE:
              deck = doRiffle(deck, false);
              break;
          case RIFFLE_PERFECT:
              deck = doRiffle(deck, true);
              break;
          default:
              break;
        }
        loggingService.shuffleLogging(deck, shuffleForm.getShuffleType());
        insertShuffle(deck, shuffleForm, sessionId);
        return deck;
    }

    private List<String> doDeal(List<String> cardList, int dealNum) {
        //0枚ならシャッフルしないでそのまま返却
        if (dealNum <= 0) {
            return cardList;
        }
        //区分けしたカードの置き場に番号を付ける。
        //番号をキー、カードの束を値としたMapを一区画として扱いHashMapを生成
        Map<Integer, List<String>> dealMapList = new HashMap<Integer, List<String>>();

        //各区画を事前に空の状態で作成しておく
        IntStream.range(0, dealNum).forEach(
            i -> dealMapList.put(i + 1, new ArrayList<String>()));

        //カードを置いていく区画のカウント
        Integer c = 0;
        for (String card: cardList) {
            c++;

            //区分けの数を超えたら1番目の区画に戻る
            if (c > dealNum) {
                c = 1;
            }

            //既に置いてある束を取得
            List<String>  currentDeal = dealMapList.get(c);

            //カードを追加
            currentDeal.add(card);

            //マップに入れなおす
            dealMapList.put(c, currentDeal);
        }
        //返却用リストを作成し、1番の区画から順に追加していって返却する。
        List<String> shuffledList = new ArrayList<String>();
        IntStream.range(0, dealNum).forEach(
            i -> shuffledList.addAll(dealMapList.get(i + 1)));

        return shuffledList;

    }

    private List<String> doHindu(List<String> cardList) {
        //乱数生成オブジェクトを初期化
        Random rand = new Random();

        //シャッフル時の最大の誤差。
        int maxErrorNum =
                cardList.size() / Integer.parseInt(env.getProperty("HARF_CAT_ERROR_RATE"));
        //シャッフル実行事に誤差を設定。デッキを分割する際この数値の分だけどちらかに多く抜き取られる
        //マイナス値を生成するために2倍し最大値を引く、かつ最大値自身も含めるため+1する
        int errorCount = rand.nextInt(maxErrorNum * 2 + 1) - maxErrorNum;

        //デッキを半分に分けた際の上側のリスト
        List<String> aboveList = cardList.subList(0, cardList.size() / 2 + errorCount);

        //デッキを半分に分けた際の下側のリスト
        List<String> belowList =
            cardList.subList(cardList.size() / 2 + errorCount, cardList.size());

        //下側のカードを順に抜き取っていく際のインデックス
        int belowIndex = 0;

        //上側のリストのディープコピーを返却用に生成
        List<String> returnList = new ArrayList<String>(aboveList);

        //最終的なデッキ枚数に変動はないことをループの終了条件とする
        for (; returnList.size() < cardList.size();) {
            //カードを抜き取る枚数。理想1枚から乱数値の分余分に抜き取る
            int pickCard = rand.nextInt(
                    Integer.parseInt(env.getProperty("PULL_OUT_ERROR_RATE")) + 1) + 1;

            //抜き取り時の最終インデックスを算出。カットした束の枚数を超える場合に最後でストップさせる処理
            int toIndex = Math.min(belowIndex + pickCard, belowList.size());

            //カードを抜き取って上側の束のリストの先頭に挿入
            returnList.addAll(0, belowList.subList(belowIndex, toIndex));

            //抜き取った分インデックスを進める
            belowIndex = toIndex;
        }

        return returnList;

    }

    private List<String> doRiffle(List<String> cardList, boolean isPerfect) {
        //返却用の生成
        List<String> shuffledList = new LinkedList<String>();

        //乱数生成オブジェクトを初期化
        Random rand = new Random();

        //デッキカット時の誤差算出用数値を初期化
        int maxErrorNum = 0;
        if (!isPerfect) {
            //パーフェクトシャッフルじゃない場合はデッキ枚数/properties定義の定数値の誤差が生まれうる
            maxErrorNum =
                    cardList.size() / Integer.parseInt(env.getProperty("HARF_CAT_ERROR_RATE"));
        }

        //デッキカットの誤差を設定。デッキを分割する際この数値の分だけどちらかに多く抜き取られる
        //マイナス値を生成するために2倍し最大値を引く、かつ最大値自身も含めるため+1する
        int errorCount = rand.nextInt(maxErrorNum * 2 + 1) - maxErrorNum;

        //デッキを半分に分けた際の右手に持つリストとそのイテレーター
        List<String> rightList = cardList.subList(0, cardList.size() / 2 + errorCount);
        ListIterator<String> rightIterator = rightList.listIterator(rightList.size());


        //デッキを半分に分けた際の左手に持つリストとそのイテレーター
        List<String> leftList = cardList.subList(cardList.size() / 2 + errorCount, cardList.size());
        ListIterator<String> leftIterator = leftList.listIterator(leftList.size());

        //最終的なデッキサイズに変動はないことをループの終了条件とする
        for (; shuffledList.size() < cardList.size();) {

            //抜き取り切るまでは処理を続行
            if (rightIterator.hasPrevious()) {
                //カードを抜き取る枚数を初期化
                int pickCard = 1;
                if (!isPerfect) {
                    //パーフェクトシャッフルじゃない場合は乱数を使って誤差を追加
                    pickCard += rand.nextInt(
                            Integer.parseInt(env.getProperty("PULL_OUT_ERROR_RATE")) + 1);
                }
                //イテレーターで後方から抜き取り、返却用リストの前方に挿入する
                IntStream.range(0, pickCard).forEach(i -> {
                    if (rightIterator.hasPrevious()) {
                        shuffledList.add(0, rightIterator.previous());
                    }
                });
            }
            //左側の束も同様の処理
            if (leftIterator.hasPrevious()) {
                int pickCard = 1;
                if (!isPerfect) {
                    pickCard += rand.nextInt(
                            Integer.parseInt(env.getProperty("PULL_OUT_ERROR_RATE")) + 1);
                }
                IntStream.range(0, pickCard).forEach(i -> {
                    if (leftIterator.hasPrevious()) {
                        shuffledList.add(0, leftIterator.previous());
                    }
                });
            }
        }
        return shuffledList;
    }

    private void insertShuffle(List<String> cardList, ShuffleForm shuffleForm, int sessionId) {

        //共通のインサート値を定義
        int decklistId = decklistMapper.selectNextDecklistId();
        final String deleteFlg = Flg.OFF.getValue();
        final Date registDate = new Date();

        Shuffle shuffle = new Shuffle();
        shuffle.setDecklistId(decklistId);
        shuffle.setShuffleType(shuffleForm.getShuffleType().getValue());
        //ディールシャッフルのみ枚数を格納
        if (shuffleForm.getShuffleType() == ShuffleType.DEAL) {
            shuffle.setDealNum(Integer.parseInt(shuffleForm.getNumber()));
        } else {
            shuffle.setDealNum(0);
        }

        shuffle.setSessionId(sessionId);
        shuffle.setShuffleId(shuffleMapper.selectNextShuffleId());
        shuffle.setDeleteFlg(deleteFlg);
        shuffle.setRegistDate(registDate);

        shuffleMapper.insert(shuffle);

        //デッキリスト情報を登録
        int cardOrder = 0;
        for (String cardName:cardList) {
            cardOrder++;
            Decklist decklist = new Decklist();
            decklist.setDecklistId(decklistId);
            decklist.setDeleteFlg(deleteFlg);
            decklist.setRegistDate(registDate);
            decklist.setCardName(cardName);
            decklist.setCardOrder(cardOrder);
            decklistMapper.insert(decklist);
        }
    }
}
