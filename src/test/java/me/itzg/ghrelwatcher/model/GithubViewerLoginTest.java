package me.itzg.ghrelwatcher.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author Geoff Bourne
 * @since Jul 2018
 */
public class GithubViewerLoginTest {

    @Test
    public void testParsing() throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();

        final ClassPathResource resource = new ClassPathResource("from-github/viewer-login.json");

        final JsonNode queryResult = objectMapper.readValue(resource.getFile(),
                JsonNode.class);

        final JsonNode loginNode = queryResult.path("data").path("viewer").path("login");
        assertFalse(loginNode.isMissingNode());
        final String login = loginNode.asText();
        assertEquals("itzg", login);
    }
}