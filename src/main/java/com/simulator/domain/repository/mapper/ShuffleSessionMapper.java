
package com.simulator.domain.repository.mapper;

import com.simulator.domain.model.mapper.ShuffleSession;
import org.apache.ibatis.annotations.Mapper;

/**
 * SHUFFLE_SESSIONテーブル用マッパーインターフェース。
 * @author asou
 *
 */
@Mapper
public interface ShuffleSessionMapper {

    /**
     * 1件インサート。
     * @param record エンティティクラスオブジェクト
     * @return
     */
    int insert(ShuffleSession record);

    /**
     * 次のSESSION_IDを取得。
     * @return
     */
    int selectNextSessionId();
}