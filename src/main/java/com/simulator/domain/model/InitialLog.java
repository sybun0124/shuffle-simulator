package com.simulator.domain.model;

import java.util.List;
import lombok.Data;

/**
 * デッキ取り込みログ出力用モデル。
 * @author asou
 *
 */
@Data
public class InitialLog {

    //SESSION_ID
    private int sessionId;

    //取り込み時のカードリスト
    private List<LogCardList> cardList;
}
