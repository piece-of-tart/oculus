package ru.tinkoff.edu.java.parser.parsers;

import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.parser.ParserLinker;
import ru.tinkoff.edu.java.parser.values.GithubValue;
import ru.tinkoff.edu.java.parser.values.StackOverflowValue;

import static org.junit.jupiter.api.Assertions.*;

class StackOverflowParserTest {
    @Test
    public void testUserRep() {
        final var parser = new ParserLinker();

        assertEquals(((StackOverflowValue) parser.parse("https://stackoverflow.com/questions/75775823/django-not-found-media-get-media-http-1-1-404-2679")).id(), "75775823");
        assertEquals(((StackOverflowValue) parser.parse("https://stackoverflow.com/questions/75775104/does-this-transformation-has-a-name-in-math-fp-or-else")).id(), "75775104");
        assertEquals(((StackOverflowValue) parser.parse("https://stackoverflow.com/questions/21577611/scala-parser-combinators-getting-a-stackoverflow-with-packratparsers")).id(), "21577611");
        assertNull(parser.parse("https://stackoverflow.co/talent/"));
        assertNull(parser.parse("https://resources.stackoverflow.co/talent/"));
        assertNull(parser.parse("https://info.stackoverflowsolutions.com/cookieless-world-ebook.html"));
    }

    @Test
    public void testIncorrectURL() {
        final var parser = new ParserLinker();

        assertNull(parser.parse(""));
        assertNull(parser.parse("gskagj34jg349ss9@@#r"));
        assertNull(parser.parse("some_freak_http"));
        assertNull(parser.parse("http/http"));
    }
}