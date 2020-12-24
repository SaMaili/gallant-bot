import commands.types.UpdateCommand;
import manage.CommandManager;
import manage.LiteSQL;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.stream.Collectors;

public class Gallant {

    public static Gallant INSTANCE;

    public ShardManager shardManager;
    private final CommandManager cmdManager;

    public static void main(String[] args) {
        try {
            new Gallant();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public Gallant() throws LoginException, IllegalArgumentException {
        try {
            UpdateCommand.converter = new File("/AsciiToUtf8.js");
            System.out.println("File \"" + UpdateCommand.converter.getAbsolutePath() + "\" needs to be created: " + (new File("/AsciiToUtf8.js").createNewFile() ? "\ncreated" : false));

            FileWriter myWriter = new FileWriter("/AsciiToUtf8.js");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(Gallant.class.getResourceAsStream("/src/AsciiToUtf8.js")));
            String converterString = buffer.lines().collect(Collectors.joining(System.getProperty("line.separator")));
            myWriter.write(converterString);
            myWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        INSTANCE = this;

        LiteSQL.connect();

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault("NzkwNTg5NTczNTczOTAyMzY4.X-Cz6g.CfIkSfmiFgry_y8V-AXHRo8wQRg");

        builder.setActivity(Activity.watching("Gallant members"));
        builder.setStatus(OnlineStatus.ONLINE);

        cmdManager = new CommandManager();

        builder.addEventListeners(new CommandListener());

        shardManager = builder.build();
        System.out.println("Bot online.");

        shutdown();
    }


    public void shutdown() {
        new Thread(() -> {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("exit")) {
                        if (shardManager != null) {
                            shardManager.setStatus(OnlineStatus.OFFLINE);
                            shardManager.shutdown();
                            LiteSQL.disconnect();
                            System.out.println("Bot offline.");
                        }
                        reader.close();
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public CommandManager getCmdManager() {
        return cmdManager;
    }
}
