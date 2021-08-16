package com.simulator.domain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.simulator.application.controller.form.ShuffleForm;

/**
 *  シャッフル処理を行うサービスクラス
 *  <br>@author asou
 *
 */
@PropertySource("classpath:application.properties")
@Service
public class ShuffleService {
    private final Environment env;

    public ShuffleService(Environment env) {
        this.env = env;
    }
    /**
     * シャッフル処理。Formから受け取ったシャッフル種別で処理を分岐する
     * <br>@param deck デッキ情報
     * <br>@param shuffleForm シャッフル種別を持ったForm
     * <br>@return シャッフル処理後のデッキ情報
     */
    public List<String> doShuffle(List<String> deck, ShuffleForm shuffleForm) {
        switch (shuffleForm.getShuffleType()) {
            case DEAL:
                deck = doDeal(deck, shuffleForm.getNumber());
                break;
            case HINDU:
                deck = doHindu(deck,shuffleForm.getNumber());
                break;
            case RIFFLE:
                deck = doRiffle(deck);
                break;
            default:
                break;
        }
        return deck;
    }

    private List<String> doDeal(List<String> cardList,int dealUnitNumber) {
        //区分けしたカードの置き場に番号を付ける。
        //番号をキー、カードの束を値としたMapを一区画として扱いHashMapを生成
        Map<Integer, List<String>> dealMapList = new HashMap<Integer,List<String>>();

        //各区画を事前に空の状態で作成しておく
        IntStream.range(0, dealUnitNumber).forEach(i -> dealMapList.put(i+1, new ArrayList<String>()));

        //カードを置いていく区画のカウント
        Integer c = 0;
        for(String card: cardList) {
            c++;

            //区分けの数を超えたら1番目の区画に戻る
            if(c > dealUnitNumber) {
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
        List<String> shuffledList= new ArrayList<String>();
        IntStream.range(0, dealUnitNumber).forEach(i -> shuffledList.addAll(dealMapList.get(i + 1)));

        return shuffledList;

    }

    private List<String> doHindu(List<String> cardList,int count) {
        //乱数生成オブジェクトを初期化
        Random rand = new Random();

    	//シャッフル時の最大の振れ幅。
    	int maxErrorCount = cardList.size()/Integer.parseInt(env.getProperty("HINDU_ERROR_COUNT"));
    	for(int i = 0; i < count; i++) {
    	    //シャッフル実行事に誤差を設定。デッキを分割する際この数値の分だけどちらかに多く抜き取られる
    	    //マイナス値を生成するために2倍し最大値を引く、かつ最大値自身も含めるため+1する
    	    int errorCount = rand.nextInt(maxErrorCount * 2 + 1) - maxErrorCount;

    	    //デッキを半分に分けた際の上側のリスト
    	    List<String> aboveList = cardList.subList(0, cardList.size()/2 + errorCount);

    	  //デッキを半分に分けた際の上側のリスト
            List<String> belowList = cardList.subList(cardList.size()/2 + errorCount, cardList.size());

            //上下を入れ替えてリストを結合
            cardList = Stream.concat(belowList.stream(), aboveList.stream())
                    .collect(Collectors.toList());
    	}
        return cardList;

    }

    private List<String> doRiffle(List<String> cardList) {

        return cardList;

    }
}
