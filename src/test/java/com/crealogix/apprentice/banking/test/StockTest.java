package com.crealogix.apprentice.banking.test;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class StockTest {

    String stockId  = "Id1";
    String name = "StockName";
    int quantity = 3;

    @InjectMocks
    private Stock stock = new Stock(stockId, name, quantity);;

    @Test
    void getStockId(){
        Assertions.assertThat(stock.getStockId()).isEqualTo(stockId);
    }

    @Test
    void getName(){
        Assertions.assertThat(stock.getName()).isEqualTo(name);
    }

    @Test
    void getQuantity(){
        Assertions.assertThat(stock.getQuantity()).isEqualTo(quantity);
    }

}
