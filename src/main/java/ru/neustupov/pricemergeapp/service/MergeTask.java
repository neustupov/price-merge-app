package ru.neustupov.pricemergeapp.service;

import java.util.ArrayList;
import java.util.Date;
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

    Date newPriceBeginDate = newPrice.getBegin();
    Date newPriceEndDate = newPrice.getEnd();
    Date oldPriceBeginDate = oldPrice.getBegin();
    Date oldPriceEndDate = oldPrice.getEnd();

    List<Price> priceList = new ArrayList<>();

    if (checkDates(newPriceBeginDate, newPriceEndDate, oldPriceBeginDate, oldPriceEndDate)){
      if (newPrice.getValue().equals(oldPrice.getValue())){
        oldPrice.setEnd(newPrice.getEnd());
        priceList.add(oldPrice);
      } else {
        if (newPriceEndDate.before(oldPriceEndDate)){
          Price createdPrice1 =  Price.builder().productCode(newPrice.getProductCode())
              .number(newPrice.getNumber())
              .depart(newPrice.getDepart())
              .begin(newPriceEndDate)
              .end(oldPriceEndDate)
              .value(newPrice.getValue())
              .build();
          priceList.add(createdPrice1);
        }
        Price createdPrice =  Price.builder().productCode(newPrice.getProductCode())
            .number(newPrice.getNumber())
            .depart(newPrice.getDepart())
            .begin(newPrice.getBegin())
            .end(newPrice.getEnd())
            .value(newPrice.getValue())
            .build();
        oldPrice.setEnd(newPriceBeginDate);
        priceList.add(createdPrice);
        priceList.add(oldPrice);
      }
    } else {
      priceList.add(newPrice);
    }
    return priceList;
  }

  private boolean checkDates(Date newPriceBeginDate, Date newPriceEndDate,
                             Date oldPriceBeginDate, Date oldPriceEndDate ){
    if (newPriceBeginDate.after(oldPriceBeginDate) | newPriceBeginDate.before(oldPriceBeginDate) |
        newPriceEndDate.before(oldPriceEndDate) | newPriceEndDate.after(oldPriceEndDate)){
      return true;
    }
    return false;
  }
}
