package com.simulator.domain.repository.mapper;

import com.simulator.domain.model.mapper.Decklist;
import org.apache.ibatis.annotations.Mapper;

/**
 * DECKLISTテーブル用マッパーインターフェース。
 * @author asou
 *
 */
@Mapper
public interface DecklistMapper {

    /**
     * 1件インサート。
     * @param record エンティティクラスオブジェクト
     * @return
     */
    int insert(Decklist record);

    /**
     * 次のDECKLIST_IDを取得。
     * @return
     */
    int selectNextDecklistId();
}