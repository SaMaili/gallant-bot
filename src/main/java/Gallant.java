import commands.types.UpdateCommand;
import manage.CommandManager;
import manage.LiteSQL;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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
        //converter File and directories
        UpdateCommand.converter = new File("AsciiToUtf8.js");

        INSTANCE = this;

        LiteSQL.connect();

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(System.getenv("TOKEN"));

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
