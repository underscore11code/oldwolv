package io.github.underscore11code.oldwolv.modules;

import java.util.ArrayList;

public interface Module {
    String getName();

    String getDescription();

    ArrayList<Command> getCommands();

    void setCommands(ArrayList<Command> commands);
}
