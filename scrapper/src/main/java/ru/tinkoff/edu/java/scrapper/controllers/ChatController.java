package ru.tinkoff.edu.java.scrapper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

@RestController
@RequestMapping("/")
public class ChatController {
    private final TgChatService tgChatService;

    private static final String CHAT_ID_INTRO = "Chat with id=";

    @Autowired
    public ChatController(TgChatService tgChatService) {
        this.tgChatService = tgChatService;
    }

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<?> registerChat(@PathVariable Long id) {
        if (tgChatService.exists(id)) {
            return ResponseEntity.badRequest().body(CHAT_ID_INTRO + id + "already have been registered.");
        }
        tgChatService.register(id);
        return ResponseEntity.ok("was successfully registered.");
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<? super Object> deleteChat(@PathVariable Long id) {
        if (tgChatService.exists(id)) {
            tgChatService.unregister(id);
            return ResponseEntity.ok(CHAT_ID_INTRO + id + " was deleted");
        }
        return ResponseEntity.badRequest().body(ExceptionApiHandler.getApiErrorResponse(
                "The chat doesn't exist.", "404",
                new RuntimeException(CHAT_ID_INTRO + id + " doesn't exist.")));
    }
}
