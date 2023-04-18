package ru.tinkoff.edu.java.parser.parsers;

import ru.tinkoff.edu.java.parser.values.Value;

import java.net.URI;
import java.net.URL;

public abstract class AbstractParser implements Parser {
    private final Parser next;

    public AbstractParser(Parser next) {
        this.next = next;
    }

    protected boolean canHandleUrl(URI uri, String host) {
        return uri.getHost().equals(host);
    }

    protected Value parseNext(URI uri) {
        return next != null ? next.parse(uri) : null;
    }
}
