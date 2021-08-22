package com.simulator.application.common.enums;

import lombok.Getter;

@Getter
public enum Flg {
    OFF("0"),
    ON("1"),;

    private final String value;

    Flg(String value) {
        this.value = value;
    }

}
