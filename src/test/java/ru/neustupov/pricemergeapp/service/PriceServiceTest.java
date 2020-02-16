package ru.neustupov.pricemergeapp.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.neustupov.pricemergeapp.model.Price;

@SpringBootTest
class PriceServiceTest {

  @Autowired
  private PriceService priceService;

  private List<Price> newPrices = new ArrayList<>();
  private List<Price> priceFor122856_1_1 = new ArrayList<>();
  private List<Price> priceFor122856_2_1 = new ArrayList<>();
  private List<Price> priceFor6654 = new ArrayList<>();
  private List<Price> priceFor100 = new ArrayList<>();
  private List<Price> priceFor200 = new ArrayList<>();
  private List<Price> priceFor300 = new ArrayList<>();

  @BeforeEach
  void init() {

    // Common new prices
    newPrices.add(Price.builder().productCode("122856").number(1).depart(1)
        .begin(LocalDateTime.of(2013,1,20,0,0,0))
        .end(LocalDateTime.of(2013,2,20,23,59,59))
        .value(11000L).build());
    newPrices.add(Price.builder().productCode("122856").number(2).depart(1)
        .begin(LocalDateTime.of(2013,1,15,0,0,0))
        .end(LocalDateTime.of(2013,1,25,23,59,59))
        .value(92000L).build());
    newPrices.add(Price.builder().productCode("6654").number(1).depart(2)
        .begin(LocalDateTime.of(2013,1,12,0,0,0))
        .end(LocalDateTime.of(2013,1,13,0,0,0))
        .value(4000L).build());
    newPrices.add(Price.builder().productCode("100").number(1).depart(1)
        .begin(LocalDateTime.of(2020,1,10,0,0,0))
        .end(LocalDateTime.of(2020,1,20,0,0,0))
        .value(11000L).build());
    newPrices.add(Price.builder().productCode("200").number(1).depart(1)
        .begin(LocalDateTime.of(2020,1,10,0,0,0))
        .end(LocalDateTime.of(2020,1,20,23,59,59))
        .value(11000L).build());
    newPrices.add(Price.builder().productCode("300").number(1).depart(1)
        .begin(LocalDateTime.of(2020,1,5,0,0,0))
        .end(LocalDateTime.of(2020,1,15,23,59,59))
        .value(1000L).build());
    newPrices.add(Price.builder().productCode("300").number(1).depart(1)
        .begin(LocalDateTime.of(2020,1,15,0,0,0))
        .end(LocalDateTime.of(2020,1,25,23,59,59))
        .value(13000L).build());

    // Prices for 122856_1_1
    priceFor122856_1_1.add(Price.builder().id(1L).productCode("122856").number(1).depart(1)
        .begin(LocalDateTime.of(2013,1,1,0,0,0))
        .end(LocalDateTime.of(2013,2,20,23,59,59))
        .value(11000L).build());

    // Prices for 122856_2_1
    priceFor122856_2_1.add(Price.builder().id(2L).productCode("122856").number(2).depart(1)
        .begin(LocalDateTime.of(2013,1,10,0,0,0))
        .end(LocalDateTime.of(2013,1,15,0,0,0))
        .value(99000L).build());
    priceFor122856_2_1.add(Price.builder().productCode("122856").number(2).depart(1)
        .begin(LocalDateTime.of(2013,1,15,0,0,0))
        .end(LocalDateTime.of(2013,1,25,23,59,59))
        .value(92000L).build());

    // Prices for 6654
    priceFor6654.add(Price.builder().productCode("6654").number(1).depart(2)
        .begin(LocalDateTime.of(2013,1,1,0,0,0))
        .end(LocalDateTime.of(2013,1,12,0,0,0))
        .value(5000L).build());
    priceFor6654.add(Price.builder().productCode("6654").number(1).depart(2)
        .begin(LocalDateTime.of(2013,1,13,0,0,0))
        .end(LocalDateTime.of(2013,1,31,0,0,0))
        .value(5000L).build());
    priceFor6654.add(Price.builder().productCode("6654").number(1).depart(2)
        .begin(LocalDateTime.of(2013,1,12,0,0,0))
        .end(LocalDateTime.of(2013,1,13,0,0,0))
        .value(4000L).build());

    // Prices for 100
    priceFor100.add(Price.builder().productCode("100").number(1).depart(1)
        .begin(LocalDateTime.of(2020,1,1,0,0,0))
        .end(LocalDateTime.of(2020,1,10,0,0,0))
        .value(1000L).build());
    priceFor100.add(Price.builder().productCode("100").number(1).depart(1)
        .begin(LocalDateTime.of(2020,1,20,0,0,0))
        .end(LocalDateTime.of(2020,1,31,0,0,0))
        .value(1000L).build());
    priceFor100.add(Price.builder().productCode("100").number(1).depart(1)
        .begin(LocalDateTime.of(2020,1,10,0,0,0))
        .end(LocalDateTime.of(2020,1,20,0,0,0))
        .value(11000L).build());

    // Prices for 200
    priceFor200.add(Price.builder().id(5L).productCode("200").number(1).depart(1)
        .begin(LocalDateTime.of(2020,1,1,0,0,0))
        .end(LocalDateTime.of(2020,1,10,0,0,0))
        .value(1000L).build());
    priceFor200.add(Price.builder().productCode("200").number(1).depart(1)
        .begin(LocalDateTime.of(2020,1,10,0,0,0))
        .end(LocalDateTime.of(2020,1,20,23,59,59))
        .value(11000L).build());
    priceFor200.add(Price.builder().id(6L).productCode("200").number(1).depart(1)
        .begin(LocalDateTime.of(2020,1,20,23,59,59))
        .end(LocalDateTime.of(2020,1,31,0,0,0))
        .value(2000L).build());

    // Prices for 300
    priceFor300.add(Price.builder().id(7L).productCode("300").number(1).depart(1)
        .begin(LocalDateTime.of(2020,1,1,0,0,0))
        .end(LocalDateTime.of(2020,1,15,23,59,59))
        .value(1000L).build());
    priceFor300.add(Price.builder().productCode("300").number(1).depart(1)
        .begin(LocalDateTime.of(2020,1,15,0,0,0))
        .end(LocalDateTime.of(2020,1,25,23,59,59))
        .value(13000L).build());
    priceFor300.add(Price.builder().id(9L).productCode("300").number(1).depart(1)
        .begin(LocalDateTime.of(2020,1,25,23,59,59))
        .end(LocalDateTime.of(2020,1,31,0,0,0))
        .value(3000L).build());
  }

  @Test
  void getAllPrices() {
    List<Price> priceList = priceService.getAllPrices();
    assertEquals(priceList.size(), 9);
  }

  @Test
  void mergePrices122856() {
    List<Price> mergedPrices = priceService.mergePrices(newPrices);

    List<Price> priceFor122856_2_1_act = mergedPrices.stream()
        .filter(price -> (price.getProductCode().equals("122856")))
        .filter(price -> price.getNumber() == 1)
        .collect(Collectors.toList());
    assertEquals(priceFor122856_2_1_act.size(), 1);
    assertIterableEquals(priceFor122856_2_1_act, priceFor122856_1_1);

    List<Price> priceFor122856_1_1_act = mergedPrices.stream()
        .filter(price -> price.getProductCode().equals("122856"))
        .filter(price ->  price.getNumber() == 2)
        .collect(Collectors.toList());
    assertEquals(priceFor122856_1_1_act.size(), 2);
    assertIterableEquals(priceFor122856_1_1_act, priceFor122856_2_1);
  }

  @Test
  void mergePrices6654() {
    List<Price> mergedPrices = priceService.mergePrices(newPrices);

    List<Price> priceFor6654_act = mergedPrices.stream()
        .filter(price -> price.getProductCode().equals("6654")).collect(Collectors.toList());
    assertEquals(priceFor6654_act.size(), 3);
    assertIterableEquals(priceFor6654_act, priceFor6654);
  }

  @Test
  void mergePrices100() {
    List<Price> mergedPrices = priceService.mergePrices(newPrices);

    List<Price> priceFor100_act = mergedPrices.stream()
        .filter(price -> price.getProductCode().equals("100")).collect(Collectors.toList());
    assertEquals(priceFor100_act.size(), 3);
    assertIterableEquals(priceFor100_act, priceFor100);
  }

  @Test
  void mergePrices200() {
    List<Price> mergedPrices = priceService.mergePrices(newPrices);

    List<Price> priceFor200_act = mergedPrices.stream()
        .filter(price -> price.getProductCode().equals("200")).collect(Collectors.toList());
    assertEquals(priceFor200_act.size(), 3);
    assertIterableEquals(priceFor200_act, priceFor200);
  }

  @Test
  void mergePrices300() {
    List<Price> mergedPrices = priceService.mergePrices(newPrices);

    List<Price> priceFor300_act = mergedPrices.stream()
        .filter(price -> price.getProductCode().equals("300")).collect(Collectors.toList());
    assertEquals(priceFor300_act.size(), 3);
    assertIterableEquals(priceFor300_act, priceFor300);
  }
}