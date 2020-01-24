package io.github.underscore11code.oldwolv.util;

import io.github.underscore11code.oldwolv.OldWolv;
import io.github.underscore11code.oldwolv.config.GuildConfig;
import lombok.Getter;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

public class PermissionUtil {
    @Getter private static PermissionType[] staffPerms= {PermissionType.MANAGE_ROLES, PermissionType.MANAGE_CHANNELS, PermissionType.BAN_MEMBERS, PermissionType.KICK_MEMBERS, PermissionType.MANAGE_SERVER};

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
        for (String id : GuildConfig.get(server.getIdAsString()).getStaffRoleIds())
            if (server.getRoleById(id).isPresent())
                if (user.getRoles(server).contains(server.getRoleById(id).get()))
                    return true;
        return false;
    }

    public static boolean botHasPermission(Server server, PermissionType permissionType) {
        return server.hasPermission(OldWolv.getApi().getYourself(), permissionType) || server.hasPermissions(OldWolv.getApi().getYourself(), PermissionType.ADMINISTRATOR);
    }
}
