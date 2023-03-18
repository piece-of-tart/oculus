package ru.tinkoff.edu.java.parser.parsers;

import ru.tinkoff.edu.java.parser.values.GithubValue;
import ru.tinkoff.edu.java.parser.values.Value;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GithubParser extends Parser {
    private final static Pattern pattern = Pattern.compile("^/([^/]+)/([^/]+)/?.*$");
    public GithubParser(Parser successor) {
        setSuccessor(successor);
    }

    @Override
    public Value parse(URL url) {
        if (canHandleUrl(url, "github.com")) {
            Matcher matcher = pattern.matcher(url.getPath());
            if (matcher.matches() && !matcher.group(1).equals("marketplace") && !matcher.group(1).equals("sponsors")) {
                return new GithubValue(matcher.group(1), matcher.group(2));
            }
            return null;
        } else {
            return super.parse(url);
        }
    }
}
