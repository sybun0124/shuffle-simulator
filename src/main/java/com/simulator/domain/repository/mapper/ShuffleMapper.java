package com.simulator.domain.repository.mapper;

import com.simulator.domain.model.mapper.Shuffle;
import org.apache.ibatis.annotations.Mapper;

/**
 * SHUFFLEテーブル用マッパーインターフェース。
 * @author asou
 *
 */
@Mapper
public interface ShuffleMapper {

    /**
     * 1件インサート。
     * @param record エンティティクラスオブジェクト
     * @return
     */
    int insert(Shuffle record);

    /**
     * 次のSHUFFLE_IDを取得。
     * @return
     */
    int selectNextShuffleId();
}