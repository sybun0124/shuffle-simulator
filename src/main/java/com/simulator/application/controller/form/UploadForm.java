package com.simulator.application.controller.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * ファイルアップロードのフォームクラス。
 */
@Data
public class UploadForm {
    private MultipartFile multipartFile;
}
