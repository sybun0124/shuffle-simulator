package com.simulator.domain.repository.mapper;

import com.simulator.domain.model.InitialLog;
import com.simulator.domain.model.ShuffleLog;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * ファイルログ出力用マッパーインターフェース。
 * @author asou
 *
 */
@Mapper
public interface LogOutputMapper {

    /**
     * デッキ取り込み時ログ用オブジェクト取得。
     * @param sessionId SESSION_ID
     * @return
     */
    public InitialLog selectInitailLog(@Param("sessionId")int sessionId);

    /**
     * シャッフルログ用オブジェクト取得。
     * @param sessionId SESSION_ID
     * @return
     */
    public List<ShuffleLog> selectShuffleLogList(@Param("sessionId")int sessionId);
}
