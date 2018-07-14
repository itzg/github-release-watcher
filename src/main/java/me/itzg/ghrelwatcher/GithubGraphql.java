package me.itzg.ghrelwatcher;

/**
 * @author Geoff Bourne
 * @since Jul 2018
 */
public class GithubGraphql {

    public static final String ROOT_URL = "https://api.github.com";
    public static final String PATH_GRAPHQL = "/graphql";

    public static final String QUERY_STARREDREPOS_RELEASES = "{\n" +
            "  viewer {\n" +
            "    starredRepositories(first: $REPO_COUNT, after:$REPOS_AFTER, orderBy: {field: STARRED_AT, direction: DESC}) {\n" +
            "      nodes {\n" +
            "        url\n" +
            "        homepageUrl\n" +
            "        name\n" +
            "        owner {login}\n" +
            "        nameWithOwner\n" +
            "        releases(first:$REL_COUNT, after:$RELS_AFTER, orderBy: {field: CREATED_AT, direction: DESC}) {\n" +
            "          nodes {\n" +
            "            name\n" +
            "            tag {\n" +
            "              name\n" +
            "            }\n" +
            "            publishedAt\n" +
            "          }\n" +
            "          pageInfo {\n" +
            "            hasNextPage\n" +
            "            endCursor\n" +
            "          }\n" +
            "          totalCount\n" +
            "        }\n" +
            "      }\n" +
            "      pageInfo {\n" +
            "        hasNextPage\n" +
            "        endCursor\n" +
            "      }\n" +
            "      totalCount\n" +
            "    }\n" +
            "  }\n" +
            "}\n";
    public static final String QUERY_VIEWER_LOGIN = "query{viewer{login}}";
}
