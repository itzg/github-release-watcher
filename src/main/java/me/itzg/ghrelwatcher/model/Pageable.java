package me.itzg.ghrelwatcher.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Geoff Bourne
 * @since Jul 2018
 */
@Data
public abstract class Pageable<T> {
    boolean moreAvailable;
    int totalCount;
    String cursor;

    List<T> nodes = new ArrayList<>();
}
