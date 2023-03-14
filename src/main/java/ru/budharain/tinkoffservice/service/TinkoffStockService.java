package ru.budharain.tinkoffservice.service;

import ru.budharain.tinkoffservice.model.Stock;
import ru.tinkoff.invest.openapi.OpenApi;

public class TinkoffStockService implements StockService{
    private  final OpenApi openApi;
    @Override
    public Stock getStockByTicker(String ticker) {

    }
}
