package io.github.underscore11code.oldwolv.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

@AllArgsConstructor
public enum MessageType {
    OK("\u2705", Color.GREEN),
    ERROR("\u274c", Color.RED),
    EXCEPTION("\u203c", Color.RED);

    @Getter private String reaction;
    @Getter private Color embedColor;
}
