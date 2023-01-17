package com.chaeking.api.value;

public record TermsValue(
        Long termsLogId,
        String title,
        String url,
        String effectiveOn
) {
}
