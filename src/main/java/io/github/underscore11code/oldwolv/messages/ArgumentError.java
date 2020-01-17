package io.github.underscore11code.oldwolv.messages;

import io.github.underscore11code.oldwolv.util.PrettyUtil;
import lombok.Getter;

import java.util.ArrayList;

public class ArgumentError implements Message {
    @Getter MessageType messageType = MessageType.ERROR;
    @Getter String title = "Argument Error";
    @Getter String description = "Expected %s args (%s), received %s (%s)";

    public ArgumentError(String[] expectedArgs, String[] receivedArgs) {
        this.description = String.format(description, expectedArgs.length, PrettyUtil.prettyArray(expectedArgs), receivedArgs.length, receivedArgs == null ? "Null" : PrettyUtil.prettyArray(receivedArgs));
    }

    public ArgumentError(String[] expectedArgs, ArrayList<String > receivedArgs) {
        this.description = String.format(description, expectedArgs.length, PrettyUtil.prettyArray(expectedArgs), receivedArgs.size(), receivedArgs == null ? "Null" : PrettyUtil.prettyArrayList(receivedArgs));
    }
}
