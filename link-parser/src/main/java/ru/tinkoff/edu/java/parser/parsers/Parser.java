package ru.tinkoff.edu.java.parser.parsers;

import ru.tinkoff.edu.java.parser.values.Value;

import java.net.URI;

public interface Parser {

    Value parse(URI url);
}
