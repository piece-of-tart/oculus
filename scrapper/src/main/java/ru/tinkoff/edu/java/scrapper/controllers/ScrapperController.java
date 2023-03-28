package ru.tinkoff.edu.java.scrapper.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.RemoveLinkRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ScrapperController {
    final private Map<Long, List<LinkResponse>> observingLinks = new HashMap<>();

    final private Set<Long> chats = new HashSet<>();

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<String> registerChat(@PathVariable Long id) {
        chats.add(id);
        return ResponseEntity.ok("Chat for id " + id + " registered.");
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<? super Object> deleteChat(@PathVariable Long id) {
        if (chats.remove(id)) {
            return ResponseEntity.ok("Chat " + id + " deleted.");
        }
        return ResponseEntity.badRequest().body(ExceptionApiHandler.getApiErrorResponse("The chat doesn't exist.", "404",
                new RuntimeException("Chat with id=" + id + " doesn't exist.")));
    }

    @GetMapping("/links")
    public ListLinksResponse getObservingLinks(@RequestHeader("Tg-Chat-Id") Long id) {
        observingLinks.putIfAbsent(id, new ArrayList<>());
        return new ListLinksResponse(observingLinks.get(id), observingLinks.get(id).size());
    }

    @PostMapping("/links")
    public LinkResponse addObservingLink(@RequestHeader("Tg-Chat-Id") Long id, @RequestBody AddLinkRequest addLinkRequest) {
        final LinkResponse linkResponse = new LinkResponse(id, addLinkRequest.url());
        observingLinks.putIfAbsent(id, new ArrayList<>());
        observingLinks.get(id).add(linkResponse);
        return linkResponse;
    }

    @DeleteMapping("/links")
    public Object deleteObservingLink(@RequestHeader("Tg-Chat-Id") Long id, @RequestBody RemoveLinkRequest removeLinkRequest) {
        if (!observingLinks.containsKey(id) || !observingLinks.get(id).remove(new LinkResponse(id, removeLinkRequest.url()))) {
            return ResponseEntity.badRequest().body(
                    ExceptionApiHandler.getApiErrorResponse("Link " + removeLinkRequest.url() + " doesn't exist.", "404",
                    new RuntimeException("Link " + removeLinkRequest.url() + " doesn't exist.")));
        }
        return new LinkResponse(id, removeLinkRequest.url());
    }
}
