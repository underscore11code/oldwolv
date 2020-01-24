package io.github.underscore11code.oldwolv.modules;

import io.github.underscore11code.oldwolv.OldWolv;
import io.github.underscore11code.oldwolv.util.CommandUtil;
import io.github.underscore11code.oldwolv.util.EmojiUtil;
import io.github.underscore11code.oldwolv.util.PermissionUtil;
import io.github.underscore11code.oldwolv.util.PrettyUtil;
import lombok.Getter;
import lombok.Setter;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;
import java.util.ArrayList;

public class ModuleLookup implements Module{
    @Getter String name = "Lookup";
    @Getter String description = "Utilities to lock up various Discord entities";
    @Getter @Setter ArrayList<Command> commands = new ArrayList<>();

    @Command(triggers = {"lookup", "lu"},
            args = "<server|channel|user|invite|role> [id]",
            helpMsg = "Looks up the given [id] of <type>\nDefaults to current channel, server, and the user to trigger the command")
    public static void commandLookup(CommandInfo cmd) {
        System.out.printf("Lookup command running with args \"%s\" (length of %s)\n", PrettyUtil.prettyArrayList(cmd.getArgs(), ", "), cmd.getArgs().size());
        if (cmd.getArgs().size() == 0) {
            CommandUtil.sendUserError(cmd.getRawEvent(), "Not enough args!", "Stynax: `lookup <server|channel|user|invite|role> [id]`");
            return;
        }

        if (cmd.getArgs().get(0).equalsIgnoreCase("server") || cmd.getArgs().get(0).equalsIgnoreCase("s")) {
            if (cmd.getArgs().size() == 2) {
                if (cmd.isBotStaff())
                    lookupServer(cmd.getArgs().get(1), cmd.getRawEvent());
                else
                    CommandUtil.sendUserError(cmd.getRawEvent(), "You may only look up your own server", "");
            } else if (cmd.getServer().isPresent())
                cmd.getServer().ifPresent(server -> lookupServer(server, cmd.getRawEvent()));
            else
                CommandUtil.sendUserError(cmd.getRawEvent(), "Not in a server", "This command may only be ran in a server");
        } else if (cmd.getArgs().get(0).equalsIgnoreCase("channel") || cmd.getArgs().get(0).equalsIgnoreCase("c")) {
            if (cmd.getArgs().size() == 2)
                lookupChannel(cmd.getArgs().get(1).replace("<#", "").replace(">", ""), cmd.getRawEvent(), cmd);
            else
                lookupChannel(cmd.getChannel().getIdAsString(), cmd.getRawEvent(), cmd);
        } else if (cmd.getArgs().get(0).equalsIgnoreCase("user") || cmd.getArgs().get(0).equalsIgnoreCase("u")) {
            if (cmd.getArgs().size() == 2)
                lookupUser(cmd.getArgs().get(1).replace("<@", "").replace(">", "").replace("!", ""), cmd.getRawEvent());
            else
                lookupUser(cmd.getRawEvent().getMessageAuthor().getIdAsString(), cmd.getRawEvent());
        } else if (cmd.getArgs().get(0).equalsIgnoreCase("invite") || cmd.getArgs().get(0).equalsIgnoreCase("i")) {
            if (cmd.getArgs().size() == 2)
                lookupInvite(cmd.getArgs().get(1).replace("https://discord.gg/", ""), cmd.getRawEvent());
            else
                CommandUtil.sendUserError(cmd.getRawEvent(), "No ID given!", "I need an ID to look up an invite");
        } else if (cmd.getArgs().get(0).equalsIgnoreCase("role") || cmd.getArgs().get(0).equalsIgnoreCase("r")) {
            if (cmd.getArgs().size() == 2)
                lookupRole(cmd.getArgs().get(1).replace("<&", "").replace(">", ""), cmd.getRawEvent());
            else
                CommandUtil.sendUserError(cmd.getRawEvent(), "No ID given!", "I need an ID to look up an role");
        } else {
            CommandUtil.sendUserError(cmd.getRawEvent(), "Unknown Selector", "Stynax: `lookup <server|channel|user|invite|role> [id]`");
        }
    }

    private static void lookupServer(String id, MessageCreateEvent rawEvent) {
        if (OldWolv.getApi().getServerById(id).isPresent())
            OldWolv.getApi().getServerById(id).ifPresent(server -> lookupServer(server, rawEvent));
         else
             CommandUtil.sendUserError(rawEvent, "Could not find server", "The bot can only look up servers it is a member of.\nThe command needs an ID, **not** the name.");
    }

    private static void lookupServer(Server server, MessageCreateEvent rawEvent) {

        EmbedBuilder embed = CommandUtil.getTemplateEmbed();
        embed.setTitle("Server Lookup: " + server.getName());
        embed.addField(EmojiUtil.OWNER.getCode() + " Owner", server.getOwner().getMentionTag(), true);
        embed.addField(":globe_with_meridians: Region", server.getRegion().getName(), true);
        embed.addField(EmojiUtil.MENTION.getCode() + " Roles", String.valueOf(server.getRoles().size()), true);
        embed.addField(EmojiUtil.CHANNEL.getCode() + " Channels", String.valueOf(server.getChannels().size()), true);
        embed.addField(EmojiUtil.MEMBERS.getCode() + " Members", String.valueOf(server.getMemberCount()), true);
        embed.addField(":1234: ID", server.getIdAsString(), true);
        server.getIcon().ifPresent(embed::setThumbnail);
        embed.setColor(Color.ORANGE);
        CommandUtil.sendReply(rawEvent, embed);
    }

    private static void lookupChannel(String id, MessageCreateEvent rawEvent, CommandInfo cmd) {
        if (OldWolv.getApi().getChannelById(id).isPresent())
            OldWolv.getApi().getChannelById(id).ifPresent(channel -> {
                EmbedBuilder embed = CommandUtil.getTemplateEmbed();
                embed.setColor(Color.ORANGE);
                embed.addField(":1234: ID", channel.getIdAsString(), true);
                embed.addField("Type", PrettyUtil.prettyEnum(channel.getType().name()), true);
                if (channel.asServerChannel().isPresent()) {
                    if (!(channel.asServerChannel().get().canSee(cmd.getSender()) || cmd.isBotStaff())) {
                        CommandUtil.sendUserError(rawEvent, "You can only look up channels you can see", "");
                    }
                }
                switch (channel.getType()) {
                    case SERVER_TEXT_CHANNEL:
                    case SERVER_NEWS_CHANNEL:
                    case SERVER_STORE_CHANNEL:
                        channel.asServerTextChannel().ifPresent(serverTextChannel -> {
                            embed.setTitle("Channel Lookup: " + EmojiUtil.CHANNEL.getCode() + serverTextChannel.getName());
                            embed.addField(EmojiUtil.SLOWDOWN.getCode() + " Slowdown", PrettyUtil.notNullString(String.valueOf(serverTextChannel.getSlowmodeDelayInSeconds()), "None"), true);
                            embed.addField(EmojiUtil.CHANNEL_NSFW.getCode() + " NSFW", PrettyUtil.prettyBoolean(serverTextChannel.isNsfw()), true);
                            serverTextChannel.getCategory().ifPresent(category -> embed.addField("Category", category.getName(), true));
                            embed.addField("Topic", PrettyUtil.notNullString(serverTextChannel.getTopic()));
                        });
                        break;
                    case SERVER_VOICE_CHANNEL:
                        channel.asServerVoiceChannel().ifPresent(serverVoiceChannel -> {
                            embed.setTitle("Channel Lookup: " + EmojiUtil.VOICE.getCode() + serverVoiceChannel.getName());
                            embed.addField(":fast_forward: Bitrate", String.valueOf(serverVoiceChannel.getBitrate()), true);
                            if (serverVoiceChannel.getUserLimit().isPresent())
                                embed.addField(EmojiUtil.MEMBERS.getCode() + " Connected / Max users", serverVoiceChannel.getConnectedUsers().size() + " / " + serverVoiceChannel.getUserLimit().get(), true);
                            else
                                embed.addField(EmojiUtil.MEMBERS.getCode() + " Connected users", String.valueOf(serverVoiceChannel.getConnectedUsers().size()), true);
                            serverVoiceChannel.getCategory().ifPresent(category -> embed.addField("Category", category.getName(), true));
                        });
                        break;
                    case CHANNEL_CATEGORY:
                        channel.asChannelCategory().ifPresent(category -> {
                            embed.setTitle("Channel Lookup: " + category.getName());
                        });
                        break;
                }
                CommandUtil.sendReply(rawEvent, embed);
            });
        else
            CommandUtil.sendUserError(rawEvent, "Could not find channel", "");
    }

    private static void lookupUser(String id, MessageCreateEvent rawEvent) {
        OldWolv.getApi().getUserById(id).thenAcceptAsync(user -> {
            EmbedBuilder embed = CommandUtil.getTemplateEmbed();
            embed.setTitle("User Lookup: " + user.getDiscriminatedName());
            embed.setColor(Color.ORANGE);
            embed.setThumbnail(user.getAvatar());
            embed.addField(EmojiUtil.MENTION.getCode() + " Mention", user.getMentionTag(), true);
            user.getActivity().ifPresent(activity -> embed.addField(":video_game: Activity", activity.getType().toString() + ": " + activity.getName(), true));
            embed.addField(":1234: ID", user.getIdAsString(), true);
            String description = "";
            if (PermissionUtil.isBotOwner(user))
                description = description + EmojiUtil.BOTTAG.getCode() + EmojiUtil.OWNER.getCode() + " Bot Owner\n";
            if (PermissionUtil.isBotStaff(user))
                description = description + EmojiUtil.BOTTAG.getCode() + EmojiUtil.STAFF.getCode() + " Bot Staff\n";
            if (rawEvent.getServer().isPresent()) {
                if (PermissionUtil.isServerOwner(user, rawEvent.getServer().get()))
                    description = description + EmojiUtil.OWNER.getCode() + " Server Owner\n";
                if (PermissionUtil.isServerAdmin(user, rawEvent.getServer().get()))
                    description = description + EmojiUtil.STAFF.getCode() + " Server Admin\n";
                if (PermissionUtil.isServerStaff(user, rawEvent.getServer().get()))
                    description = description + EmojiUtil.STAFF.getCode() + " Server Staff\n";
            }
            embed.setDescription(description);

            rawEvent.getServer().ifPresent(server -> {
                embed.addField("Server-specific", "-=-=-=-=-=-");
                String roles = "";
                for (Role role : user.getRoles(server)) {
                    roles = roles + role.getMentionTag() + " (" + role.getIdAsString() + ")\n";
                }
                embed.addField(EmojiUtil.MENTION.getCode() + " Roles", roles);
                user.getNickname(server).ifPresent(nickname -> embed.addField("Nickname", nickname, true));
                user.getRoleColor(server).ifPresent(color -> embed.addField("Color", "R=" + color.getRed() + " G=" + color.getGreen() + " B=" + color.getBlue(), true));
            });

            CommandUtil.sendReply(rawEvent, embed);
        });
    }

    private static void lookupInvite(String id, MessageCreateEvent rawEvent) {
        OldWolv.getApi().getInviteByCode(id).thenAcceptAsync(invite -> {
            EmbedBuilder embed = CommandUtil.getTemplateEmbed();
            embed.setTitle("Invite Lookup: " + invite.getServerName());
            invite.getServerIcon().ifPresent(embed::setThumbnail);
            invite.getServerSplash().ifPresent(embed::setImage);
            invite.getApproximatePresenceCount().ifPresent(presence -> {
                invite.getApproximateMemberCount().ifPresent(members -> {
                    embed.addField("Approx Online/Total Members", presence + "/" + members, true);
                });
            });
            embed.addField("Landing Channel", invite.getChannelName(), true);
            embed.addField("URL", invite.getUrl().toString(), true);
            embed.setColor(Color.ORANGE);
            CommandUtil.sendReply(rawEvent, embed);
        });
    }

    private static void lookupRole(String id, MessageCreateEvent rawEvent) {
        OldWolv.getApi().getRoleById(id).ifPresent(role -> {
            EmbedBuilder embed = CommandUtil.getTemplateEmbed();
            embed.setTitle("Role Lookup: " + role.getName());
            embed.setColor(Color.ORANGE);
            embed.addField(":1234: ID", role.getIdAsString(), true);
            role.getColor().ifPresent(color -> embed.addField("Color", "R=" + color.getRed() + " G=" + color.getGreen() + " B=" + color.getBlue(), true));
            embed.addField("Hoisted", PrettyUtil.prettyBoolean(role.isDisplayedSeparately()), true);
            embed.addField("Mentionable", PrettyUtil.prettyBoolean(role.isMentionable()), true);
            String allowed = "";
            for (PermissionType perm : role.getAllowedPermissions())
                allowed = allowed + PrettyUtil.prettyEnum(perm.name()) + "\n";
            embed.addField("Permissions", allowed);
            CommandUtil.sendReply(rawEvent, embed);
        });
        if (!OldWolv.getApi().getRoleById(id).isPresent())
            CommandUtil.sendUserError(rawEvent, "Could not find role!", "Make sure it is in the server!");
    }





    @Command(triggers = {"list", "ls"}, args = "<server|channel|role>", helpMsg = "Lists all <server|channel|role>s the bot has access to")
    public static void commandList(CommandInfo cmd) {
        if (cmd.getArgs().size() == 1) {
            switch (cmd.getArgs().get(0)) {
                case "servers":
                case "s":
                    if (cmd.isBotStaff()) {
                        EmbedBuilder embed = CommandUtil.getTemplateEmbed();
                        embed.setColor(Color.ORANGE);
                        embed.setTitle("Server List");
                        for (Server server : OldWolv.getApi().getServers()) {
                            embed.addField(server.getName(), "ID: " + server.getIdAsString() + "\nMembers: " + server.getMemberCount());
                        }
                        CommandUtil.sendReply(cmd.getRawEvent(), embed);
                    } else
                        CommandUtil.sendUserError(cmd.getRawEvent(), "Only Bot staff may list servers", "");
                    break;
                case "channels":
                case "c":
                    if (cmd.getServer().isPresent()) {
                        EmbedBuilder embed = CommandUtil.getTemplateEmbed().setTitle("Channel List").setColor(Color.ORANGE);
                        for (ServerChannel channel : cmd.getServer().get().getChannels()) {
                            if (channel.canSee(cmd.getSender()) || cmd.isServerAdmin()) {
                                switch (channel.getType()) {
                                    case CHANNEL_CATEGORY:
                                        //noinspection OptionalGetWithoutIsPresent
                                        embed.addField("**" + channel.asChannelCategory().get().getName() + "**", channel.getIdAsString());
                                        break;
                                    case SERVER_TEXT_CHANNEL:
                                    case SERVER_STORE_CHANNEL:
                                    case SERVER_NEWS_CHANNEL:
                                        ServerTextChannel serverTextChannel = channel.asServerTextChannel().get();
                                        if (serverTextChannel.isNsfw())
                                            embed.addField(EmojiUtil.CHANNEL_NSFW.getCode() + " " + serverTextChannel.getName(), serverTextChannel.getIdAsString(), true);
                                        else if (serverTextChannel.canSee(cmd.getSender()))
                                            embed.addField(EmojiUtil.CHANNEL.getCode() + " " + serverTextChannel.getName(), serverTextChannel.getIdAsString(), true);
                                        else
                                            embed.addField(EmojiUtil.CHANNEL_LOCKED.getCode() + " " + serverTextChannel.getName(), serverTextChannel.getIdAsString(), true);
                                        break;
                                    case SERVER_VOICE_CHANNEL:
                                        ServerVoiceChannel serverVoiceChannel = channel.asServerVoiceChannel().get();
                                        if (serverVoiceChannel.canSee(cmd.getSender()))
                                            embed.addField(EmojiUtil.VOICE.getCode() + " " + serverVoiceChannel.getName(), serverVoiceChannel.getIdAsString(), true);
                                        else
                                            embed.addField(EmojiUtil.VOICE_LOCKED.getCode() + " " + serverVoiceChannel.getName(), serverVoiceChannel.getIdAsString(), true);

                                }
                            }
                        }
                        CommandUtil.sendReply(cmd.getRawEvent(), embed);
                    } else
                        CommandUtil.sendUserError(cmd.getRawEvent(), "Not in a server", "");
                    break;
                case "roles":
                case "r":
                    if (cmd.getServer().isPresent()) {
                        EmbedBuilder embed = CommandUtil.getTemplateEmbed().setTitle("Role List").setColor(Color.ORANGE);
                        for (Role role : cmd.getServer().get().getRoles()) {
                            embed.addField(EmojiUtil.MENTION.getCode() + role.getName(), role.getIdAsString());
                        }
                        CommandUtil.sendReply(cmd.getRawEvent(), embed);
                    } else
                        CommandUtil.sendUserError(cmd.getRawEvent(), "Not in a server", "");
                    break;
            }
        } else
            CommandUtil.sendUserError(cmd.getRawEvent(), "No selector given!", "Stynax: `list <servers|channels|roles>");
    }
}
