package com.simulator.application.controller.form;

import com.simulator.application.common.config.annotation.Deal;
import com.simulator.application.common.enums.ShuffleType;
import lombok.Data;

@Data
@Deal(fields = {"shuffleType", "number"})
public class ShuffleForm {

    //シャッフルの種類
    private ShuffleType shuffleType;

    //ディールシャッフル時の区分けの数
    private String number;

}
