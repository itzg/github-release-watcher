package me.itzg.ghrelwatcher.services;

import me.itzg.ghrelwatcher.model.Repositories;

/**
 * @author Geoff Bourne
 * @since Jul 2018
 */
public interface GithubService {
    String getUsername();

    Repositories getInitialStarredRepos();

    Repositories getNextStarredRepos(String reposCursor);
}
