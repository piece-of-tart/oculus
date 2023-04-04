package ru.tinkoff.edu.java.scrapper.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;

@Log4j2
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class BotController {
    @PostMapping("/update")
    public ResponseEntity<String> updateLink(@Validated @RequestBody LinkUpdate linkUpdate) {
        log.info("BotController class method updateLink");
        return ResponseEntity.ok("Ok:   " + linkUpdate);
    }
}
