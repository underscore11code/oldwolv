package io.github.underscore11code.oldwolv.config;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class GuildConfig {
    @Getter @Setter ArrayList<String> staffRoleIds = new ArrayList<>();

    @Getter @Setter ArrayList<String> disabledCommands = new ArrayList<>();

    @Getter @Setter String verifiedRoleId;

    @Getter private String guildId;

    private GuildConfig(String guildId) {
        this.guildId = guildId;
    }

    public static GuildConfig get(String guildId) {
        File file = new File("guildconfig/" + guildId + ".json");
        if (file.exists()) {
            try {
                return new Gson().fromJson(new Scanner(file).nextLine(), GuildConfig.class);
            } catch (FileNotFoundException e) {
                System.out.println("Despite file.exists() returning true, we couldn't find the file...HOW?!?!");
                e.printStackTrace();
                return new GuildConfig(guildId);
            } catch (NoSuchElementException e) {
                return new GuildConfig(guildId);
            }
        }
        else
            return new GuildConfig(guildId);
    }

    public void save() {
        try {
            System.out.println("Writing config file for " + this.guildId + ":\n" + new Gson().toJson(this));
             FileWriter writer = new FileWriter("guildconfig/" + this.guildId + ".json");
             writer.write(new Gson().toJson(this));
             writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
