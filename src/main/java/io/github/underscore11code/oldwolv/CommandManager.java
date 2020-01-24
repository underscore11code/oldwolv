package io.github.underscore11code.oldwolv;

import io.github.underscore11code.oldwolv.config.GuildConfig;
import io.github.underscore11code.oldwolv.messages.MessageType;
import io.github.underscore11code.oldwolv.messages.SimpleMessage;
import io.github.underscore11code.oldwolv.modules.*;
import io.github.underscore11code.oldwolv.util.CommandUtil;
import io.github.underscore11code.oldwolv.util.PrettyUtil;
import lombok.Getter;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CommandManager {
    @Getter private ArrayList<Command> annotationList = new ArrayList<>();
    @Getter private HashMap<String, Method> commands = new HashMap<>();

    public CommandManager() {
        Class<?>[] moduleClasses = {ModuleInfo.class, ModuleLookup.class, ModuleVerify.class, ModuleConfig.class};

        for (Class<?> clazz : moduleClasses) {
            System.out.printf("Initializing module %s\n", clazz.getName());
            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(Command.class))
                    continue;
                if (method.getParameterCount() != 1) {
                    System.out.printf("  Method %s#%s is annotated as Command but does not have a parameters\n", clazz.getName(), method.getName());
                    continue;
                }
                if (method.getParameters()[0].getType() != CommandInfo.class) {
                    System.out.printf("  Method %s#%s is annotated as Command but parameter 0 != CommandInfo\n", clazz.getName(), method.getName());
                    continue;
                }
                Command command = method.getAnnotation(Command.class);
                annotationList.add(command);
                for (String trigger : command.triggers())
                    commands.put(trigger, method);
                System.out.printf("  Method %s#%s enabled\n", clazz.getName(), method.getName());
            }
        }
    }

    public void processCommand(MessageCreateEvent event) {
        Message message = event.getMessage();
        String content = message.getContent();
        if (!content.startsWith(OldWolv.getPrefix()))
            return;
        if (message.getAuthor().isWebhook())
            return;
        String[] cmd = content.substring(OldWolv.getPrefix().length()).split(" ");
        Method method = commands.get(cmd[0].toLowerCase());
        if (method != null) {
            CommandInfo cmdInfo = new CommandInfo(cmd[0], Arrays.copyOfRange(cmd, 1, cmd.length), event);
            if (cmdInfo.getServer().isPresent())
                if (GuildConfig.get(cmdInfo.getServer().get().getIdAsString()).getDisabledCommands().contains(cmdInfo.getLabel()) && !cmdInfo.isServerAdmin())
                    CommandUtil.sendUserError(event, "That command is disabled in this server!", "");
                else
            try {
                method.invoke(null, cmdInfo);
            } catch (IllegalAccessException e) {
                System.out.printf("Could not run command %s because it's declaring method (%s) is not public", cmd[0], commands.get(cmd[0]).getName());
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                CommandUtil.sendReply(cmdInfo.getRawEvent(), new SimpleMessage(MessageType.ERROR, "Exception thrown while running command: " + e.getCause().getMessage(), ""));
                e.printStackTrace();
            }
        }
    }
}
