package io.github.underscore11code.oldwolv.modules;

import io.github.underscore11code.oldwolv.util.PermissionUtil;
import io.github.underscore11code.oldwolv.util.PrettyUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.ArrayList;
import java.util.Optional;

public class CommandInfo {
    @Getter(AccessLevel.PUBLIC) private String label;
    @Getter(AccessLevel.PUBLIC) private ArrayList<String> args = new ArrayList<>();
    @Getter(AccessLevel.PUBLIC) private ArrayList<String> flags = new ArrayList<>();
    @Getter(AccessLevel.PUBLIC) private User sender;
    @Getter(AccessLevel.PUBLIC) private TextChannel channel;
    @Getter(AccessLevel.PUBLIC) private Optional<Server> server;
    @Getter(AccessLevel.PUBLIC) private MessageCreateEvent rawEvent;
    @Getter(AccessLevel.PUBLIC) private boolean botOwner;
    @Getter(AccessLevel.PUBLIC) private boolean botStaff;
    @Getter(AccessLevel.PUBLIC) private boolean serverOwner = false;
    @Getter(AccessLevel.PUBLIC) private boolean serverAdmin = false;
    @Getter(AccessLevel.PUBLIC) private boolean serverStaff = false;

    public CommandInfo(String label, String[] arguments, MessageCreateEvent rawEvent) {
        this.label = label.toLowerCase();
        this.rawEvent = rawEvent;
        //noinspection OptionalGetWithoutIsPresent
        this.sender = rawEvent.getMessageAuthor().asUser().get();
        this.channel = rawEvent.getChannel();
        this.server = rawEvent.getServer();
        this.botOwner = PermissionUtil.isBotOwner(this.sender);
        this.botStaff = PermissionUtil.isBotStaff(this.sender);
        this.server.ifPresent(server1 -> {
            this.serverOwner = PermissionUtil.isServerOwner(this.sender, server1);
            this.serverAdmin = PermissionUtil.isServerAdmin(this.sender, server1);
            this.serverStaff = PermissionUtil.isServerStaff(this.sender, server1);
        });
        for (String item : arguments) {
            if (item.startsWith("."))
                this.flags.add(item);
            else
                this.args.add(item);
        }
        if ((flags.contains("..overrideperms") || flags.contains(".op")) && this.botStaff) {
            this.serverStaff = true;
            this.serverAdmin = true;
            this.serverOwner = true;
        }

        System.out.printf("%s ran command %s with args {%s} and flags {%s} in %s\n(Permissions BO: %s, BS: %s, SO: %s, SA: %s, SS: %s)\n",
                this.sender.getMentionTag(), this.label, PrettyUtil.prettyArrayList(this.args, ", "), PrettyUtil.prettyArrayList(this.flags, ", "), this.channel.getId(),
                PrettyUtil.prettyBoolean(this.botOwner), PrettyUtil.prettyBoolean(this.botStaff), PrettyUtil.prettyBoolean(this.serverOwner), PrettyUtil.prettyBoolean(this.serverAdmin), PrettyUtil.prettyBoolean(this.botStaff));
    }
}
