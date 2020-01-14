package io.github.underscore11code.oldwolv.util;

import java.util.ArrayList;

public class PrettyUtil {
    public static String prettyArray(String[] args, String spacer) {
        String out = "";
        for (String string : args)
            out = out + string + spacer;
        if (out.length() != 0)
            out = out.substring(0, out.length()-spacer.length());
        return out;
    }

    public static String prettyArray(String[] args) {
        return prettyArray(args, " ");
    }

    public static String prettyBoolean(boolean bool) {
        if (bool)
            return "True";
        else
            return "False";
    }

    public static String prettyEnum(String name) {
        name = name.replace("_", " ").toLowerCase();
        String[] split = name.split(" ");
        String out = "";
        for (String item : split) {
            out = out + " " + item.replaceFirst(item.substring(0,1), item.substring(0,1).toUpperCase());
        }
        return out;
    }

    public static String notNullString(String text) {
        return notNullString(text, "Null");
    }

    public static String notNullString(String text, String ifnull) {
        if (text.equalsIgnoreCase(""))
            return ifnull;
        else
            return text;
    }

    public static String prettyArrayList(ArrayList<String> arrayList) {
        return prettyArrayList(arrayList, " ");
    }

    public static String prettyArrayList(ArrayList<String> arrayList, String spacer) {
        String out = "";
        for (String string : arrayList)
            out = out + string + spacer;
        if (out.length() != 0)
            out = out.substring(0, out.length()-spacer.length());
        return out;
    }
}
