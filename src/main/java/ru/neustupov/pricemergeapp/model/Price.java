package ru.neustupov.pricemergeapp.model;

import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Data
public class Price {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String productCode;
  private int number;
  private int depart;
  private Date begin;
  private Date end;
  private Long value;

}
