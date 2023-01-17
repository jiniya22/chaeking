package com.chaeking.api.domain.repository;

import com.chaeking.api.domain.entity.BestSeller;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BestSellerRepository extends JpaRepository<BestSeller, Long> {

    @EntityGraph(attributePaths = {"book", "book.publisher"})
    List<BestSeller> findTop10WithBookAndPublisherByOrderById();
    @EntityGraph(attributePaths = {"book", "book.publisher"})
    List<BestSeller> findTop3WithBookAndPublisherByOrderById();
}
