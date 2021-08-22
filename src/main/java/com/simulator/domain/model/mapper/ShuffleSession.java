package com.simulator.domain.model.mapper;

import java.util.Date;
import lombok.Data;

/**
 * SHUFFLE_SESSIONテーブル用のエンティティクラス。
 * @author asou
 *
 */
@Data
public class ShuffleSession {

    //SESSION_ID
    private Integer sessionId;

    //INITIAL_DECKLIST_ID
    private Integer initialDecklistId;

    //REGIST_DATE
    private Date registDate;

    //DELETE_FLG
    private String deleteFlg;

}