package io.github.underscore11code.oldwolv.modules;

import io.github.underscore11code.oldwolv.OldWolv;
import io.github.underscore11code.oldwolv.util.CommandUtil;
import io.github.underscore11code.oldwolv.util.PrettyUtil;
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
}
