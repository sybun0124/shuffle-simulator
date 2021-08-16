package com.simulator.domain.model;

import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShuffleSession {

    private List<String> initialDeck;
    
    private List<String> shuffledDeck;
    
    private int shuffleCount;
    
    public void incliment() {
        this.shuffleCount += 1;
    }
}
