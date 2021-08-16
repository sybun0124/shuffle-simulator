package com.simulator.domain.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * デッキデータ作成用のサービスクラス
 * @author asou
 *
 */
@Service
public class DeckService {
    private final RestTemplate restTemplate;
    
    public DeckService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> importDeckFromTxt() throws IOException {
        List<String> cardList = new ArrayList<String>();
        //分割文字は半角スペース
        String regex  = " ";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/static/deck/sample_deck.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitedLine = line.split(regex, 2);
                 //分割文字列の前半はカード枚数
                 int numberOfCard = Integer.parseInt(splitedLine[0]);
                 //分割文字列の後半はカード名
                 String card = splitedLine[1];
                
                 //枚数分カードを格納
                 IntStream.range(0, numberOfCard).forEach(i -> cardList.add(card));
            }
            reader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return cardList;
    }
    
}
