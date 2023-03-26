package ru.tinkoff.edu.java.parser.parsers;

import ru.tinkoff.edu.java.parser.values.Value;

import java.net.URL;

public abstract class AbstractParser implements Parser {
    private final Parser next;

    public AbstractParser(Parser next) {
        this.next = next;
    }

    protected boolean canHandleUrl(URL url, String host) {
        return url.getHost().equals(host);
    }

    protected Value parseNext(URL url) {
        return next != null ? next.parse(url) : null;
    }
}
