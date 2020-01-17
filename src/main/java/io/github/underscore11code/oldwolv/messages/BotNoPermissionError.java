package io.github.underscore11code.oldwolv.messages;

import io.github.underscore11code.oldwolv.util.PrettyUtil;
import lombok.Getter;
import org.javacord.api.entity.permission.PermissionType;

public class BotNoPermissionError implements Message {
    @Getter MessageType messageType = MessageType.ERROR;
    @Getter String title = "I don't have permission to do that";
    @Getter String description = "Missing permission %s";

    public BotNoPermissionError(PermissionType permissionType) {
        this.description = String.format(description, PrettyUtil.prettyEnum(permissionType.name()));
    }
}
