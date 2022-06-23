package com.chaeking.api.domain.value;

import com.chaeking.api.domain.enumerate.AnalysisType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class AnalysisValue {

    @Getter
    public static class BookAnalysis {
        private AnalysisType type;
        private int totalAmount;
        private List<Content> contents = new ArrayList<>();

        public BookAnalysis(AnalysisType type) {
            this.type = type;
        }

        @Getter
        public static class Content {
            private String name;
            private int amount;

            private Content(String name, int amount) {
                this.name = name;
                this.amount = amount;
            }
        }

        public void addContent(String name, int amount) {
            this.contents.add(new Content(name, amount));
            this.totalAmount += amount;
        }
    }
}
