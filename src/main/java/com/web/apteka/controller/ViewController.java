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
    @GetMapping("/aids")
    public String aidPage()
    {
        return "aid_page";
    }
    @GetMapping("/auth")
    public String authPage()
    {
        return "auth";
    }
    @GetMapping("/cart")
    public String cartPage()
    {
        return "cart";
    }
    @GetMapping("/favorite")
    public String favPage()
    {
        return "favorite";
    }
    @GetMapping("/history")
    public String historyPage()
    {
        return "history";
    }
    @GetMapping("/about")
    public String aboutPage()
    {
        return "about";
    }
}
