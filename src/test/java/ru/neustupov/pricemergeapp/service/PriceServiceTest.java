package ru.neustupov.pricemergeapp.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.neustupov.pricemergeapp.model.Price;

@SpringBootTest
class PriceServiceTest {

  @Autowired
  private PriceService priceService;

  @BeforeEach
  void setUp() {
  }

  @Test
  void getAllPrices() {
    List<Price> priceList = priceService.getAllPrices();
    assertEquals(priceList.size(), 9);
  }

  @Test
  void mergePrices() {
  }
}