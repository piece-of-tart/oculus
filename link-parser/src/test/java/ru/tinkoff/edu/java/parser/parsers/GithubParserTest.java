package ru.tinkoff.edu.java.parser.parsers;

import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.parser.ParserLinker;
import ru.tinkoff.edu.java.parser.values.GithubValue;

import static org.junit.jupiter.api.Assertions.*;

class GithubParserTest {
    @Test
    public void testUserRep() {
        final var parser = new ParserLinker();

        assertEquals(((GithubValue) parser.parse("https://github.com/Dogzik/Paradigms-in-ITMO/blob/master/HW06/expression/AbstractBinaryOperator.java")).user(), "Dogzik");
        assertEquals(((GithubValue) parser.parse("https://github.com/WLM1ke/poptimizer")).rep(), "poptimizer");
        assertEquals(((GithubValue) parser.parse("https://github.com/doctor-kaliy/java-advanced/tree/main/java-solutions/info")).user(), "doctor-kaliy");
        assertNull(parser.parse("https://github.com/marketplace/codefactor"));
        assertNull(parser.parse("https://github.com/explore"));
    }

    @Test
    public void testIncorrectURL() {
        final var parser = new ParserLinker();

        assertThrows(Exception.class , () -> parser.parse(""));
        assertThrows(Exception.class , () -> parser.parse("gskagj34jg349ss9@@#r"));
        assertThrows(Exception.class , () -> parser.parse("some_freak_http"));
        assertThrows(Exception.class , () -> parser.parse("http/http"));
    }
}