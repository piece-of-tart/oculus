package ru.tinkoff.edu.java.parser.parsers;

import ru.tinkoff.edu.java.parser.values.Value;

import java.net.URL;

public interface Parser {

    Value parse(URL url);
}
