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
    @GetMapping("/users/{id}")
    public String userId(Model model,@PathVariable UUID id) {
        AccountDTO user = accountService.getUserById(id).get();
        if(user.isDeleted()) {
            return "blocked";
        } else {
            model.addAttribute("name", user.getName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("createdAt", user.getCreatedAt().toString().replace("T", "\t"));
            model.addAttribute("updatedAt", user.getUpdatedAt().toString().replace("T", "\t"));
            return "user";
        }
    }
    @GetMapping("/users/{id}/edit")
    public String userEdit(Model model,@PathVariable UUID id) {
        AccountDTO user = accountService.getUserById(id).get();
        if(user.isDeleted()) {
            return "blocked";
        } else {
            model.addAttribute("name", user.getName());
            model.addAttribute("email", user.getEmail());
            return "user_edit";
        }
    }
    @GetMapping("/users")
    public String userId(Model model,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "15") int size)
    {
        model.addAttribute("page",page);
        model.addAttribute("size",size);
        return "users";
    }
}
