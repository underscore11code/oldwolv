package io.github.underscore11code.oldwolv.modules;

import io.github.underscore11code.oldwolv.OldWolv;
import io.github.underscore11code.oldwolv.config.GuildConfig;
import io.github.underscore11code.oldwolv.messages.*;
import io.github.underscore11code.oldwolv.util.CommandUtil;
import io.github.underscore11code.oldwolv.util.PermissionUtil;
import lombok.Getter;
import lombok.Setter;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;
import java.util.ArrayList;

public class ModuleVerify implements Module{
    @Getter String name = "Verify";
    @Getter String description = "Commands for verifying new server members";
    @Getter @Setter ArrayList<Command> commands = new ArrayList<>();

    @Command(triggers = {"verify", "v"}, args = "<user>", helpMsg = "Gives the <user> the Verified role")
    public static void commandVerify(CommandInfo cmd) {
        if (!cmd.getServer().isPresent()) {
            CommandUtil.sendReply(cmd.getRawEvent(), new SimpleMessage(SimpleMessage.Error.NOT_IN_SERVER));
        } else if (!cmd.isServerStaff()) {
            CommandUtil.sendReply(cmd.getRawEvent(), new UserNoPermissionError("Server Staff"));
        } else if (cmd.getArgs().size() != 1) {
          CommandUtil.sendReply(cmd.getRawEvent(), new ArgumentError(new String[]{"<user>"}, cmd.getArgs()));
        } else {
            Server server = cmd.getServer().get();
            GuildConfig config = GuildConfig.get(server.getIdAsString());
            String roleId = config.getVerifiedRoleId();
            if (roleId == null) {
                CommandUtil.sendReply(cmd.getRawEvent(), new SimpleMessage(MessageType.ERROR, "No Verified role set", "Please set a verified role with setverifiedrole <role>"));
            } else if (!server.getRoleById(roleId).isPresent()) {
                CommandUtil.sendReply(cmd.getRawEvent(), new SimpleMessage(MessageType.ERROR, "No Verified role set", "Please set a verified role with setverifiedrole <role>"));
            } else {
                Role verifiedRole = server.getRoleById(roleId).get();
                OldWolv.getApi().getUserById(CommandUtil.cleanId(cmd.getArgs().get(0))).thenAcceptAsync(user -> {
                    if (server.isMember(user)) {
                        if (!server.getRoles(user).contains(verifiedRole)) {
                            if (PermissionUtil.botHasPermission(server, PermissionType.MANAGE_ROLES)) {
                                user.addRole(verifiedRole);
                                CommandUtil.sendReply(cmd.getRawEvent(), new SimpleMessage(MessageType.OK, "User Verified!", user.getMentionTag() + " (" + user.getIdAsString() + ")"));
                            } else {
                                CommandUtil.sendReply(cmd.getRawEvent(), new BotNoPermissionError(PermissionType.MANAGE_ROLES));
                            }
                        } else {
                            CommandUtil.sendReply(cmd.getRawEvent(), new SimpleMessage(MessageType.ERROR, "User Already Verified!", user.getMentionTag() + " (" + user.getIdAsString() + ")"));
                        }
                    } else {
                        CommandUtil.sendReply(cmd.getRawEvent(), new UnknownEntityError("User"));
                    }
                });
            }
        }

    }
}
