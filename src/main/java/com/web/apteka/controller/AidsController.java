package com.web.apteka.controller;

import com.web.apteka.model.AccountDTO;
import com.web.apteka.model.AidDTO;
import com.web.apteka.service.AccountService;
import com.web.apteka.service.AidService;
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
@RequestMapping("/api/aids")
public class AidsController {
    private final AidService aidService;
    @Autowired
    public AidsController(AidService aidService) {
        this.aidService = aidService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AidDTO> getAidById(@PathVariable Integer id) {
        Optional<AidDTO> aidOptional = aidService.getAidById(id);
        return aidOptional.map(aid -> new ResponseEntity<>(aid, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    //Добавил пагинацию для оптимизации памяти при большом количестве записей в бд [14.04]
    @GetMapping
    public ResponseEntity<List<AidDTO>> getAllAids(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "20") int size,
                                                   @RequestParam(defaultValue = "") String search,
                                                   @RequestParam(defaultValue = "0") double minP,
                                                   @RequestParam(defaultValue = "999999") double maxP) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AidDTO> aidsPage;

        if (!search.isEmpty()) {
            if((minP == 0) && (maxP == 0))
                aidsPage = aidService.findAids(pageable, search);
            else
                aidsPage = aidService.searchViaPrice(pageable,search,minP,maxP);
        } else {
            // Иначе получаем все лекарства
            if((minP == 0) && (maxP == 0))
                aidsPage = aidService.getActiveAids(pageable);
            else
                aidsPage = aidService.searchViaPrice(pageable,"",minP,maxP);
        }

        if (aidsPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(aidsPage.getContent());
        }
    }
    @GetMapping("/sale")
    public ResponseEntity<List<AidDTO>> getSales() {
        if (aidService.getSales().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(aidService.getSales());
        }
    }
    @PostMapping("/add")
    public ResponseEntity<AidDTO> createAid(@Valid @RequestBody AidDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации
            return ResponseEntity.badRequest().build();
        }

        AidDTO createdAid = aidService.createAid(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAid);
    }
    @PatchMapping("/delete/{id}")
    public ResponseEntity<AidDTO> deleteAidById(@PathVariable Integer id) {
        if(aidService.deleteAid(id)) {
            return ResponseEntity.status(HttpStatus.OK).body(aidService.getAidById(id).get());
        }
        else return ResponseEntity.notFound().build();
    }
    @PatchMapping("/recover/{id}")
    public ResponseEntity<AidDTO> recoverAidById(@PathVariable Integer id) {
        if(aidService.recoverAid(id)) {
            return ResponseEntity.status(HttpStatus.OK).body(aidService.getAidById(id).get());
        }
        else return ResponseEntity.notFound().build();
    }
    @GetMapping("/count")
    public ResponseEntity<Integer> getAidsCount(@RequestParam(defaultValue = "") String search,
                                                @RequestParam(defaultValue = "0") Integer minP,
                                                @RequestParam(defaultValue = "0") Integer maxP) {
        Integer count;
        if(!search.isEmpty())
            if(minP == 0 && maxP == 0)
                count = aidService.searchAidsCount(search);
            else
                count = aidService.searchViaPriceCount(search,minP,maxP);
        else
        if(minP == 0 && maxP == 0)
            count = aidService.getAidsCount();
        else
            count = aidService.searchViaPriceCount(search,minP,maxP);
        return ResponseEntity.ok(count);
    }
}
