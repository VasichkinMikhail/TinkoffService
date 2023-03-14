package ru.budharain.tinkoffservice.exeption;

public class StockNotFoundException extends RuntimeException{
    public StockNotFoundException(String message){ super(message);}
}
