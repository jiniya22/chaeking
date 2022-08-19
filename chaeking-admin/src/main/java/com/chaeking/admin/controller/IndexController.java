package com.chaeking.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {

	@GetMapping("")
	public String main(Model model) {
		model.addAttribute("currentPage", "home");
		return "content/main";
	}

	@GetMapping("/tags")
	public String tags(Model model) {
		model.addAttribute("currentPage", "tag");
		return "content/tags";
	}

}
