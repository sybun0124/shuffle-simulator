package com.simulator.domain.model;

import java.util.List;
import lombok.Data;

/**
 * シャッフル結果のファイルログ出力用モデル。
 * @author asou
 *
 */
@Data
public class ShuffleLog {

    //SHUFFLE_ID
    private int shuffleId;

    //シャッフル種別。valueで取り出して出力時に逆引きする
    private String shuffleType;

    //ディールシャッフル時の区分けの数
    private Integer dealNum;

    //シャッフル後のカードリスト
    private List<LogCardList> cardList;


}
