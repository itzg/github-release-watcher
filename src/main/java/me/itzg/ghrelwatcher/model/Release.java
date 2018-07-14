package me.itzg.ghrelwatcher.model;


import lombok.Data;

import java.util.Date;

@Data
public class Release {
    String name;
    String tag;
    Date publishedAt;
}
