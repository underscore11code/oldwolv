package io.github.underscore11code.oldwolv.messages;

import io.github.underscore11code.oldwolv.util.PrettyUtil;
import lombok.Getter;
import org.javacord.api.entity.permission.PermissionType;

public class UserNoPermissionError implements Message {
    @Getter MessageType messageType = MessageType.ERROR;
    @Getter String title = "You don't have permission to do that";
    @Getter String description = "Missing permission %s";

    public UserNoPermissionError(String permission) {
        this.description = String.format(description, permission);
    }

    public UserNoPermissionError(PermissionType permissionType) {
        this.description = String.format(description, PrettyUtil.prettyEnum(permissionType.name()));
    }
}
