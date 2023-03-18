package ru.tinkoff.edu.java.parser.parsers;

import ru.tinkoff.edu.java.parser.values.Value;

import java.net.URL;

public sealed class Parser permits
    StackOverflowParser, GithubParser
{
    private Parser successor;

    public Value parse(URL url) {
        return successor == null ? null : successor.parse(url);
    }

    protected boolean canHandleUrl(URL url, String host) {
        return url.getHost().equals(host);
    }

    protected Parser getSuccessor() {
        return successor;
    }

    protected void setSuccessor(Parser successor) {
        this.successor = successor;
    }
}
