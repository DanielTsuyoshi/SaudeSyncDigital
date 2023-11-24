package com.saudesync.exceptions;

public record RestError(
    int cod,
    String message
) {}
