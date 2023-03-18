package ru.tinkoff.edu.java.parser;

import ru.tinkoff.edu.java.parser.parsers.GithubParser;
import ru.tinkoff.edu.java.parser.parsers.StackOverflowParser;
import ru.tinkoff.edu.java.parser.values.Value;

import java.net.MalformedURLException;
import java.net.URL;

public class ParserLinker {
    public void main(String[] args) {
        System.out.println(parse("https://stackoverflow.co/teams/"));
        System.out.println(parse("https://stackoverflow.com/teams/"));
        System.out.println(parse("https://stackoverflow.com/questions/577554/when-is-assembly-faster-than-c"));
        System.out.println(parse("https://github.com/Dogzik/Paradigms-in-ITMO"));
        System.out.println(parse("https://github.com/Dogzik/Paradigms-in-ITMO/blob/master/HW07/myEcxeptions/IllegalOperationException.java"));
        System.out.println(parse("https://github.com/marketplace/codefactor"));
    }

    public Value parse(final String stringUrl) {
        final URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            return null;
        }
        var githubParser = new GithubParser(null);
        var stackOverFlow = new StackOverflowParser(githubParser);

        return stackOverFlow.parse(url);
    }
}
