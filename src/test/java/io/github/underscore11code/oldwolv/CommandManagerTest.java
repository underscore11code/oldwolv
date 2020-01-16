package io.github.underscore11code.oldwolv;

import io.github.underscore11code.oldwolv.modules.Command;
import io.github.underscore11code.oldwolv.modules.CommandInfo;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class CommandManagerTest {
    @Test
    public void validCalls() {
        for (Class<?> clazz : CommandManager.getModuleClasses()) {
            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(Command.class)) {
                    continue;
                }
                if (!Modifier.isStatic(method.getModifiers())) {
                    fail(String.format("%s#%s() is not static", clazz.getName(), method.getName()));
                    continue;
                }
                if (!Modifier.isPublic(method.getModifiers())) {
                    fail(String.format("%s#%s() is not public", clazz.getName(), method.getName()));
                    continue;
                }
                if (method.getParameterCount() != 1) {
                    fail(String.format("%s#%s()'s param count != 1", clazz.getName(), method.getName()));
                    continue;
                }
                if (method.getParameters()[0].getType() != CommandInfo.class) {
                    fail(String.format("%s#%s()'s param type != io.github.underscore11code.oldwolv.modules.CommandInfo", clazz.getName(), method.getName()));
                    continue;
                }
            }
        }
    }
}