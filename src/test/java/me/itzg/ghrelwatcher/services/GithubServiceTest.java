package me.itzg.ghrelwatcher.services;

import me.itzg.ghrelwatcher.GithubGraphql;
import me.itzg.ghrelwatcher.config.AppProperties;
import me.itzg.ghrelwatcher.model.Release;
import me.itzg.ghrelwatcher.model.Repositories;
import me.itzg.ghrelwatcher.model.Repository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.Date;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author Geoff Bourne
 * @since Jul 2018
 */
@RunWith(SpringRunner.class)
@RestClientTest(components = {GithubServiceImpl.class, AppProperties.class})
@AutoConfigureWebClient(registerRestTemplate = true)
public class GithubServiceTest {

    @Autowired
    GithubService githubService;

    @Autowired
    MockRestServiceServer server;

    @Test
    public void getUsername() {
        server.expect(requestTo(GithubGraphql.PATH_GRAPHQL))
                .andRespond(withSuccess(new ClassPathResource("from-github/viewer-login.json"), MediaType.APPLICATION_JSON));

        final String username = githubService.getUsername();
        assertEquals("itzg", username);

    }

    @Test
    public void testGetStarredRepos() {
        server.expect(requestTo(GithubGraphql.PATH_GRAPHQL))
                .andRespond(withSuccess(new ClassPathResource("from-github/starredRepositories-releases.json"), MediaType.APPLICATION_JSON));

        final Repositories repositories = githubService.getInitialStarredRepos();

        assertNotNull(repositories);
        assertEquals(192, repositories.getTotalCount());
        assertEquals("Y3Vyc29yOnYyOpK5MjAxOC0wNC0xNVQxNzozNDoyNi0wNTowMM4HP4Zp", repositories.getCursor());
        assertTrue(repositories.isMoreAvailable());

        final Repository repository = repositories.getNodes().get(2);
        assertEquals("https://goreliu.github.io/wsl-terminal/", repository.getHomepageUrl());
        assertEquals("https://github.com/goreliu/wsl-terminal", repository.getUrl());
        assertEquals("goreliu", repository.getOwner());
        assertEquals("wsl-terminal", repository.getName());

        assertEquals(34, repository.getReleases().getTotalCount());
        assertEquals("Y3Vyc29yOnYyOpK5MjAxNy0xMS0yMVQwNToxNjo0NS0wNjowMM4Agzz3",
                repository.getReleases().getCursor());
        assertTrue(repository.getReleases().isMoreAvailable());

        final Release release = repositories.getNodes().get(0).getReleases().getNodes().get(0);
        assertEquals("v1.1.0", release.getName());
        assertEquals("v1.1.0", release.getTag());
        assertEquals(new Date(1526368157000L), release.getPublishedAt());
    }
}