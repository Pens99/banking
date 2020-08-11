package com.crealogix.apprentice.banking.test;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PortfolioTest {

    @Mock
    private StockService stockService;

    private Portfolio portfolio;

    @Mock
    private Stock stock1;

    @Mock
    private Stock stock2;

    private static List<Stock> stocks = new ArrayList<>();

    @BeforeEach
    void setUp() {
        stocks.add(stock1);
        stocks.add(stock2);
        portfolio = Mockito.mock(Portfolio.class, Mockito.CALLS_REAL_METHODS);
        ReflectionTestUtils.setField(portfolio, "stockService", stockService);
        ReflectionTestUtils.setField(portfolio, "stocks", stocks);
    }

    @Test
    void getMarketValueWithoutQuantity() {
        Mockito.when(stockService.getPrice(stock1)).thenReturn(2.0);
        Mockito.when(stockService.getPrice(stock2)).thenReturn(3.0);
        Assertions.assertThat(portfolio.getMarketValue()).isEqualTo(00.0);
        verify(stockService, times(2)).getPrice(any(Stock.class));
        verify(portfolio, times(1)).getMarketValue();
    }

    @Test
    void getMarketValueWithQuantity() {
        Mockito.when(stock1.getQuantity()).thenReturn(2);
        Mockito.when(stock2.getQuantity()).thenReturn(2);
        Mockito.when(stockService.getPrice(stock1)).thenReturn(2.0);
        Mockito.when(stockService.getPrice(stock2)).thenReturn(3.0);
        Assertions.assertThat(portfolio.getMarketValue()).isEqualTo(10.0);
        verify(stockService, times(2)).getPrice(any(Stock.class));
        verify(portfolio, times(1)).getMarketValue();
    }

    @Test
    void getMarketValueWithZeroQuantity() {
        Mockito.when(stock2.getQuantity()).thenReturn(0);
        Mockito.when(stock2.getQuantity()).thenReturn(0);
        Mockito.when(stockService.getPrice(stock1)).thenReturn(0.0);
        Mockito.when(stockService.getPrice(stock2)).thenReturn(0.0);
        Assertions.assertThat(portfolio.getMarketValue()).isEqualTo(0.0);
        verify(stockService, times(2)).getPrice(any(Stock.class));
        verify(portfolio, times(1)).getMarketValue();
    }

    @Test
    void getMarketValueWithNoPrices() {
        Mockito.when(stock1.getQuantity()).thenReturn(2);
        Mockito.when(stock2.getQuantity()).thenReturn(2);
        Mockito.when(stockService.getPrice(stock1)).thenReturn(0.0);
        Mockito.when(stockService.getPrice(stock2)).thenReturn(0.0);
        Assertions.assertThat(portfolio.getMarketValue()).isEqualTo(0.0);
        verify(stockService, times(2)).getPrice(any(Stock.class));
        verify(portfolio, times(1)).getMarketValue();
    }
}