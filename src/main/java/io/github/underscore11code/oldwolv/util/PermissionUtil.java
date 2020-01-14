package io.github.underscore11code.oldwolv.util;

import io.github.underscore11code.oldwolv.OldWolv;
import lombok.Getter;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

public class PermissionUtil {
    @Getter private static PermissionType[] staffPerms= {PermissionType.MANAGE_ROLES, PermissionType.MANAGE_CHANNELS, PermissionType.BAN_MEMBERS, PermissionType.KICK_MEMBERS, PermissionType.MANAGE_SERVER, PermissionType.PRIORITY_SPEAKER};

    public static boolean isBotOwner(User user) {
        return OldWolv.getOwner().equals(user);
    }

    public static boolean isBotStaff(User user) {
        if (isBotOwner(user))
            return true;
        return false;
    }

    public static boolean isServerOwner(User user, Server server) {
        return server.getOwner().equals(user);
    }

    public static boolean isServerAdmin(User user, Server server) {
        return server.hasPermission(user, PermissionType.ADMINISTRATOR);
    }

    public static boolean isServerStaff(User user, Server server) {
        for (PermissionType perm : staffPerms)
            if (server.hasPermission(user, perm))
                return true;
        return false;
    }
}