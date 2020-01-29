package io.github.underscore11code.oldwolv.modules;

import io.github.underscore11code.oldwolv.OldWolv;
import io.github.underscore11code.oldwolv.config.GuildConfig;
import io.github.underscore11code.oldwolv.util.CommandUtil;
import io.github.underscore11code.oldwolv.util.PermissionUtil;
import lombok.Getter;
import lombok.Setter;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Role;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class ModuleConfig implements Module {
    @Getter String name = "Config";
    @Getter String description = "Commands for getting and setting config options";
    @Getter @Setter ArrayList<Command> commands = new ArrayList<>();

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

    @Command(triggers = "disabled", args = "", helpMsg = "Lists all disabled commands in the server")
    public static void commandDisabled(CommandInfo cmd) {
        if (cmd.getServer().isPresent()) {
            String out = "";
            for (String disabledCommand : GuildConfig.get(cmd.getServer().get().getIdAsString()).getDisabledCommands()) {
                out = out + disabledCommand + "\n";
            }
            EmbedBuilder embed = CommandUtil.getTemplateEmbed();
            embed.setTitle("Disabled commands in " + cmd.getServer().get().getName());
            embed.setDescription(out);
            embed.setColor(Color.BLUE);
            CommandUtil.sendReply(cmd.getRawEvent(), embed);
        } else
            CommandUtil.sendUserError(cmd.getRawEvent(), "Not in a server!", "");
    }

    @Command(triggers = "addstaff", args = "<command>", helpMsg = "Adds a role as staff")
    public static void commandAddStaff(CommandInfo cmd) {
        if (cmd.getServer().isPresent()) {
            if (cmd.isServerAdmin()) {
                if (cmd.getArgs().size() != 1)
                    CommandUtil.sendUserError(cmd.getRawEvent(), "Expected 1 argument, received " + cmd.getArgs().size(), "Stynax: `addstaff <command>`");
                else {
                    String id = cmd.getArgs().get(0).replace("<@&", "").replace(">", "");
                    if (cmd.getServer().get().getRoleById(id).isPresent()) {
                        GuildConfig config = GuildConfig.get(cmd.getServer().get().getIdAsString());
                        ArrayList<String> staffRoles = config.getStaffRoleIds();
                        staffRoles.add(id);
                        config.setStaffRoleIds(staffRoles);
                        config.save();
                        CommandUtil.sendReply(cmd.getRawEvent(), CommandUtil.getTemplateEmbed().setColor(Color.GREEN).setTitle("Added staff role " + cmd.getArgs().get(0)));
                    } else
                        CommandUtil.sendUserError(cmd.getRawEvent(), "Unknown Role!", "");
                }
            } else
                CommandUtil.sendUserError(cmd.getRawEvent(), "You don't have permission!", "");
        } else
            CommandUtil.sendUserError(cmd.getRawEvent(), "Not in a server!", "");
    }

    @Command(triggers = {"remstaff", "removestaff"}, args = "<command>", helpMsg = "Removes a role as staff")
    public static void commandRemStaff(CommandInfo cmd) {
        if (cmd.getServer().isPresent()) {
            if (cmd.isServerAdmin()) {
                if (cmd.getArgs().size() != 1)
                    CommandUtil.sendUserError(cmd.getRawEvent(), "Expected 1 argument, received " + cmd.getArgs().size(), "Stynax: `addstaff <command>`");
                else {
                    String id = cmd.getArgs().get(0).replace("<@&", "").replace(">", "");
                    GuildConfig config = GuildConfig.get(cmd.getServer().get().getIdAsString());
                    if (config.getStaffRoleIds().contains(id)) {
                        ArrayList<String> staffRoles = config.getStaffRoleIds();
                        staffRoles.remove(id);
                        config.setStaffRoleIds(staffRoles);
                        config.save();
                        CommandUtil.sendReply(cmd.getRawEvent(), CommandUtil.getTemplateEmbed().setColor(Color.GREEN).setTitle("Removed staff role " + cmd.getArgs().get(0)));
                    } else
                        CommandUtil.sendUserError(cmd.getRawEvent(), "Unknown Role!", "");
                }
            } else
                CommandUtil.sendUserError(cmd.getRawEvent(), "You don't have permission!", "");
        } else
            CommandUtil.sendUserError(cmd.getRawEvent(), "Not in a server!", "");
    }

    @Command(triggers = "staff", args = "", helpMsg = "Lists all staff roles")
    public static void commandStaffRoles(CommandInfo cmd) {
        if (cmd.getServer().isPresent()) {
            String manuallyAdded = "";
            for (String staffRole : GuildConfig.get(cmd.getServer().get().getIdAsString()).getStaffRoleIds()) {
                manuallyAdded = manuallyAdded + "<@&" + staffRole + ">\n";
            }
            String roles = "";
            for (Role role : cmd.getServer().get().getRoles()) {
                boolean isStaff = false;
                for (PermissionType allowedPermission : role.getAllowedPermissions()) {
                    for (PermissionType staffPerm : PermissionUtil.getStaffPerms()) {
                        if (allowedPermission.equals(staffPerm)) {
                            isStaff = true;
                            break;
                        }
                    }
                }
                if (isStaff)
                    roles = roles + role.getMentionTag() + "\n";
            }
            EmbedBuilder embed = CommandUtil.getTemplateEmbed();
            embed.setTitle("Staff roles in " + cmd.getServer().get().getName());
            System.out.println("Characters in desc: " + roles.length());
            embed.setDescription(roles);
            if (manuallyAdded != "")
                embed.addField("Manually added", manuallyAdded);
            embed.setColor(Color.BLUE);
            CommandUtil.sendReply(cmd.getRawEvent(), embed);
        } else
            CommandUtil.sendUserError(cmd.getRawEvent(), "Not in a server!", "");
    }

    @Command(triggers = {"setverifiedrole"}, args = "<role>", helpMsg = "Sets the role to give when verified")
    public static void commandSetVerified(CommandInfo cmd) {
        if (cmd.getServer().isPresent()) {
            if (cmd.isServerAdmin()) {
                if (cmd.getArgs().size() != 1) {
                    GuildConfig config = GuildConfig.get(cmd.getServer().get().getIdAsString());
                    config.setVerifiedRoleId("");
                    config.save();
                    CommandUtil.sendReply(cmd.getRawEvent(), CommandUtil.getTemplateEmbed().setColor(Color.GREEN).setTitle("Unset Verified role!").setDescription("Verifications will now use a role named `Verified`, if present"));
                } else {
                    String id = cmd.getArgs().get(0).replace("<@&", "").replace(">", "");
                    if (cmd.getServer().get().getRoleById(id).isPresent()) {
                        GuildConfig config = GuildConfig.get(cmd.getServer().get().getIdAsString());
                        config.setVerifiedRoleId(id);
                        config.save();
                        CommandUtil.sendReply(cmd.getRawEvent(), CommandUtil.getTemplateEmbed().setColor(Color.GREEN).setTitle("Set verified role to " + cmd.getServer().get().getRoleById(id).get().getName()));
                    } else
                        CommandUtil.sendUserError(cmd.getRawEvent(), "Unknown Role!", "");
                }
            } else
                CommandUtil.sendUserError(cmd.getRawEvent(), "You don't have permission!", "");
        } else
            CommandUtil.sendUserError(cmd.getRawEvent(), "Not in a server!", "");
    }
}
