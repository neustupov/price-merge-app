package ru.neustupov.pricemergeapp.controller;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neustupov.pricemergeapp.model.Price;
import ru.neustupov.pricemergeapp.service.PriceService;

@Log4j2
@RestController
@RequestMapping("/api/v1/prices")
public class PriceController {

  private PriceService priceService;

  public PriceController(PriceService priceService) {
    this.priceService = priceService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Price> getAllPrices() {
    log.info("Get all prices method");
    return priceService.getAllPrices();
  }

  @PostMapping(value = "/merge", consumes = MediaType.APPLICATION_JSON_VALUE)
  public List<Price> mergePrices(@RequestBody List<Price> priceList) {
    log.info("Merge prices method");
    return priceService.mergePrices(priceList);
  }
}
