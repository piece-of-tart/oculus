package ru.tinkoff.edu.java.parser.parsers;

import ru.tinkoff.edu.java.parser.values.GithubValue;
import ru.tinkoff.edu.java.parser.values.Value;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GithubParser extends AbstractParser {
    private final static Pattern PATTERN = Pattern.compile("^/([^/]+)/([^/]+)/?.*$");

    public GithubParser(Parser next) {
        super(next);
    }

    @Override
    public Value parse(URI uri) {
        if (canHandleUrl(uri, "github.com")) {
            Matcher matcher = PATTERN.matcher(uri.getPath());
            if (matcher.matches() && !matcher.group(1).equals("marketplace") && !matcher.group(1).equals("sponsors")) {
                return new GithubValue(matcher.group(1), matcher.group(2));
            }
            return null;
        }
        return parseNext(uri);
    }
}
