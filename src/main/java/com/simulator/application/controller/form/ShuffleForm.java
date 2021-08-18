package com.simulator.application.controller.form;

import lombok.Data;
import lombok.Getter;

@Data
public class ShuffleForm {

    //シャッフルの種類
    private ShuffleType shuffleType;

    //数
    private int number;

    @Getter
    public enum ShuffleType{
        DEAL("ディールシャッフル", "1"),
        HINDU("ヒンドゥーシャッフル", "2"),
        RIFFLE("リフルシャッフル", "3");

        private String label;
        private String id;

        private ShuffleType(String label, String id) {   //コンストラクタはprivateで宣言
            this.label = label;
            this.id = id;
        }


    }

}
