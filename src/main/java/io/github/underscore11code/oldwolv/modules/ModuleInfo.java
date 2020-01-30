package io.github.underscore11code.oldwolv.modules;

import io.github.underscore11code.oldwolv.OldWolv;
import io.github.underscore11code.oldwolv.util.CommandUtil;
import io.github.underscore11code.oldwolv.util.PrettyUtil;
import io.github.underscore11code.oldwolv.util.VersionUtil;
import lombok.Getter;
import lombok.Setter;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Permissions;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ModuleInfo implements Module {
    @Getter String name = "About";
    @Getter String description = "General info about the bot";
    @Getter @Setter ArrayList<Command> commands = new ArrayList<>();

    @Command(triggers = {"help", "h", "?"}, args = "[module|command]", helpMsg = "Displays commands")
    public static void commandHelp(CommandInfo cmd) {
        EmbedBuilder embed = CommandUtil.getTemplateEmbed();
        if (cmd.getArgs().size() == 0) {
            String desc = "<required> [optional] | or\n";
            for (Module module : OldWolv.getCommandManager().getModules().values()) {
                desc = desc + "`" + module.getName() + "`: " + module.getDescription() + "\n";
            }
            embed.setDescription(desc + "\n\n`" + OldWolv.getPrefix() + "help [module]` for more information");
            embed.setTitle(OldWolv.getApi().getYourself().getName() + " Help: Modules");
        } else {
            String query = cmd.getArgs().get(0);
            embed.setDescription("<required> [optional] | or");
            embed.setTitle(OldWolv.getApi().getYourself().getName() + " Help: " + query);
            if (OldWolv.getCommandManager().getModules().containsKey(query)) {
                Module module = OldWolv.getCommandManager().getModules().get(query);
                String moduleDesc = "";
                for (Command command : module.getCommands()) {
                    moduleDesc = moduleDesc + "`" + PrettyUtil.prettyArray(command.triggers(), "|") + (!(command.args()[0].equals("@null")) ? " " + PrettyUtil.prettyArray(command.args()) : "") + "` " + command.helpMsg() + "\n";
                }
                embed.addField("Module: " + module.getName(), moduleDesc);
            }
            if (OldWolv.getCommandManager().getCommands().containsKey(query)) {
                String commandDesc = "";
                for (Command command : OldWolv.getCommandManager().getAnnotations()) {
                    for (String trigger : command.triggers()) {
                        if (trigger.equals(query.toLowerCase())) {
                            commandDesc = commandDesc + "`" + PrettyUtil.prettyArray(command.triggers(), "|") + (!(command.args()[0].equals("@null")) ? " " + PrettyUtil.prettyArray(command.args()) : "") + "` " + command.helpMsg() + "\n";
                            embed.addField("Command: " + command.triggers()[0], commandDesc);
                        }
                    }
                }
            }
        }
        embed.setColor(Color.GRAY);
        CommandUtil.sendReply(cmd.getRawEvent(), embed);
    }

    @Command(triggers = {"ping", "p"}, args = "@null", helpMsg = "Sees how quickly the bot can respond")
    public static void commandPing(CommandInfo cmd) {
        CommandUtil.sendReply(cmd.getRawEvent(), CommandUtil.getTemplateEmbed().setTitle("Pong!"));
    }

    @Command(triggers = "info", args = "@null", helpMsg = "Prints info about the bot")
    public static void commandInfo(CommandInfo cmd) {
        EmbedBuilder embed = CommandUtil.getTemplateEmbed();
        embed.setTitle(OldWolv.getApi().getYourself().getName());
        embed.setDescription("A discord bot made by Underscore11");
        embed.setThumbnail(OldWolv.getApi().getYourself().getAvatar());
        embed.addField("Github", "[underscore11code/oldwolv](https://github.com/underscore11code/oldwolv)");
        embed.addField("Libraries used", "[Javacord](https://javacord.org/) - Discord API\n[Gson](https://github.com/google/gson) - Configuration saving and loading");
        embed.addField("Version", OldWolv.getVersion() + "\nCommit " + VersionUtil.get("git.commit.id") + "\nBranch " + VersionUtil.get("git.branch"));
        embed.setColor(Color.BLUE);
        CommandUtil.sendReply(cmd.getRawEvent(), embed);
    }

    @Command(triggers = "invite", args = "@null", helpMsg = "Gives a invite link for the bot")
    public static void commandInvite(CommandInfo cmd) {
        EmbedBuilder embed = CommandUtil.getTemplateEmbed();
        embed.setTitle(OldWolv.getApi().getYourself().getName() + " invite");
        embed.setDescription("[Click Me!](" + OldWolv.getApi().createBotInvite(Permissions.fromBitmask(8)) + ")");
        CommandUtil.sendReply(cmd.getRawEvent(), embed);
    }
}
