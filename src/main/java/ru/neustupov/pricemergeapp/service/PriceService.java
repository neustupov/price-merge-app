package ru.neustupov.pricemergeapp.service;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.neustupov.pricemergeapp.model.Price;
import ru.neustupov.pricemergeapp.repository.PriceRepository;

@Service
public class PriceService {

  @Autowired
  private PriceRepository priceRepository;

  public List<Price> getAllPrices(){
    return priceRepository.findAll();
  }

  public List<Price> mergePrices(List<Price> newPrices){
    List<Price> oldPrices = getAllPrices();
    return merge(newPrices, oldPrices);
  }

  private List<Price> merge(List<Price> newPrices, List<Price> oldPrices){
    return Collections.emptyList();
  }
}
