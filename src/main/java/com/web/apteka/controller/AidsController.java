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
                                                        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AidDTO> aidsPage = aidService.getActiveAids(pageable);

        if (aidsPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(aidsPage.getContent());
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
//    @PutMapping("/update/{id}")
//    public ResponseEntity<AidDTO> updateAid(@PathVariable Integer id, @RequestBody AidDTO newData)
//    {
//        if(!aidService.getAidById(id).isEmpty()) {
//            return ResponseEntity.status(HttpStatus.OK).body(aidService.updateAid(id,newData));
//        }
//        else return ResponseEntity.notFound().build();
//    }
    @GetMapping("/count")
    public ResponseEntity<Integer> getAidsCount() {
        Integer count = aidService.getAidsCount();
        return ResponseEntity.ok(count);
    }
}
