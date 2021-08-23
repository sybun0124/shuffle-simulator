package com.simulator.application.common.enums;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ShuffleType {
    DEAL("ディールシャッフル", "1"),
    HINDU("ヒンドゥーシャッフル", "2"),
    RIFFLE("リフルシャッフル", "3"),
    RIFFLE_PERFECT("リフルシャッフル（パーフェクト）", "4");

    private String label;
    private String value;

    private ShuffleType(String label, String value) {
        this.label = label;
        this.value = value;
    }

    /**
     * valueから逆引きしてEnum値を返すメソッド。
     * @param value 逆引きするvalue
     * @return
     */
    public static ShuffleType getShuffleTypeByCode(String value) {
        return Arrays.stream(values())
                .filter(v -> v.getValue().equals(value))
                .findFirst()
                .orElse(DEAL);
    }
}
