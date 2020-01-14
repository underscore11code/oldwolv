package io.github.underscore11code.oldwolv.modules;

import io.github.underscore11code.oldwolv.OldWolv;
import io.github.underscore11code.oldwolv.util.CommandUtil;
import io.github.underscore11code.oldwolv.util.PermissionUtil;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;

public class ModuleVerify {
    @Command(triggers = {"verify", "v"}, args = "<user mention|user ID>", helpMsg = "Gives the <user> the Verified role")
    public static void commandVerify(CommandInfo cmd) {
        if (!cmd.getServer().isPresent()) {
            CommandUtil.sendUserError(cmd.getRawEvent(), "Not in a server", "This command can only be run in a server");
            return;
        }
        cmd.getServer().ifPresent(server -> {
            if (cmd.isServerStaff()) {
                if (cmd.getArgs().size() == 1) {
                    String userId = cmd.getArgs().get(0).replace("<@", "").replace(">", "").replace("!", "");
                    OldWolv.getApi().getUserById(userId).thenAcceptAsync(user -> {
                       if (server.getMembers().contains(user)) {
                           if (!server.getRoles(user).contains(server.getRolesByName("Verified").get(0))) {
                               server.addRoleToUser(user, server.getRolesByName("Verified").get(0));
                               CommandUtil.sendReply(cmd.getRawEvent(), CommandUtil.getTemplateEmbed().setColor(Color.GREEN).setTitle("Verified " + user.getDiscriminatedName()));
                           } else
                               CommandUtil.sendUserError(cmd.getRawEvent(), "That user is already verified!", "");
                       } else
                           CommandUtil.sendUserError(cmd.getRawEvent(), "User is not in the server!", "");
                    });
                } else
                    CommandUtil.sendUserError(cmd.getRawEvent(), "No user given!", "");
            } else {
                CommandUtil.sendUserError(cmd.getRawEvent(), "You don't have permission!", "");
                return;
            }
        });
    }
}
