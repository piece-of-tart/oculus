package ru.tinkoff.edu.java.scrapper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.dto.jdbc.LinkEntity;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@RestController
@RequestMapping("/links")
public class LinkController {
    private final LinkService linkService;

    @Autowired
    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping
    public ResponseEntity<? super Object> getTrackedLink(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        Collection<LinkEntity> linkEntities = linkService.listAll(tgChatId);
        return ResponseEntity.ok(Objects.requireNonNullElse(linkEntities, Collections.EMPTY_LIST));
    }

    @PostMapping
    public ResponseEntity<? super LinkResponse> addTrackedLink(@RequestHeader("Tg-Chat-Id") Long tgChatId,
                                                               @RequestBody AddLinkRequest addLinkRequest) {
        final LinkEntity linkEntity = new LinkEntity(addLinkRequest.uri(), tgChatId, addLinkRequest.description(),
                0L, null);
        if (linkService.getLink(tgChatId, linkEntity.uri()) != null) {
            return ResponseEntity.ok("This link is tracked by chat with id " + tgChatId + " yet.");
        }
        final LinkEntity retLinkEntity = linkService.add(linkEntity);
        return ResponseEntity.ok(new LinkResponse(retLinkEntity.chatId(), retLinkEntity.uri()));
    }

    @DeleteMapping
    public ResponseEntity<? super LinkResponse> deleteTrackedLink(@RequestHeader("Tg-Chat-Id") Long tgChatId,
                                                                  @RequestBody RemoveLinkRequest removeLinkRequest) {
        final LinkEntity linkEntity = linkService.remove(tgChatId, removeLinkRequest.uri());
        if (linkEntity == null) {
            final String message = "Link " + removeLinkRequest.uri() + " doesn't exist.";
            return ResponseEntity.badRequest().body(
                    ExceptionApiHandler.getApiErrorResponse(message, "404", new RuntimeException(message)));
        }
        return ResponseEntity.ok(new LinkResponse(linkEntity.chatId(), linkEntity.uri()));
    }
}
