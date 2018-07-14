package me.itzg.ghrelwatcher.model;

import lombok.Data;

/**
 * @author Geoff Bourne
 * @since Jul 2018
 */
@Data
public class ValueHolder<T> {
    T value;

    public static <T> ValueHolder<T> of(T data) {
        final ValueHolder<T> response = new ValueHolder<>();
        response.setValue(data);
        return response;
    }
}
