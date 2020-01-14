package io.github.underscore11code.oldwolv.util;

import io.github.underscore11code.oldwolv.OldWolv;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class CommandUtil {
    public static CompletableFuture<Message> sendReply(MessageCreateEvent event, EmbedBuilder builder, String reaction) {
        event.getMessage().addReaction(reaction);
        return new MessageBuilder().setContent("<@" + event.getMessageAuthor().getIdAsString() + ">").setEmbed(builder).send(event.getChannel());
    }

    public static CompletableFuture<Message> sendReply(MessageCreateEvent event, EmbedBuilder builder) {
        return sendReply(event, builder, "\u2705");
    }

    public static EmbedBuilder getTemplateEmbed() {
        return new EmbedBuilder().setTimestampToNow().setFooter(OldWolv.getApi().getYourself().getName() + " v" + OldWolv.getVersion());
    }

    public static void sendUserError(MessageCreateEvent event, String title, String desc) {
        sendReply(event, getTemplateEmbed().setTitle(title).setDescription(desc).setColor(Color.RED), "\u274C");
    }
}
