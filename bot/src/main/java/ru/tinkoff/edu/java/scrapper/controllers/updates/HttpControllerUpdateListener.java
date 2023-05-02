package ru.tinkoff.edu.java.scrapper.controllers.updates;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
public class HttpControllerUpdateListener {
    @Autowired
    private UpdateListener updateListener;

    @PostMapping("/update")
    public ResponseEntity<?> updateLink(@Validated @RequestBody LinkUpdate linkUpdate) {
        log.info(getClass().toString() + " - got link with updates: " + linkUpdate);
        updateListener.sendUpdatesToUser(linkUpdate);
        return ResponseEntity.ok().build();
    }
}
