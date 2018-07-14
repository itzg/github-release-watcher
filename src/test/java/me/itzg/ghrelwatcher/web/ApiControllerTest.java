package me.itzg.ghrelwatcher.web;

import me.itzg.ghrelwatcher.GithubGraphql;
import me.itzg.ghrelwatcher.model.ValueHolder;
import me.itzg.ghrelwatcher.services.GithubService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author Geoff Bourne
 * @since Jul 2018
 */
@RunWith(SpringRunner.class)
@RestClientTest(ApiController.class)
@AutoConfigureWebClient(registerRestTemplate = true)
public class ApiControllerTest {

    @Autowired
    ApiController apiController;

    @MockBean
    GithubService githubService;

    @Autowired
    MockRestServiceServer server;

    @Test
    public void testSimple() {
        server.expect(requestTo(GithubGraphql.PATH_GRAPHQL))
                .andRespond(withSuccess(new ClassPathResource("from-github/viewer-login.json"), MediaType.APPLICATION_JSON));

        when(githubService.getUsername())
                .thenReturn("itzg");

        final ValueHolder<String> response = apiController.githubUsername();
        assertNotNull(response);
        assertEquals("itzg", response.getValue());
    }
}