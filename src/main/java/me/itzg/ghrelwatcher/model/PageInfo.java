package me.itzg.ghrelwatcher.model;

import lombok.Data;

@Data
public class PageInfo {
    boolean hasNextPage;
    String endCursor;
}
