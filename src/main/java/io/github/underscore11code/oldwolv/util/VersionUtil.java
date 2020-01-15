package io.github.underscore11code.oldwolv.util;

import io.github.underscore11code.oldwolv.OldWolv;
import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class VersionUtil {

    @Getter static HashMap<String, String> versionInfo = new HashMap<>();

    public static void load() {
        try {
            Scanner verInfo = new Scanner(OldWolv.class.getClassLoader().getResource("git.properties").openStream());
            while (verInfo.hasNext()) {
                String line = verInfo.nextLine();
                if (line.startsWith("#"))
                    continue;
                String[] split = line.split("=");
                if (split.length == 1)
                    versionInfo.put(split[0], null);
                else
                    versionInfo.put(split[0], split[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return versionInfo.get(key);
    }
}
