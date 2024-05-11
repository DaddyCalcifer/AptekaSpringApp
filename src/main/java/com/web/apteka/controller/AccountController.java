package com.web.apteka.controller;

import com.web.apteka.model.AccountDTO;
import com.web.apteka.service.AccountService;
import com.web.apteka.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class AccountController {
    private final AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getUserById(@PathVariable UUID id) {
        Optional<AccountDTO> userOptional = accountService.getUserById(id);
        return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/data")
    public ResponseEntity<AccountDTO> getUserByJWT(@RequestParam String jwt) {
        if(JwtService.validateToken(jwt)) {
            var id = JwtService.getUserIdFromToken(jwt);
            Optional<AccountDTO> userOptional = accountService.getUserById(id);
            return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } else
            return new ResponseEntity<>(HttpStatus.LOCKED);
    }
    //Добавил пагинацию для оптимизации памяти при большом количестве записей в бд [14.04]
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AccountDTO> usersPage = accountService.getAllUsers(pageable);

        if (usersPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(usersPage.getContent());
        }
    }
    @PostMapping("/add")
    public ResponseEntity<AccountDTO> createUser(@Valid @RequestBody AccountDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации
            return ResponseEntity.badRequest().build();
        }

        AccountDTO createdUser = accountService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    @PostMapping("/auth")
    public ResponseEntity<String> doAuth(@Valid @RequestBody AccountDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации
            return ResponseEntity.badRequest().build();
        }

        String result = accountService.doAuth(request.getEmail(), request.getPasswordHash());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }
    @PatchMapping("/delete/{id}")
    public ResponseEntity<AccountDTO> deleteUserById(@PathVariable UUID id) {
        if(accountService.deleteUser(id)) {
            return ResponseEntity.status(HttpStatus.OK).body(accountService.getUserById(id).get());
        }
        else return ResponseEntity.notFound().build();
    }
    @PatchMapping("/recover/{id}")
    public ResponseEntity<AccountDTO> recoverUserById(@PathVariable UUID id) {
        if(accountService.recoverUser(id)) {
            return ResponseEntity.status(HttpStatus.OK).body(accountService.getUserById(id).get());
        }
        else return ResponseEntity.notFound().build();
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<AccountDTO> updateUser(@PathVariable UUID id, @RequestBody AccountDTO newData)
    {
        if(!accountService.getUserById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(accountService.updateUser(id,newData));
        }
        else return ResponseEntity.notFound().build();
    }
    @GetMapping("/count")
    public ResponseEntity<Integer> getUsersCount() {
        Integer count = accountService.getUsersCount();
        return ResponseEntity.ok(count);
    }
    @GetMapping("/id")
    public ResponseEntity<UUID> getIdWithJWT(@RequestParam String jwt)
    {
        if(JwtService.validateToken(jwt))
            return ResponseEntity.ok(JwtService.getUserIdFromToken(jwt));
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}