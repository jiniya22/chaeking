package com.chaeking.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class IndexController {

    private final ResourcePatternResolver resourcePatternResolver;

    @GetMapping("")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        try {
            List<String> apiDocNames = Arrays.stream(resourcePatternResolver.getResources("classpath:static/docs/*.html"))
                    .map(Resource::getFilename)
                    .filter(Strings::isNotEmpty).toList();
            mv.addObject("apiDocs", apiDocNames);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return mv;
    }

}
