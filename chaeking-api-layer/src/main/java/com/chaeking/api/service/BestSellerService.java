package com.chaeking.api.service;

import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.value.BookValue;
import com.chaeking.api.domain.repository.BestSellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BestSellerService {
    private final BestSellerRepository bestSellerRepository;

    public List<BookValue.Res.Simple> bestSellers() {
        return bestSellerRepository.findTop10WithBookAndPublisherByOrderById().stream().map(bestSeller -> {
            bestSeller.getBook().getBookAndAuthors().forEach(bookAndAuthor -> bookAndAuthor.getAuthor().getName());
            return Book.createSimple(bestSeller.getBook());
        }).collect(Collectors.toList());
    }

}
