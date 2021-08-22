package com.simulator.application.controller;

import com.simulator.application.common.enums.ShuffleType;
import com.simulator.application.controller.form.ShuffleForm;
import com.simulator.application.controller.form.UploadForm;
import com.simulator.domain.model.UserSession;
import com.simulator.domain.service.LogOutputService;
import com.simulator.domain.service.SessionService;
import com.simulator.domain.service.ShuffleService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ShuffleSimulatorContoroller {
    private static final Logger logger = LoggerFactory.getLogger(ShuffleSimulatorContoroller.class);
    private final SessionService sessionServise;
    private final ShuffleService shuffleServise;
    private final LogOutputService logOutputService;
    //セッションオブジェクトはコンテナから取り出すが値は都度変更する
    private UserSession userSession;

    /**
     * デフォルトコンストラクタ。
     * @param deckService DeckService
     * @param shuffleServise ShuffleServise
     * @param logOutputService LogOutputService
     * @param userSession UserSession
     */
    public ShuffleSimulatorContoroller(SessionService deckService,
                                       ShuffleService shuffleServise,
                                       LogOutputService logOutputService,
                                       UserSession userSession)  {
        this.sessionServise = deckService;
        this.shuffleServise = shuffleServise;
        this.logOutputService = logOutputService;
        this.userSession = userSession;
    }

    /**
     * 画面表示メソッド。
     * @param shuffleForm シャッフル選択フォームクラス
     * @param uploadForm デッキリストファイルのアップロードフォームクラス
     * @param model 画面表示用Model
     * @return
     */
    @GetMapping("index")
    public String index(ShuffleForm shuffleForm, UploadForm uploadForm, Model model)  {

        model.addAttribute("shuffleForm", shuffleForm);
        model.addAttribute("uploadForm", uploadForm);

        model.addAttribute("shuffleTypeList",
                           ShuffleType.values());
        model.addAttribute("initialCardList", userSession.getInitialDeck());
        model.addAttribute("shuffledCardList", userSession.getShuffledDeck());
        return "tables";
    }

    /**
     * シャッフル処理。
     * @param uploadForm デッキリストファイルのアップロードフォームクラス
     * @param shuffleForm シャッフル選択フォームクラス
     * @param bindingResult バリデーション結果の格納先オブジェクト
     * @param model 画面表示用Model
     * @return
     */
    @PostMapping(value = "shuffle", params = "shuffle")
    public String shuffle(UploadForm uploadForm,
                           @ModelAttribute @Validated ShuffleForm shuffleForm,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<String> errorList = new ArrayList<String>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
            model.addAttribute("validationError", errorList);
            return index(shuffleForm, uploadForm, model);
        }
        userSession.setShuffledDeck(
                shuffleServise.doShuffle(
                        userSession.getShuffledDeck(), shuffleForm, userSession.getSessionId()));
        return index(shuffleForm, uploadForm, model);
    }

    /**
     * シャッフルログtxtファイル出力メソッド。
     * @param shuffleForm シャッフル選択フォームクラス
     * @param uploadForm デッキリストファイルのアップロードフォームクラス
     * @return
     */
    @PostMapping(value = "shuffle", params = "logOutput")
    @ResponseBody
    public ResponseEntity<byte[]> logOutput(ShuffleForm shuffleForm,
                                             UploadForm uploadForm) {
        try {
            byte[] data = logOutputService.createFileShuffleLog(userSession.getSessionId());
            // ResponseHeader
            HttpHeaders header = new HttpHeaders();
            header.add("Content-Type", "text/plain");
            header.setContentDispositionFormData("filename", "log.txt");

            return new ResponseEntity<>(data, header, HttpStatus.OK);
        } catch (IOException e) {
            logger.warn("エラー発生:ログファイル出力失敗");
        }
        return null;
    }

    /**
     * デッキリストのtxtファイル取り込みメソッド。
     * @param model 画面表示用Model
     * @param shuffleForm シャッフル選択フォームクラス
     * @param uploadForm デッキリストファイルのアップロードフォームクラス
     * @return 画面表示用Model
     */
    @PostMapping("upload")
    public String upload(Model model, ShuffleForm shuffleForm, UploadForm uploadForm) {
        try {
            userSession =
                    sessionServise.importDeckFromTxt(uploadForm.getMultipartFile(), userSession);
        } catch (IOException e) {
            model.addAttribute("msg", "デッキ取り込みに失敗しました。");
        }
        return index(shuffleForm, uploadForm, model);

    }




}
