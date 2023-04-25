package ru.tinkoff.edu.java.scrapper.client.dao.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.client.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.dao.jpa.JpaLinkTypeDao;
import ru.tinkoff.edu.java.scrapper.dto.jdbc.ChatEntity;
import ru.tinkoff.edu.java.scrapper.dto.jdbc.LinkEntity;
import ru.tinkoff.edu.java.scrapper.dto.jdbc.LinkUpdateData;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaTgChatService;

import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

@SpringBootTest(classes = {ScrapperApplication.class, IntegrationEnvironment.IntegrationEnvironmentConfiguration.class})
public class JpaLinkTest extends IntegrationEnvironment {
    @Container
    public PostgreSQLContainer<?> postgreSQLContainer = POSTGRES_SQL_CONTAINER;
    @Autowired
    private JpaTgChatService jpaTgChatService;
    @Autowired
    private JpaLinkService jpaLinkService;
    @Autowired
    private JpaLinkTypeDao jpaLinkTypeDao;

    @Transactional
    @Rollback
    @Test
    public void findLinkTypeByTypeTest() {
        Assertions.assertNotNull(jpaLinkTypeDao.findLinkTypeByType("github.com"));
        Assertions.assertNotNull(jpaLinkTypeDao.findLinkTypeByType("stackoverflow.com"));
    }

    @Transactional
    @Rollback
    @Test
    public void addTest() {
        final int n = 20;
        List<ChatEntity> chatEntitiesSent = getSampleListOfChatEntities(n);
        List<URI> linkUris = new ArrayList<>(getSampleListOfGithubLinks());
        linkUris.addAll(getSampleListOfStackoverflowLinks());
        Date creationDate = Date.valueOf(LocalDate.EPOCH);
        for (int i = 0; i < n; i++) {
            jpaTgChatService.register(chatEntitiesSent.get(i).id());
            jpaLinkService.add(new LinkEntity(linkUris.get(i), chatEntitiesSent.get(i).id(), "some description",
                    -1L, creationDate));
        }
        for (int i = 0; i < n; i++) {
            Assertions.assertNotNull(jpaLinkService.getLinksByUri(linkUris.get(i)));
        }
    }

    @Transactional
    @Rollback
    @Test
    public void removeTest() {
        final int n = 20;
        List<ChatEntity> chatEntitiesSent = getSampleListOfChatEntities(n);
        List<URI> linkUris = new ArrayList<>(getSampleListOfGithubLinks());
        linkUris.addAll(getSampleListOfStackoverflowLinks());
        Date creationDate = Date.valueOf(LocalDate.EPOCH);
        for (int i = 0; i < n; i++) {
            jpaTgChatService.register(chatEntitiesSent.get(i).id());
            jpaLinkService.add(new LinkEntity(linkUris.get(i), chatEntitiesSent.get(i).id(), "some description",
                    -1L, creationDate));
        }
        for (int i = 0; i < n; i++) {
            Assertions.assertNotNull(jpaLinkService.remove(chatEntitiesSent.get(i).id(), linkUris.get(i)));
            try {
                jpaLinkService.remove(chatEntitiesSent.get(i).id(), linkUris.get(i));
            } catch (RuntimeException e) {
                continue;
            }
            Assertions.fail();
        }
    }

    @Transactional
    @Rollback
    @Test
    public void listAllTest() {
        final int n = 20;
        List<ChatEntity> chatEntitiesSent = getSampleListOfChatEntities(n);
        List<URI> linkUris = new ArrayList<>(getSampleListOfGithubLinks());
        linkUris.addAll(getSampleListOfStackoverflowLinks());
        Date creationDate = Date.valueOf(LocalDate.EPOCH);
        for (int i = 0; i < n; i++) {
            jpaTgChatService.register(chatEntitiesSent.get(i).id());
            jpaLinkService.add(new LinkEntity(linkUris.get(i), chatEntitiesSent.get(i).id(), "some description",
                    -1L, creationDate));
        }
        final long chatId = 25235;
        final int k = 10;
        jpaTgChatService.register(chatId);
        for (int i = 0; i < k; i++) {
            jpaLinkService.add(new LinkEntity(linkUris.get(i), chatId, "description number " + i,
                    -1L, creationDate));
        }
        final List<LinkEntity> linkEntities = jpaLinkService.listAll(chatId).stream().toList();
        Assertions.assertEquals(k, linkEntities.size());
    }

    @Transactional
    @Rollback
    @Test
    public void getLinkTest() {
        final int n = 20;
        List<ChatEntity> chatEntitiesSent = getSampleListOfChatEntities(n);
        List<URI> linkUris = new ArrayList<>(getSampleListOfGithubLinks());
        linkUris.addAll(getSampleListOfStackoverflowLinks());
        Date creationDate = Date.valueOf(LocalDate.EPOCH);
        for (int i = 0; i < n; i++) {
            jpaTgChatService.register(chatEntitiesSent.get(i).id());
            jpaLinkService.add(new LinkEntity(linkUris.get(i), chatEntitiesSent.get(i).id(), "description number " + i,
                    -1L, creationDate));
        }
        for (int i = 0; i < n; i++) {
            final LinkEntity link = jpaLinkService.getLink(chatEntitiesSent.get(i).id(), linkUris.get(i));
            Assertions.assertEquals(chatEntitiesSent.get(i).id(), link.chatId());
            Assertions.assertEquals(-1L, link.lastUpdatedId());
            Assertions.assertEquals(linkUris.get(i), link.uri());
            Assertions.assertEquals("description number " + i, link.description());
        }
    }

    @Transactional
    @Rollback
    @Test
    public void getLinkByUriTest() {
        addTest();
    }

    @Transactional
    @Rollback
    @Test
    public void getLinksByTypeTest() {
        final int n = 20;
        List<ChatEntity> chatEntitiesSent = getSampleListOfChatEntities(n);
        List<URI> linkUris = new ArrayList<>(getSampleListOfGithubLinks());
        linkUris.addAll(getSampleListOfStackoverflowLinks());
        Date creationDate = Date.valueOf(LocalDate.EPOCH);
        for (int i = 0; i < n; i++) {
            jpaTgChatService.register(chatEntitiesSent.get(i).id());
            jpaLinkService.add(new LinkEntity(linkUris.get(i), chatEntitiesSent.get(i).id(), "description number " + i,
                    -1L, creationDate));
        }
        List<LinkUpdateData> linkEntities = jpaLinkService.getLinksByType("github.com");
        Assertions.assertEquals(10, linkEntities.size());
        for (int i = 0; i < 10; i++) {
            final var link = linkEntities.get(i);
            Assertions.assertEquals(-1L, link.lastUpdatedId());
            Assertions.assertEquals(linkUris.get(i), link.uri());
            Assertions.assertEquals(creationDate, link.lastChecked());
        }

        linkEntities = jpaLinkService.getLinksByType("stackoverflow.com");
        Assertions.assertEquals(10, linkEntities.size());
        for (int i = 10; i < 20; i++) {
            final var link = linkEntities.get(i - 10);
            Assertions.assertEquals(link.lastUpdatedId(), -1L);
            Assertions.assertEquals(link.uri(), linkUris.get(i));
            Assertions.assertEquals(link.lastChecked(), creationDate);
        }
    }

    private List<ChatEntity> getSampleListOfChatEntities(int n) {
        final long[] chatIds = RandomGenerator.getDefault().longs().limit(n).toArray();
        List<ChatEntity> chatEntityList = new ArrayList<>();
        for (long chatId : chatIds) {
            chatEntityList.add(new ChatEntity(chatId));
        }
        return chatEntityList;
    }

    private void saveChatsAndLinks() {
        final int n = 20;
        List<ChatEntity> chatEntitiesSent = getSampleListOfChatEntities(n);
        List<URI> linkUris = new ArrayList<>(getSampleListOfGithubLinks());
        linkUris.addAll(getSampleListOfStackoverflowLinks());
        Date creationDate = Date.valueOf(LocalDate.MIN);
        for (int i = 0; i < n; i++) {
            jpaTgChatService.register(chatEntitiesSent.get(i).id());
            jpaLinkService.add(new LinkEntity(linkUris.get(i), chatEntitiesSent.get(i).id(), "some description",
                    -1L, creationDate));
        }
    }

    private List<URI> getSampleListOfGithubLinks() {
        return List.of(
                URI.create("https://github.com/tensorflow/tensorflow"),
                URI.create("https://github.com/freeCodeCamp/freeCodeCamp"),
                URI.create("https://github.com/vuejs/vue"),
                URI.create("https://github.com/facebook/react"),
                URI.create("https://github.com/docker/docker-ce"),
                URI.create("https://github.com/ohmyzsh/ohmyzsh"),
                URI.create("https://github.com/ansible/ansible"),
                URI.create("https://github.com/kubernetes/kubernetes"),
                URI.create("https://github.com/apache/spark"),
                URI.create("https://github.com/Microsoft/vscode")
        );
    }

    private List<URI> getSampleListOfStackoverflowLinks() {
        return List.of(
                URI.create("https://stackoverflow.com/questions/159521/getting-a-function-name-as-a-string"),
                URI.create("https://stackoverflow.com/questions/417142/what-is-the-difference-between-a-stack-and-a-queue"),
                URI.create("https://stackoverflow.com/questions/1732348/regex-match-open-tags-except-xhtml-self-contained-tags"),
                URI.create("https://stackoverflow.com/questions/845345/whats-your-favorite-version-control-system"),
                URI.create("https://stackoverflow.com/questions/22284292/how-to-deal-with-a-class-that-might-not-be-defined-when-compiling-a-c-template"),
                URI.create("https://stackoverflow.com/questions/1450956/why-are-there-no-concurrent-cpu-intensive-databases"),
                URI.create("https://stackoverflow.com/questions/1492453/how-do-you-make-sure-email-you-send-programatically-is-not-automatically-marked"),
                URI.create("https://stackoverflow.com/questions/1414565/how-to-get-all-of-the-immediate-subdirectories-in-c"),
                URI.create("https://stackoverflow.com/questions/121199/why-use-multiple-cpu-cores"),
                URI.create("https://stackoverflow.com/questions/40480/is-java-pass-by-reference-or-pass-by-value")
        );
    }
}
