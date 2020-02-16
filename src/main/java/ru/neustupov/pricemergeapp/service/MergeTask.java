package ru.neustupov.pricemergeapp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.neustupov.pricemergeapp.model.Price;

@Data
@AllArgsConstructor
public class MergeTask implements Callable<List<Price>> {

  private Price newPrice;
  private Price oldPrice;

  @Override
  public List<Price> call() {

    LocalDateTime newPriceBegin = newPrice.getBegin();
    LocalDateTime newPriceEnd = newPrice.getEnd();
    LocalDateTime oldPriceBegin = oldPrice.getBegin();
    LocalDateTime oldPriceEnd = oldPrice.getEnd();

    List<Price> priceList = new ArrayList<>();

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
          oldPrice.setEnd(newPriceBegin);
          priceList.add(oldPrice);
          priceList.add(newPrice);
        }
      } else {
        // Случаи, когда цена совпадает
        if (newPriceBegin.isBefore(oldPriceBegin) && newPriceEnd.isAfter(oldPriceEnd)) {
          priceList.add(newPrice);
        } else if (newPriceEnd.isAfter(oldPriceEnd)) {
            oldPrice.setEnd(newPriceEnd);
            priceList.add(oldPrice);

        } else if (newPriceBegin.isBefore(oldPriceBegin)) {
          oldPrice.setBegin(newPriceBegin);
          priceList.add(oldPrice);
        }
      }
    }
    return priceList;
  }

  private Price createNewPrice(Price price, LocalDateTime  begin, LocalDateTime  end){
    return Price.builder().productCode(price.getProductCode())
        .number(price.getNumber())
        .depart(price.getDepart())
        .begin(begin)
        .end(end)
        .value(price.getValue())
        .build();
  }
}
