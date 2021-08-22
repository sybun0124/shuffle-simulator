package com.simulator.domain.service;

import com.simulator.application.common.enums.Flg;
import com.simulator.domain.model.UserSession;
import com.simulator.domain.model.mapper.Decklist;
import com.simulator.domain.model.mapper.ShuffleSession;
import com.simulator.domain.repository.mapper.DecklistMapper;
import com.simulator.domain.repository.mapper.ShuffleSessionMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * セッション情報保存、出力用のサービスクラス。
 * @author asou
 *
 */
@Service
public class SessionService {
    private final LoggingService loggingService;
    private final ShuffleSessionMapper shuffleSessionMapper;
    private final DecklistMapper decklistMapper;

    /**
     * デフォルトコンストラクタ。
     * @param loggingService LoggingService
     * @param shuffleSessionMapper ShuffleSessionMapper
     * @param decklistMapper DecklistMapper
     */
    public SessionService(LoggingService loggingService,
                           ShuffleSessionMapper shuffleSessionMapper,
                           DecklistMapper decklistMapper) {
        this.loggingService = loggingService;
        this.shuffleSessionMapper = shuffleSessionMapper;
        this.decklistMapper = decklistMapper;
    }

    /**
     * デッキリスト情報をMultipartFileから取り込んでセッションに格納する。
     * @param multipartFile デッキリストのtxtファイル
     * @param oldSession 既存のユーザーセッションオブジェクト
     * @return
     * @throws IOException
     */
    public UserSession importDeckFromTxt(MultipartFile multipartFile,
                                          UserSession oldSession) throws IOException {
        //デッキ新規取り込み時はセッション情報を洗い替える
        UserSession newSession = new UserSession();
        newSession.setSessionId(shuffleSessionMapper.selectNextSessionId());
        List<String> cardList = new ArrayList<String>();

        //一行に一枚のカード名が入力されているとしてカードリストに格納。空文字の行はスキップ
        InputStream stream = multipartFile.getInputStream();
        Reader reader = new InputStreamReader(stream);
        BufferedReader buf = new BufferedReader(reader);
        String line;
        while ((line = buf.readLine()) != null) {
            if (!ObjectUtils.isEmpty(line)) {
                cardList.add(line);
            }
        }
        reader.close();

        //作成したデッキリストをセット
        newSession.setInitialDeck(cardList);
        newSession.setShuffledDeck(cardList);

        //ログ出力
        loggingService.initializeLogging(cardList);

        //DB登録
        if (!CollectionUtils.isEmpty(cardList)) {
            insertInitialSession(newSession);
        }
        return newSession;
    }

    private void insertInitialSession(UserSession userSession) {
        //共通のインサート値を定義
        int decklistId = decklistMapper.selectNextDecklistId();
        String deleteFlg = Flg.OFF.getValue();
        Date registDate = new Date();



        ShuffleSession shuffleSession = new ShuffleSession();

        //セッション情報をentityに格納
        shuffleSession.setSessionId(userSession.getSessionId());
        shuffleSession.setInitialDecklistId(decklistId);
        shuffleSession.setDeleteFlg(deleteFlg);
        shuffleSession.setRegistDate(registDate);

        //セッション情報を登録
        shuffleSessionMapper.insert(shuffleSession);

        //デッキリスト情報を登録
        int cardOrder = 0;
        for (String cardName:userSession.getInitialDeck()) {
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
