package me.itzg.ghrelwatcher.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Geoff Bourne
 * @since Jul 2018
 */
@ConfigurationProperties("app")
@Component
@Data
public class AppProperties {
    int queryRepoCount = 50;
    int queryReleaseCount = 5;
    double githubRateLimit = 1.0;
}
