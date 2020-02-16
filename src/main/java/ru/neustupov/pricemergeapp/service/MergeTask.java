package ru.neustupov.pricemergeapp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.neustupov.pricemergeapp.model.Price;

/**
 * На основе новой цены и списков старых и новых цен создаёт список преобразованых цен, выполняется
 * в отдельном потоке
 */
@Data
@AllArgsConstructor
public class MergeTask implements Callable<List<Price>> {

  private Price newPrice;
  private List<Price> oldPrices;
  private List<Price> newPrices;

  @Override
  public List<Price> call() {

    List<Price> priceList = new ArrayList<>();

    for (Price oldPrice : oldPrices) {
      LocalDateTime newPriceBegin = newPrice.getBegin();
      LocalDateTime newPriceEnd = newPrice.getEnd();
      LocalDateTime oldPriceBegin = oldPrice.getBegin();
      LocalDateTime oldPriceEnd = oldPrice.getEnd();

      if (newPriceEnd.isAfter(oldPriceBegin) | newPriceBegin.isBefore(oldPriceEnd)) {
        if (!newPrice.getValue().equals(oldPrice.getValue())) {
          // Случай, когда новая цена полностью в рамках старой
          if (newPriceBegin.isAfter(oldPriceBegin) && newPriceEnd.isBefore(oldPriceEnd)) {
            priceList.add(createNewPrice(oldPrice, oldPriceBegin, newPriceBegin));
            priceList.add(createNewPrice(oldPrice, newPriceEnd, oldPriceEnd));
            priceList.add(newPrice);
          }

          // Случай, когда новая цена начинается раньше, и заканчивается раньше
          if (newPriceBegin.isBefore(oldPriceBegin) && newPriceEnd.isBefore(oldPriceEnd)
              && newPriceEnd.isAfter(oldPriceBegin)) {
            oldPrice.setBegin(newPriceEnd);
            priceList.add(oldPrice);
            priceList.add(newPrice);
          }

          // Случай, когда новая цена начинается и заканчивается позже
          if (newPriceBegin.isAfter(oldPriceBegin) && newPriceBegin.isBefore(oldPriceEnd) &&
              newPriceEnd.isAfter(oldPriceEnd)) {
            if (!checkHit(oldPrice, newPrices)) {
              oldPrice.setEnd(newPriceBegin);
              priceList.add(oldPrice);
            }
            priceList.add(newPrice);
          }
        } else {
          // Случаи, когда цена совпадает
          if (newPriceBegin.isBefore(oldPriceBegin) && newPriceEnd.isAfter(oldPriceEnd)) {
            priceList.add(newPrice);
          } else if (newPriceBegin.isAfter(oldPriceBegin) && newPriceEnd.isAfter(oldPriceEnd)) {
            oldPrice.setEnd(newPriceEnd);
            priceList.add(oldPrice);
            break;
          } else if (newPriceBegin.isBefore(oldPriceBegin)) {
            oldPrice.setBegin(newPriceBegin);
            priceList.add(oldPrice);
            break;
          }
        }
      }
    }
    return priceList;
  }

  /**
   * Создаёт новую цену
   *
   * @param price Цена
   * @param begin Начало
   * @param end Конец
   */
  private Price createNewPrice(Price price, LocalDateTime begin, LocalDateTime end) {
    return Price.builder().productCode(price.getProductCode())
        .number(price.getNumber())
        .depart(price.getDepart())
        .begin(begin)
        .end(end)
        .value(price.getValue())
        .build();
  }

  /**
   * Дополнительная проверка на вхождение начала старой цены в период действия других новых цен
   *
   * @param oldPrice Параметры запроса
   * @param newPrices Список новых цен
   */
  private boolean checkHit(Price oldPrice, List<Price> newPrices) {
    boolean expected = false;
    for (Price newPrice : newPrices) {
      if (newPrice.getEnd().isAfter(oldPrice.getBegin())) {
        expected = true;
      }
    }
    return expected;
  }
}
