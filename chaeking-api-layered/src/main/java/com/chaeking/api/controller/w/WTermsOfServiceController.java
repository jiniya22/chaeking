package com.chaeking.api.controller.w;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/w/terms_of_service")
public class WTermsOfServiceController {

    @GetMapping(value = "/1")
    public ModelAndView termsOfService1() {
        ModelAndView view = new ModelAndView("terms_of_service_1");
        return view;
    }

}
