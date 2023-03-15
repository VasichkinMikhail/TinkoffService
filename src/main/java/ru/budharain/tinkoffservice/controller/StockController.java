package ru.budharain.tinkoffservice.controller;

import dto.FigiesDto;
import dto.StocksDto;
import dto.StocksPricesDto;
import dto.TickersDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
    @PostMapping("/stocks/getStocksByTickers")
    public StocksDto getStocksByTickers(@RequestBody TickersDto tickersDto){
        return stockService.getStocksByTickers(tickersDto);
    }
    @PostMapping("/prices")
    public StocksPricesDto getPrices(@RequestBody FigiesDto figiesDto){
        return stockService.getPrices(figiesDto);
    }

}
