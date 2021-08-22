package com.simulator.domain.model.mapper;

import java.util.Date;
import lombok.Data;

/**
 * SHUFFLEテーブル用のエンティティクラス。
 * @author asou
 *
 */
@Data
public class Shuffle {

    //SHUFFLE_ID
    private Integer shuffleId;

    //SESSION_ID
    private Integer sessionId;

    //DECKLIST_ID
    private Integer decklistId;

    //SHUFFLE_TYPE
    private String shuffleType;

    //DEAL_NUM
    private Integer dealNum;

    //REGIST_DATE
    private Date registDate;

    //DELETE_FLG
    private String deleteFlg;

}