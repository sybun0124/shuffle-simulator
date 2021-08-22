package com.simulator.domain.service;

import com.simulator.application.common.enums.ShuffleType;
import com.simulator.domain.model.InitialLog;
import com.simulator.domain.model.LogCardList;
import com.simulator.domain.model.ShuffleLog;
import com.simulator.domain.repository.mapper.LogOutputMapper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogOutputService {
    private static final Logger logger = LoggerFactory.getLogger(LogOutputService.class);
    private final LogOutputMapper logOutputMapper;

    /**
     * デフォルトコンストラクタ。
     * @param logOutputMapper LogOutputMapper
     */
    public LogOutputService(LogOutputMapper logOutputMapper) {
        this.logOutputMapper = logOutputMapper;
    }

    /**
     * シャッフルログの出力用ファイル作成メソッド
     * @param sessionId SESSION_ID
     * @return
     * @throws IOException
     */
    public byte[] createFileShuffleLog(int sessionId) throws IOException {
        final InitialLog initialLog = logOutputMapper.selectInitailLog(sessionId);
        final List<ShuffleLog> shuffleLogList = logOutputMapper.selectShuffleLogList(sessionId);
        Path path = Paths.get("src/main/resources/log.txt");
        File file = new File(path.toString());
        FileWriter filewriter = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(filewriter);

        bw.write("[シャッフル前デッキリスト]");
        bw.newLine();
        bw = writeCardList(bw, initialLog.getCardList());
        bw.newLine();
        //シャッフル回数を出力するためのカウント
        int i = 0;
        for (ShuffleLog shuffleLog:shuffleLogList) {
            i++;
            bw.write("[シャッフル" + i + "回目]");
            bw.newLine();
            ShuffleType shuffleType = ShuffleType.getShuffleTypeByCode(shuffleLog.getShuffleType());
            bw.write("[シャッフル種別:" + shuffleType.getLabel() + "]");
            if (shuffleType == ShuffleType.DEAL) {
                bw.newLine();
                bw.write("[" + shuffleLog.getDealNum() + "枚区切り]");
            }
            bw.newLine();
            bw = writeCardList(bw, shuffleLog.getCardList());
            bw.newLine();
        }
        bw.close();

        return Files.readAllBytes(Paths.get("src/main/resources/log.txt"));
    }

    private BufferedWriter writeCardList(BufferedWriter bw, List<LogCardList> cardList) {
        for (LogCardList cardName:cardList) {
            try {
                bw.write(cardName.getCardName());
                bw.newLine();
            } catch (IOException e) {
                logger.warn("エラー発生:カード名出力スキップ");
            }
        }
        return bw;
    }
}
