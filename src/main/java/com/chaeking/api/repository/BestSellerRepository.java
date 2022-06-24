package com.chaeking.api.repository;

import com.chaeking.api.domain.entity.BestSeller;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BestSellerRepository extends JpaRepository<BestSeller, Long> {

    @EntityGraph(attributePaths = "book")
    List<BestSeller> findTop10ByOrderById();
}
