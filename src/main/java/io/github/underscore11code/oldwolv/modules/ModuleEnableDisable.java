package io.github.underscore11code.oldwolv.modules;

import io.github.underscore11code.oldwolv.OldWolv;
import io.github.underscore11code.oldwolv.config.GuildConfig;
import io.github.underscore11code.oldwolv.util.CommandUtil;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class ModuleEnableDisable {
    @Command(triggers = "disable", args = "<command>", helpMsg = "Disables the <command> given server-wide.\nAdmins can still override it")
    public static void commandDisable(CommandInfo cmd) {
        if (cmd.getServer().isPresent()) {
            if (cmd.isServerAdmin()) {
                if (cmd.getArgs().size() != 1)
                    CommandUtil.sendUserError(cmd.getRawEvent(), "Expected 1 argument, received " + cmd.getArgs().size(), "Stynax: `disable <command>`");
                else {
                    if (OldWolv.getCommandManager().getCommands().containsKey(cmd.getArgs().get(0))) {
                        GuildConfig config = GuildConfig.get(cmd.getServer().get().getIdAsString());
                        ArrayList<String> disabledCmds = config.getDisabledCommands();
                        disabledCmds.addAll(Arrays.asList(OldWolv.getCommandManager().getCommands().get(cmd.getArgs().get(0)).getAnnotation(Command.class).triggers()));
                        config.setDisabledCommands(disabledCmds);
                        config.save();
                        CommandUtil.sendReply(cmd.getRawEvent(), CommandUtil.getTemplateEmbed().setColor(Color.GREEN).setTitle("Disabled command " + cmd.getArgs().get(0)));
                    } else
                        CommandUtil.sendUserError(cmd.getRawEvent(), "Unknown command!", "");
                }
            } else
                CommandUtil.sendUserError(cmd.getRawEvent(), "You don't have permission!", "");
        } else
            CommandUtil.sendUserError(cmd.getRawEvent(), "Not in a server!", "");
    }

    @Command(triggers = "enable", args = "<command>", helpMsg = "Enables the <command> given server-wide, if it was previously disabled")
    public static void commandEnable(CommandInfo cmd) {
        if (cmd.getServer().isPresent()) {
            if (cmd.isServerAdmin()) {
                if (cmd.getArgs().size() != 1)
                    CommandUtil.sendUserError(cmd.getRawEvent(), "Expected 1 argument, received " + cmd.getArgs().size(), "Stynax: `disable <command>`");
                else {
                    if (OldWolv.getCommandManager().getCommands().containsKey(cmd.getArgs().get(0))) {
                        GuildConfig config = GuildConfig.get(cmd.getServer().get().getIdAsString());
                        ArrayList<String> disabledCmds = config.getDisabledCommands();
                        disabledCmds.removeAll(Arrays.asList(OldWolv.getCommandManager().getCommands().get(cmd.getArgs().get(0)).getAnnotation(Command.class).triggers()));
                        config.setDisabledCommands(disabledCmds);
                        config.save();
                        CommandUtil.sendReply(cmd.getRawEvent(), CommandUtil.getTemplateEmbed().setColor(Color.GREEN).setTitle("Enabled command " + cmd.getArgs().get(0)));
                    } else
                        CommandUtil.sendUserError(cmd.getRawEvent(), "Unknown command!", "");
                }
            } else
                CommandUtil.sendUserError(cmd.getRawEvent(), "You don't have permission!", "");
        } else
            CommandUtil.sendUserError(cmd.getRawEvent(), "Not in a server!", "");
    }
}
