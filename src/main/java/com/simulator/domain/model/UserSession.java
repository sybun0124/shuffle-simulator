package com.simulator.domain.model;

import java.util.List;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * デッキ取り込み時からセッション破棄/デッキ再取り込みまでの情報を保持するオブジェクトクラス。
 * @author asou
 *
 */
@Data
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession {

    //取り込み時デッキリスト
    private List<String> initialDeck;

    //シャッフル後デッキリスト
    private List<String> shuffledDeck;

    //SESSION_ID
    private int sessionId;

}