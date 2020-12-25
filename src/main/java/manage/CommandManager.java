package manage;

import commands.types.HelpCommand;
import commands.types.ServerCommand;
import commands.types.UpdateCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {

    public ConcurrentHashMap<String, ServerCommand> commands;

    public CommandManager() {
        this.commands = new ConcurrentHashMap<>();
        this.commands.put("update", new UpdateCommand());
        this.commands.put("info", new HelpCommand());
    }

    public boolean perform(String command, Member member, TextChannel channel, Message message) {
        ServerCommand cmd;
        if ((cmd = this.commands.get(command.toLowerCase())) != null) {
            cmd.performCommand(member, channel, message);
            return true;
        }
        return false;
    }
}
