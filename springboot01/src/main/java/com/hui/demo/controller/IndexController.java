package com.hui.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.Locale;

@Controller
public class IndexController {



    @GetMapping("/index")
    public String Index(ModelMap modelMap) {
        modelMap.addAttribute("msg",
                "Hello Dalaoyang ！");
        return "index";
    }

    @Autowired
    private MessageSource messageSource;
    @GetMapping("/i18")
    public String i18(ModelMap modelMap) {
        Locale locale = LocaleContextHolder.getLocale();
        modelMap.addAttribute("msg",
                messageSource.getMessage("message",null,locale));
        return "index";
    }

}
