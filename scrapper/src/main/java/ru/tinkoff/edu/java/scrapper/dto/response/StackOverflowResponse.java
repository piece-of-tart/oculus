package ru.tinkoff.edu.java.scrapper.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class StackOverflowResponse {
    @JsonProperty("items")
    private List<LastActivityDate> items;

    @Getter
    @Setter
    @ToString
    public static class LastActivityDate {
        @JsonProperty("last_activity_date")
        private OffsetDateTime lastActivityDate;
    }
}
