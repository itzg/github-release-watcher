package me.itzg.ghrelwatcher.web;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import me.itzg.ghrelwatcher.GithubGraphql;
import me.itzg.ghrelwatcher.model.Repositories;
import me.itzg.ghrelwatcher.model.ValueHolder;
import me.itzg.ghrelwatcher.services.GithubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Geoff Bourne
 * @since May 2018
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

    private final GithubService githubService;

    @Autowired
    public ApiController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("githubUsername")
    public ValueHolder<String> githubUsername() {
        return ValueHolder.of(githubService.getUsername());
    }

    @GetMapping("initialStarredRepos")
    public Repositories getInitialStarredRepos() {
        return githubService.getInitialStarredRepos();
    }

    @GetMapping("nextStarredRepos")
    public Repositories getNextStarredRepos(@RequestParam String cursor) {
        return githubService.getNextStarredRepos(cursor);
    }
}
