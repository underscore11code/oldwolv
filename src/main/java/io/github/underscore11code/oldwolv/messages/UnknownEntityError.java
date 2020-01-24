package io.github.underscore11code.oldwolv.messages;

import lombok.Getter;

public class UnknownEntityError implements Message {
    @Getter MessageType messageType = MessageType.ERROR;
    @Getter String title = "Unknown %s";
    @Getter String description = "";

    public UnknownEntityError(String what) {
        this(what, "");
    }

    public UnknownEntityError(String what, String entity) {
        this.title = String.format(title, what);
        this.description = entity;
    }
}
