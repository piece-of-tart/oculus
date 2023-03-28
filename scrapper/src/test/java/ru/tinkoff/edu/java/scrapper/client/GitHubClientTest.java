package ru.tinkoff.edu.java.scrapper.client;

import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.parser.ParserLinker;
import ru.tinkoff.edu.java.parser.values.GithubValue;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GitHubClientTest {

    @Test
    void getGithubRepositoryTest() {
        ParserLinker parserLinker = new ParserLinker();
        GitHubClient gitHubClient = new GitHubClient();

        GithubValue repo1 = (GithubValue) parserLinker.parse("https://github.com/Dogzik/jpepeg");
        var repo1Response = gitHubClient.getRepository(repo1.user(), repo1.rep()).block();
        assertEquals(Objects.requireNonNull(repo1Response).getName(), "jpepeg");
        assertEquals(repo1Response.getDescription(), "Transcoder from vanilla jpeg to more efficient format");

        GithubValue repo2 = (GithubValue) parserLinker.parse("https://github.com/Example-uPagge/swagger_setting");
        var repo2Response = gitHubClient.getRepository(repo2.user(), repo2.rep()).block();
        assertEquals(Objects.requireNonNull(repo2Response).getName(), "swagger_setting");
        assertEquals(repo2Response.getDescription(), "https://blog.uPagge.ru/posts/guide/spring/swagger/");
    }
}