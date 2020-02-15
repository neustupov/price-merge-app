package ru.neustupov.pricemergeapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
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

  public List<Price> mergePrices(List<Price> newPrices) {
    List<Price> oldPrices = getAllPrices();
    return merge(newPrices, oldPrices);
  }

  private List<Price> merge(List<Price> newPrices, List<Price> oldPrices) {
    ThreadPoolExecutor executor =
        (ThreadPoolExecutor) Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    List<Price> totalPrices = new ArrayList<>();
    List<Future<List<Price>>> futures = new ArrayList<>();
    for (Price newPrice : newPrices){
      List<Price> concurPrices = oldPrices.stream()
          .filter(oldPrice -> oldPrice.getProductCode().equals(newPrice.getProductCode())
              && oldPrice.getDepart() == newPrice.getDepart()
              && oldPrice.getNumber() == newPrice.getNumber())
          .collect(Collectors.toList());
      if (concurPrices.isEmpty()){
        totalPrices.add(newPrice);
      } else {
        for (Price oldPrice : concurPrices) {
          futures.add(executor.submit(new MergeTask(newPrice, oldPrice)));
        }
      }
    }

    try {
      executor.awaitTermination(1, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    futures.forEach(future -> {
      try {
        totalPrices.addAll(future.get());
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    });

    executor.shutdown();
    return totalPrices;
  }
}
