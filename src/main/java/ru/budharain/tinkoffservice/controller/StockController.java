package ru.budharain.tinkoffservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import ru.budharain.tinkoffservice.model.Stock;


public class StockController {
    private final StockService stockService;
    @GetMapping("/stocks/{ticker}")
    public Stock getStock(String ticker){
       return stockService.getStockByTicker(ticker);
    }
}
