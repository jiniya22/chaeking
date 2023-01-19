package com.chaeking.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
public class IndexController {

    @GetMapping("")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        List<String> apiDocNames = getApiDocs();
        mv.addObject("apiDocs", apiDocNames);
        return mv;
    }

    private List<String> getApiDocs() {
        try {
            File docsDirectory = ResourceUtils.getFile("classpath:static/docs");
            File[] docs = docsDirectory.listFiles();
            if (docs != null) {
                List<String> fileNames = Arrays.stream(docs)
                        .map(File::getName)
                        .filter(name -> name.endsWith(".html")).toList();
                System.out.println(fileNames);
                return fileNames;
            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

}
