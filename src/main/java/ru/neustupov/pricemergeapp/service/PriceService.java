package ru.neustupov.pricemergeapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    Map<String, List<Price>> compPriceMap = oldPrices.stream().collect(Collectors.groupingBy(
        this::makeGroupId));

    for (Price newPrice : newPrices){
      List<Price> concurPrices = compPriceMap.get(makeGroupId(newPrice));
      // Если совпадений не найдено, просто добавляем новую цену в список без обработки
      if (concurPrices.isEmpty()){
        totalPrices.add(newPrice);
      } else {
        for (Price oldPrice : concurPrices) {
          Price price = new Price(oldPrice);
          futures.add(executor.submit(new MergeTask(newPrice, price)));
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
        future.get().forEach(price -> {
          if (!totalPrices.contains(price)){
            totalPrices.add(price);
          }
        });
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    });

    executor.shutdown();
    return totalPrices;
  }

  private String makeGroupId(Price price){
    return price.getProductCode()
        + price.getDepart()
        + price.getNumber();
  }
}
