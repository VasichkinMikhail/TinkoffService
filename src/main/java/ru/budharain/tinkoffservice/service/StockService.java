package ru.budharain.tinkoffservice.service;

import ru.budharain.tinkoffservice.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);
}
