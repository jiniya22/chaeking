package com.chaeking.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
public class IndexController {

    @GetMapping("")
    public ModelAndView index() throws IOException {
        ModelAndView mv = new ModelAndView("index");
        List<String> apiDocNames = getApiDocs();
        mv.addObject("apiDocs", apiDocNames);
        return mv;
    }

    private List<String> getApiDocs() {
        try {
            InputStream inputStream = new ClassPathResource("static/docs").getInputStream();
            String files = new String(inputStream.readAllBytes());
            return Arrays.asList(files.split("\n"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

}
