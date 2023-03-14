package ru.budharain.tinkoffservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.budharain.tinkoffservice.model.Stock;
import ru.budharain.tinkoffservice.service.StockService;

@RestController
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;
    @GetMapping("/stocks/{ticker}")
    public Stock getStock(@PathVariable String ticker){
       return stockService.getStockByTicker(ticker);
    }
}
