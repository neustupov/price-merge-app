package ru.neustupov.pricemergeapp.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"product_code", "price_number", "depart"})})
public class Price {

  @Id
  @GeneratedValue
  private Long id;
  @Column(name = "product_code")
  private String productCode;
  @Column(name = "price_number")
  private int number;
  private int depart;
  @Column(name = "price_begin")
  private Date begin;
  @Column(name = "price_end")
  private Date end;
  @Column(name = "price_value")
  private Long value;

}
