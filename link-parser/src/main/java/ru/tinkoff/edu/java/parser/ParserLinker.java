package ru.tinkoff.edu.java.parser;

import ru.tinkoff.edu.java.parser.parsers.GithubParser;
import ru.tinkoff.edu.java.parser.parsers.StackOverflowParser;
import ru.tinkoff.edu.java.parser.values.Value;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class ParserLinker {

    public Value parse(final String stringUrl) {
        final URI uri;
        try {
            uri = URI.create(stringUrl);
        } catch (Exception e) {
            return null;
        }
        var githubParser = new GithubParser(null);
        var stackOverFlow = new StackOverflowParser(githubParser);

        return stackOverFlow.parse(uri);
    }
}
