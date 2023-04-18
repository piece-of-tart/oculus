package ru.tinkoff.edu.java.scrapper.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ScrapperController {
    final private Map<Long, List<LinkResponse>> observingLinks = new HashMap<>();

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<String> registerChat(@PathVariable Long id) {
        boolean wasRegistered = observingLinks.putIfAbsent(id, new ArrayList<>()) != null;
        return wasRegistered ? ResponseEntity.ok("User with id=" + id + " was registered before.") :
            ResponseEntity.ok("Chat for id " + id + " registered.");
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<? super Object> deleteChat(@PathVariable Long id) {
        if (observingLinks.remove(id) != null) {
            return ResponseEntity.ok("Chat " + id + " deleted.");
        }
        return ResponseEntity.badRequest().body(ExceptionApiHandler.getApiErrorResponse("The chat doesn't exist.", "404",
                new RuntimeException("Chat with id=" + id + " doesn't exist.")));
    }

    @GetMapping("/links")
    public ListLinksResponse getObservingLinks(@RequestHeader("Tg-Chat-Id") Long id) {
        if (observingLinks.get(id) == null) {
            log.error("User with id=" + id + " passed away registration and request list of tracked links from scrapper.");
            observingLinks.put(id, new ArrayList<>());
        }
        return new ListLinksResponse(observingLinks.get(id), observingLinks.get(id).size());
    }

    @PostMapping("/links")
    public LinkResponse addObservingLink(@RequestHeader("Tg-Chat-Id") Long id, @RequestBody AddLinkRequest addLinkRequest) {
        final LinkResponse linkResponse = new LinkResponse(id, addLinkRequest.uri());
        observingLinks.putIfAbsent(id, new ArrayList<>());
        observingLinks.get(id).add(linkResponse);
        return linkResponse;
    }

    @DeleteMapping("/links")
    public Object deleteObservingLink(@RequestHeader("Tg-Chat-Id") Long id, @RequestBody RemoveLinkRequest removeLinkRequest) {
        if (!observingLinks.containsKey(id) || !observingLinks.get(id).remove(new LinkResponse(id, removeLinkRequest.uri()))) {
            return ResponseEntity.badRequest().body(
                    ExceptionApiHandler.getApiErrorResponse("Link " + removeLinkRequest.uri() + " doesn't exist.", "404",
                    new RuntimeException("Link " + removeLinkRequest.uri() + " doesn't exist.")));
        }
        return new LinkResponse(id, removeLinkRequest.uri());
    }
}
