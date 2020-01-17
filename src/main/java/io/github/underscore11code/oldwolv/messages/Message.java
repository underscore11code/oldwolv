package io.github.underscore11code.oldwolv.messages;

public interface Message {
    MessageType getMessageType();

    String getTitle();

    String getDescription();
}
