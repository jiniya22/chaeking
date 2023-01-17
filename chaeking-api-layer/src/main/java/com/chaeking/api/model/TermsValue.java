package com.chaeking.api.model;

public record TermsValue(
        Long termsLogId,
        String title,
        String url,
        String effectiveOn
) {
}
