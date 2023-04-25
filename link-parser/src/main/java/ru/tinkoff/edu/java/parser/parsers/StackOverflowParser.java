package ru.tinkoff.edu.java.parser.parsers;

import ru.tinkoff.edu.java.parser.values.StackOverflowValue;
import ru.tinkoff.edu.java.parser.values.Value;

import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StackOverflowParser extends AbstractParser {
    private final static Pattern pattern = Pattern.compile("^/questions/([^/]+)/?.*$");

    public StackOverflowParser(Parser next) {
        super(next);
    }

    @Override
    public Value parse(URI uri) {
        if (canHandleUrl(uri, "stackoverflow.com")) {
            Matcher matcher = pattern.matcher(uri.getPath());
            return matcher.matches() ? new StackOverflowValue(matcher.group(1)) : null;
        }
        return parseNext(uri);
    }
}