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

  /**
   * Достаёт из репозитория список цен
   */
  public List<Price> getAllPrices(){
    return priceRepository.findAll();
  }

  /**
   * Преобразует список старых цен на сонове списка новых цен
   *
   * @param newPrices Список новых цен
   */
  public List<Price> mergePrices(List<Price> newPrices) {
    List<Price> oldPrices = getAllPrices();
    return merge(newPrices, oldPrices);
  }

  /**
   * Основной метод преобразования списка старых цен на основе списка новых цен,
   * выбрана итерация по новым ценам, поскольку логично предположить, что новых цен
   * должно быть меньше, чем старых.
   *
   * Обработка производится в отдельных потоках, количество потоков выбирается исходя из
   * параметров системы.
   *
   * @param newPrices Цена
   * @param oldPrices Начало
   */
  private List<Price> merge(List<Price> newPrices, List<Price> oldPrices) {
    ThreadPoolExecutor executor =
        (ThreadPoolExecutor) Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    List<Price> totalPrices = new ArrayList<>();
    List<Future<List<Price>>> futures = new ArrayList<>();
    Map<String, List<Price>> compOldPriceMap = oldPrices.stream().collect(Collectors.groupingBy(
        this::makeGroupId));
    Map<String, List<Price>> compNewPriceMap = newPrices.stream().collect(Collectors.groupingBy(
        this::makeGroupId));

    for (Price newPrice : newPrices){
      List<Price> concurOldPrices = compOldPriceMap.get(makeGroupId(newPrice));
      List<Price> concurNewPrices = new ArrayList<>(compNewPriceMap.get(makeGroupId(newPrice)));
      concurNewPrices.remove(newPrice);
      // Если совпадений не найдено, просто добавляем новую цену в список без обработки
      if (concurOldPrices.isEmpty()){
        totalPrices.add(newPrice);
      } else {
          futures.add(executor.submit(new MergeTask(newPrice, concurOldPrices, concurNewPrices)));
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

  /**
   * Создаёт идентификатор цены на основе кода, отдела и номера цены
   *
   * @param price Цена
   */
  private String makeGroupId(Price price){
    return price.getProductCode()
        + price.getDepart()
        + price.getNumber();
  }
}
