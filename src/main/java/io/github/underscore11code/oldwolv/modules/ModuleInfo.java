package io.github.underscore11code.oldwolv.modules;

import io.github.underscore11code.oldwolv.OldWolv;
import io.github.underscore11code.oldwolv.util.CommandUtil;
import io.github.underscore11code.oldwolv.util.PrettyUtil;
import io.github.underscore11code.oldwolv.util.VersionUtil;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;

public class ModuleInfo {
    @Command(triggers = {"help", "h", "?"}, args = "", helpMsg = "Displays commands")
    public static void commandHelp(CommandInfo cmd) {
        EmbedBuilder embed = CommandUtil.getTemplateEmbed();
        embed.setDescription("`<>`: required, `[]`: optional, `|`: or");
        for (Command command : OldWolv.getCommandManager().getAnnotationList()) {
            embed.addField(command.triggers()[0] + " " + PrettyUtil.prettyArray(command.args()), command.helpMsg() + "\nAliases: " + PrettyUtil.prettyArray(command.triggers(), ","));
        }
        embed.setColor(Color.GRAY);
        embed.setTitle("Help");
        CommandUtil.sendReply(cmd.getRawEvent(), embed);
    }

    @Command(triggers = {"ping", "p"}, args = "", helpMsg = "Sees how quickly the bot can respond")
    public static void commandPing(CommandInfo cmd) {
        CommandUtil.sendReply(cmd.getRawEvent(), CommandUtil.getTemplateEmbed().setTitle("Pong!"));
    }

    @Command(triggers = "info", args = "", helpMsg = "Prints info about the bot")
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
}
