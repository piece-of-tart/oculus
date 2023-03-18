package ru.tinkoff.edu.java.parser.values;

public sealed interface Value permits
        GithubValue,
        StackOverflowValue
{}