package io.github.underscore11code.oldwolv;

import io.github.underscore11code.oldwolv.util.CommandUtil;
import lombok.AccessLevel;
import lombok.Getter;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.user.User;

import java.awt.*;
import java.util.Scanner;

public class OldWolv {
    @Getter(AccessLevel.PUBLIC) private static DiscordApi api;
    @Getter(AccessLevel.PUBLIC) private static String version = "1.0.0-SNAPSHOT";
    @Getter(AccessLevel.PUBLIC) private static CommandManager commandManager;
    @Getter(AccessLevel.PUBLIC) private static String prefix = "$$";
    @Getter(AccessLevel.PUBLIC) private static User owner;
    private static String onofflogchannelid = "666088790372253716";

    public static void main(String[] args) {
        System.out.println("Logging in...");
        api = new DiscordApiBuilder().setToken(args[0]).login().join();
        System.out.printf("Logged in to %s\n", api.getYourself().getDiscriminatedName());
        System.out.println("Initializing Commands...");
        commandManager = new CommandManager();
        System.out.println("Done!");
        owner = getApi().getOwner().join();
        System.out.printf("You may invite the bot with:\n%s (no perms)\n%s (Admin)\n", getApi().createBotInvite(), getApi().createBotInvite(Permissions.fromBitmask(8)));
        getApi().updateActivity(ActivityType.WATCHING, getPrefix() + "help | v" + getVersion());
        getApi().addMessageCreateListener(event -> getCommandManager().processCommand(event));

        getApi().getTextChannelById(onofflogchannelid).ifPresent(channel -> new MessageBuilder().setEmbed(CommandUtil.getTemplateEmbed().setTitle("Bot online!").setColor(Color.GREEN)).send(channel));
        long starttime = System.currentTimeMillis();

        Scanner in = new Scanner(System.in);
        while (true) {
            String cmd = in.next();
            if (cmd.equalsIgnoreCase("stop")) {
                long uptime = System.currentTimeMillis() - starttime;
                stop();
            }
        }
    }

    public static void stop() {
        getApi().getTextChannelById(onofflogchannelid).ifPresent(channel -> new MessageBuilder().setEmbed(CommandUtil.getTemplateEmbed().setTitle("Bot offline!").setColor(Color.RED)).send(channel));
        getApi().disconnect();
        System.exit(0);
    }
}
