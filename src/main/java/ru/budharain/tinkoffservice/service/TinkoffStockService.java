package ru.budharain.tinkoffservice.service;

import dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.budharain.tinkoffservice.exeption.StockNotFoundException;
import ru.budharain.tinkoffservice.model.Currency;
import ru.budharain.tinkoffservice.model.Stock;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class TinkoffStockService implements StockService{
    private  final OpenApi openApi;
    @Async
    public CompletableFuture<MarketInstrumentList> getMarketInstrumentTicker(String ticker){
        var context = openApi.getMarketContext();
        return  context.searchMarketInstrumentsByTicker(ticker);
    }
    @Override
    public Stock getStockByTicker(String ticker) {
        var cf = getMarketInstrumentTicker(ticker);
        var list = cf.join().getInstruments();
        if (list.isEmpty()){
            throw new StockNotFoundException(String.format("Stock %S not found",ticker));
        }
        var item = list.get(0);
        return  new Stock(
                item.getTicker(),
                item.getFigi(),
                item.getName(),
                item.getType().getValue(),
                Currency.valueOf(item.getCurrency().getValue()),
                "TINKOFF");
    }
    @Override
    public StocksDto getStocksByTickers(TickersDto tickers){
        List<CompletableFuture<MarketInstrumentList>> marketInstrument = new ArrayList<>();
        tickers.getTickers().forEach(ticker -> marketInstrument.add(getMarketInstrumentTicker(ticker)));
        List<Stock> stocks = marketInstrument.stream()
                .map(CompletableFuture::join)
                .map(mi ->{
                    if(!mi.getInstruments().isEmpty()){
                        return mi.getInstruments().get(0);
                    }
                    return null;
                })
                .filter(el -> Objects.nonNull(el))
                .map(mi -> new Stock(
                        mi.getTicker(),
                        mi.getFigi(),
                        mi.getName(),
                        mi.getType().getValue(),
                        Currency.valueOf(mi.getCurrency().getValue()),
                        "TINKOFF"
                ))
                .collect(Collectors.toList());

        return new StocksDto(stocks);
    }
    @Async
    public CompletableFuture<Optional<Orderbook>> getOrderBookByFigi(String figi){
        var orderbook = openApi.getMarketContext().getMarketOrderbook(figi,0);
        log.info("Getting price {} from Tinkoff",figi);
        return orderbook;
    }
    @Override
    public StocksPricesDto getPrices(FigiesDto figiesDto){
        long start = System.currentTimeMillis();
        List<CompletableFuture<Optional<Orderbook>>> orderbooks = new ArrayList<>();
        figiesDto.getFigies().forEach(figi -> orderbooks.add(getOrderBookByFigi(figi)));
        var listPrices = orderbooks.stream()
                        .map(CompletableFuture::join)
                                .map(oo -> oo.orElseThrow(()-> new StockNotFoundException("Stock not found")))
                                        .map(orderbook -> new StockPrice(
                                                orderbook.getFigi(),
                                                orderbook.getLastPrice().doubleValue()
                                        ))
                                                .collect(Collectors.toList());
        log.info("Time - {}", System.currentTimeMillis() - start);
        return new StocksPricesDto(listPrices);
    }
}
