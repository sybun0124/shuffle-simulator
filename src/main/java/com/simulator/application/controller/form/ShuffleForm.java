package com.simulator.application.controller.form;

import com.simulator.application.common.enums.ShuffleType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ShuffleForm {

    //シャッフルの種類
    private ShuffleType shuffleType;

    //ディールシャッフル時の区分けの数/その他シャッフルの回数
    @NotBlank(message = "枚数/回数を入力してください")
    @Pattern(regexp = "^[0-9]*$",message = "枚数/回数は半角数字のみで入力してください")
    private String number;

}
