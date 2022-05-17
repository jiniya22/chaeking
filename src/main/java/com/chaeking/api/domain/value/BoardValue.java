package com.chaeking.api.domain.value;

public class BoardValue {

    public static class Res {
        public record Simple(long id, String title, String content) {}
    }
}
