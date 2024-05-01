package com.web.apteka.controller;

import com.web.apteka.model.AccountDTO;
import com.web.apteka.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class ViewController {
    private final AccountService accountService;
    @Autowired
    public ViewController(AccountService accountService) {
        this.accountService = accountService;
    }
    @GetMapping("/catalog_pages")
    public String catalogp(Model model,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "15") int size)
    {
        model.addAttribute("page",page);
        model.addAttribute("size",size);
        return "catalog";
    }
    @GetMapping("/catalog")
    public String catalog()
    {
        return "catalog";
    }
    @GetMapping("/index")
    public String index()
    {
        return "index";
    }
    @GetMapping("/profile")
    public String profile()
    {
        return "profile";
    }
}
