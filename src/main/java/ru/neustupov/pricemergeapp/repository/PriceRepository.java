package ru.neustupov.pricemergeapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neustupov.pricemergeapp.model.Price;

public interface PriceRepository extends JpaRepository<Price, Long> {

}
