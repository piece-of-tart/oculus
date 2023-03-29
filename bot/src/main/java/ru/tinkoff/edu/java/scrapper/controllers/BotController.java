package ru.tinkoff.edu.java.scrapper.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class BotController {

    @PostMapping("/update")
    public ResponseEntity<String> updateLink(@Validated @RequestBody LinkUpdate linkUpdate) {
        return ResponseEntity.ok("Ok:   " + linkUpdate);
    }
}
