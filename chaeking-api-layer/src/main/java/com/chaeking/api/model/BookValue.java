package com.chaeking.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public final class BookValue {

    public final static class Res {

        @Schema(name = "BookKakao")
        public record Kakao(
                String isbn,
                String name,
                String authors,
                String publisher,
                String imageUrl
        ) { }

        @Schema(name = "BookSimple")
        public record Simple(
                long id,
                String name,
                String authors,
                String publisher,
                String imageUrl
        ) { }

        @Schema(name = "BookDetail")
        public record Detail(
                long id,
                String name,
                int price,
                String publisher,
                String publicationDate,
                String isbn,
                String imageUrl,
                String detailInfo,
                List<String> authors,
                BookMemoryCompleteValue.Res.Content bookMemoryComplete,
                BookMemoryWishValue.Res.Content bookMemoryWish
        ) { }

    }
}
