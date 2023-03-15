package ru.budharain.tinkoffservice.service;

import dto.FigiesDto;
import dto.StocksDto;
import dto.StocksPricesDto;
import dto.TickersDto;
import ru.budharain.tinkoffservice.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);
    StocksDto getStocksByTickers(TickersDto tickersDto);
    StocksPricesDto getPrices(FigiesDto figiesDto);
}
