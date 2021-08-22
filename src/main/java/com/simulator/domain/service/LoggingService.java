package com.simulator.domain.service;

import com.simulator.application.common.enums.ShuffleType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {
    private static final Logger logger = LoggerFactory.getLogger(SessionService.class);

    public void initializeLogging(List<String> cardList) {
        logger.info("デッキリスト初期化：" + cardList);
    }

    public void shuffleLogging(List<String> cardList, ShuffleType shuffleType) {
        logger.info("シャッフル実行：" + shuffleType.getLabel());
        logger.info("実行後デッキリスト：" + cardList);
    }
}
