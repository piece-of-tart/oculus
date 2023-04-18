package ru.tinkoff.edu.java.scrapper.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.dto.LinkEntity;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.util.Collection;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkController {
    private final LinkService linkService;

    @GetMapping
    public ResponseEntity<? super Object> getTrackedLink(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        Collection<LinkEntity> linkEntities = linkService.listAll(tgChatId);
        if (linkEntities == null) {
            return ResponseEntity.badRequest().body("There is no links for chat with id " + tgChatId);
        }
        return ResponseEntity.ok(linkEntities);
    }

    @PostMapping("/links")
    public ResponseEntity<? super LinkResponse> addTrackedLink(@RequestHeader("Tg-Chat-Id") Long tgChatId, @RequestBody AddLinkRequest addLinkRequest) {
        final LinkEntity linkEntity = new LinkEntity(addLinkRequest.uri(), tgChatId, addLinkRequest.description(), 0L, null);
        if (linkService.get(tgChatId, linkEntity.uri()) != null) {
            return ResponseEntity.badRequest().body("This link is tracked by chat with id " + tgChatId + " yet.");
        }
        final LinkEntity retLinkEntity = linkService.add(linkEntity);
        return ResponseEntity.ok(new LinkResponse(retLinkEntity.chatId(), retLinkEntity.uri()));
    }

    @DeleteMapping("/links")
    public ResponseEntity<? super LinkResponse> deleteTrackedLink(@RequestHeader("Tg-Chat-Id") Long tgChatId, @RequestBody RemoveLinkRequest removeLinkRequest) {
        final LinkEntity linkEntity = linkService.remove(tgChatId, removeLinkRequest.uri());
        if (linkEntity == null) {
            return ResponseEntity.badRequest().body(
                    ExceptionApiHandler.getApiErrorResponse("Link " + removeLinkRequest.uri() + " doesn't exist.", "404",
                            new RuntimeException("Link " + removeLinkRequest.uri() + " doesn't exist.")));
        }
        return ResponseEntity.ok(new LinkResponse(linkEntity.chatId(), linkEntity.uri()));
    }
}
