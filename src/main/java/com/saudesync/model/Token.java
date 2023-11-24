package com.saudesync.model;

public record Token(
        String token,
        String type,
        String prefix
) {
}
