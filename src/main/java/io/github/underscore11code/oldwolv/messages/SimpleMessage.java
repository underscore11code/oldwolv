package io.github.underscore11code.oldwolv.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SimpleMessage implements Message {
    @Getter MessageType messageType;
    @Getter String title;
    @Getter String description;

    public SimpleMessage(Error msg) {
        this.messageType = msg.messageType;
        this.title = msg.title;
        this.description = msg.description;
    }

    @AllArgsConstructor
    public enum Error {
        NOT_IN_SERVER(MessageType.ERROR, "Not in a server!", "This command can only be ran in a server.");

        @Getter private MessageType messageType;
        @Getter private String title;
        @Getter private String description;
    }
}
