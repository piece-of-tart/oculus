package ru.tinkoff.edu.java.parser.parsers;

import ru.tinkoff.edu.java.parser.values.StackOverflowValue;
import ru.tinkoff.edu.java.parser.values.Value;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StackOverflowParser extends Parser {
    private final static Pattern pattern = Pattern.compile("^/questions/([^/]+)/?.*$");
    public StackOverflowParser(Parser successor) {
        setSuccessor(successor);
    }

    @Override
    public Value parse(URL url) {
        if (canHandleUrl(url, "stackoverflow.com")) {
            Matcher matcher = pattern.matcher(url.getPath());
            return matcher.matches() ? new StackOverflowValue(matcher.group(1)) : null;
        } else {
            return super.parse(url);
        }
    }
}