package io.github.underscore11code.oldwolv.config;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GuildConfig {
    @Getter @Setter ArrayList<String> staffRoleIds = new ArrayList<>();

    @Getter private String guildId;

    private GuildConfig(String guildId) {
        this.guildId = guildId;
    }

    public static GuildConfig get(String guildId) {
        File file = new File("/guildconfig/" + guildId + ".json");
        if (file.exists())
            return new Gson().fromJson(new Scanner("/guildconfig/" + guildId + ".json").nextLine(), GuildConfig.class);
        else
            return new GuildConfig(guildId);
    }

    public void save() {
        try {
            new FileWriter("/guildconfig/" + this.guildId + ".json").write(new Gson().toJson(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
