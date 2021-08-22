package com.simulator.domain.model;

import lombok.Data;

/**
 * ログ出力時のカード情報クラス。
 * Listで使用
 * @author asou
 *
 */
@Data
public class LogCardList {

    //カード名
    private String cardName;

    //カードのリスト内での順番。mybatisでのマッピング時に同名カードをまとめてしまわないように定義
    private int cardOrder;
}
