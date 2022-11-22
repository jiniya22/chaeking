package com.chaeking.api.controller.w;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/w/privacy")
public class WPrivacyController {

    @GetMapping(value = "/1")
    public ModelAndView privacy() {
        ModelAndView view = new ModelAndView("privacy_1");
        return view;
    }

}
