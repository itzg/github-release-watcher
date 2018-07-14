package me.itzg.ghrelwatcher.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import me.itzg.ghrelwatcher.GithubGraphql;
import me.itzg.ghrelwatcher.config.AppProperties;
import me.itzg.ghrelwatcher.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static me.itzg.ghrelwatcher.GithubGraphql.PATH_GRAPHQL;
import static me.itzg.ghrelwatcher.GithubGraphql.QUERY_VIEWER_LOGIN;

/**
 * @author Geoff Bourne
 * @since Jul 2018
 */
@Service
@Slf4j
public class GithubServiceImpl implements GithubService {
    private final RestTemplate restTemplate;
    private final AppProperties appProperties;
    private final ObjectMapper objectMapper;
    @SuppressWarnings("UnstableApiUsage")
    private final RateLimiter githubRateLimiter;

    @Autowired
    public GithubServiceImpl(RestTemplate restTemplate, AppProperties appProperties, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.appProperties = appProperties;
        this.objectMapper = objectMapper;

        //noinspection UnstableApiUsage
        githubRateLimiter = RateLimiter.create(appProperties.getGithubRateLimit());
    }

    @Override
    public String getUsername() {
        final JsonNode result =
                restTemplate.postForObject(PATH_GRAPHQL, new GraphqlRequest(QUERY_VIEWER_LOGIN),
                        JsonNode.class);

        //noinspection ConstantConditions
        return result.path("data").path("viewer").path("login").asText();
    }

    @Override
    public Repositories getInitialStarredRepos() {
        final String query = GithubGraphql.QUERY_STARREDREPOS_RELEASES
                .replace("$REPO_COUNT", Integer.toString(appProperties.getQueryRepoCount()))
                .replace("$REPOS_AFTER", "null")
                .replace("$REL_COUNT", Integer.toString(appProperties.getQueryReleaseCount()))
                .replace("$RELS_AFTER", "null");

        return queryStarredRepos(query);
    }

    @Override
    public Repositories getNextStarredRepos(String reposCursor) {
        final String query = GithubGraphql.QUERY_STARREDREPOS_RELEASES
                .replace("$REPO_COUNT", Integer.toString(appProperties.getQueryRepoCount()))
                .replace("$REPOS_AFTER", String.format("\"%s\"", reposCursor))
                .replace("$REL_COUNT", Integer.toString(appProperties.getQueryReleaseCount()))
                .replace("$RELS_AFTER", "null");

        return queryStarredRepos(query);
    }

    private Repositories queryStarredRepos(String query) {
        githubRateLimiter.acquire();

        final JsonNode result =
                restTemplate.postForObject(PATH_GRAPHQL, new GraphqlRequest(query),
                        JsonNode.class);

        final JsonNode errors = result.path("errors");
        if (errors.isArray() && errors.size() > 0) {
            throw new RuntimeException(errors.get(0).get("message").asText());
        }
        @SuppressWarnings("ConstantConditions") final JsonNode reposNode = result.path("data").path("viewer").path("starredRepositories");

        final Repositories repositories = new Repositories();

        try {
            parsePageInfo(reposNode, repositories);
            reposNode.get("nodes").iterator().forEachRemaining(jsonNode -> {
                repositories.getNodes().add(parseRepository(jsonNode));
            });
        } catch (NullPointerException e) {
            log.warn("Failed to parse {}", reposNode);
        }

        return repositories;
    }

    private Repository parseRepository(JsonNode repoNode) {
        final Repository repository = new Repository();

        repository.setUrl(repoNode.get("url").asText());
        repository.setHomepageUrl(repoNode.get("homepageUrl").asText());
        repository.setName(repoNode.get("name").asText());
        repository.setOwner(repoNode.get("owner").get("login").asText());

        final Releases releases = new Releases();
        final JsonNode releasesNode = repoNode.get("releases");
        parsePageInfo(releasesNode, releases);
        releasesNode.get("nodes").iterator().forEachRemaining(jsonNode -> {
            releases.getNodes().add(parseRelease(repository, jsonNode));
        });
        repository.setReleases(releases);

        return repository;
    }

    private Release parseRelease(Repository repository, JsonNode releaseNode) {
        final Release release = new Release();
        release.setName(releaseNode.get("name").asText());
        try {
            release.setTag(releaseNode.get("tag").get("name").asText());
        } catch (NullPointerException e) {
            log.warn("Bad tag in {}/{}", repository.getOwner(), repository.getName());
        }
        try {
            // since a JsonNode can't parse dates itself, we'll use the object mapper
            release.setPublishedAt(objectMapper.treeToValue(releaseNode.get("publishedAt"), Date.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse publishedAt date", e);
        }

        return release;
    }

    /**
     *
     * @param connectionNode a JSON node with pageInfo and totalCount fields
     * @param pageable the {@link Pageable} to populate
     */
    private void parsePageInfo(JsonNode connectionNode, Pageable<?> pageable) {
        pageable.setTotalCount(connectionNode.get("totalCount").asInt(0));

        final JsonNode pageInfoNode = connectionNode.get("pageInfo");
        pageable.setMoreAvailable(pageInfoNode.get("hasNextPage").asBoolean());
        pageable.setCursor(pageInfoNode.get("endCursor").asText());
    }

}
