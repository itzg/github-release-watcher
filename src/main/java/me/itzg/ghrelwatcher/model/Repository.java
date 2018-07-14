package me.itzg.ghrelwatcher.model;

import lombok.Data;

@Data
public class Repository {
    String url;
    String homepageUrl;
    String name;
    String owner;
    Releases releases;
}
