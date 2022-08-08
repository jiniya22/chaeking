package com.chaeking.api.util;

import org.junit.jupiter.api.Test;

public class RegexTest {

    @Test
    void test1() {
        String s1 = "<Penguin>   Random  House US  ";

        String s2 = s1.replaceAll("[^\\da-zA-Z가-힣 ]", "");
        String s3 = s2.replaceAll(" +", " ");
        System.out.println(s2);
        System.out.println(s3);
    }

    @Test
    void test2() {
        String ttt = """
                우리, \u003cspan class\u003d\"searching_txt\"\u003e헤어질\u003c/span\u003e 줄 몰랐지
                """;
        String ttt2 = """
                우리, <span class=\\"searching_txt\\">헤어질</span> 줄 몰랐지
                """;
        System.out.println(removeTag(ttt));
        System.out.println(removeTag(ttt2));
        System.out.println(removeTag("/NL/contents/search.do#viewKey\u003dCNTS-00099023813\u0026viewType\u003dC"));
    }

    String removeTag(String ori) {
        return ori.replaceAll("<[^>]*>", "");
    }
}
