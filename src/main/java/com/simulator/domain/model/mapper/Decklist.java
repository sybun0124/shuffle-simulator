package com.simulator.domain.model.mapper;

import java.util.Date;
import lombok.Data;

/**
 * DECKLISTテーブル用のエンティティクラス。
 * @author asou
 *
 */
@Data
public class Decklist {

    //DECKLIST_ID
    private Integer decklistId;

    //CARD_ORDER
    private Integer cardOrder;

    //CARD_NAME
    private String cardName;

    //REGIST_DATE
    private Date registDate;

    //DELETE_FLG
    private String deleteFlg;
}