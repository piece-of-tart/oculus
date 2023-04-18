package ru.tinkoff.edu.java.scrapper.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

@Log4j2
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ChatController {
    private final TgChatService tgChatService;

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<?> registerChat(@PathVariable Long id) {
        if (tgChatService.exists(id)) {
            return ResponseEntity.badRequest().body("Chat with id " + id + "already have been registered.");
        }
        tgChatService.register(id);
        return ResponseEntity.ok("Chat with id " + id + " was successfully registered.");
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<? super Object> deleteChat(@PathVariable Long id) {
        if (tgChatService.exists(id)) {
            tgChatService.unregister(id);
            return ResponseEntity.ok("Chat with id " + id + " was deleted");
        }
        return ResponseEntity.badRequest().body(ExceptionApiHandler.getApiErrorResponse("The chat doesn't exist.", "404",
                new RuntimeException("Chat with id=" + id + " doesn't exist.")));
    }
}
