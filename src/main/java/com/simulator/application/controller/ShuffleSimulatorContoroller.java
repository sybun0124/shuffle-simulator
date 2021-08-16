package com.simulator.application.controller;

import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.simulator.application.controller.form.ShuffleForm;
import com.simulator.application.controller.form.ShuffleForm.ShuffleType;
import com.simulator.domain.model.ShuffleSession;
import com.simulator.domain.service.DeckService;
import com.simulator.domain.service.ShuffleService;

@Controller
public class ShuffleSimulatorContoroller {
    private final DeckService deckServise;
    private final ShuffleService shuffleServise;
    private final ShuffleSession shuffleSession;
    
    public ShuffleSimulatorContoroller(DeckService deckService, 
                                       ShuffleService shuffleServise, 
                                       ShuffleSession shuffleSession)  {
        this.deckServise = deckService;
        this.shuffleServise = shuffleServise;
        this.shuffleSession = shuffleSession;
    }
    
    @GetMapping("index")
    public String index(Model model,ShuffleForm shuffleForm)  {
        if(CollectionUtils.isEmpty(shuffleSession.getInitialDeck())) {
            try {
                shuffleSession.setInitialDeck(deckServise.importDeckFromTxt());
//                shuffleSession.setInitialDeck(deckServise.setImagePath(shuffleSession.getInitialDeck()));
                shuffleSession.setShuffledDeck(shuffleSession.getInitialDeck());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        model.addAttribute("shuffleForm", shuffleForm);
        model.addAttribute("shuffleTypeList", ShuffleType.values());
        model.addAttribute("shuffleCount", shuffleSession.getShuffleCount());
        model.addAttribute("initialCardList", shuffleSession.getInitialDeck());
        model.addAttribute("shuffledCardList", shuffleSession.getShuffledDeck());
        return "tables";
    }
    
    @PostMapping("shuffle")
    public String shuffle(Model model,ShuffleForm shuffleForm) {
        //シャッフル回数を一回追加
        shuffleSession.incliment();
        
        shuffleSession.setShuffledDeck(shuffleServise.doShuffle(shuffleSession.getShuffledDeck(), shuffleForm));
        
        return index(model, shuffleForm);
    }
    
    
    
    
}
