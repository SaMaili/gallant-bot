package commands.types;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class HelpCommand implements ServerCommand{
    @Override
    public void performCommand(Member member, TextChannel channel, Message message) {
        System.out.println(channel.canTalk());
        System.out.println(channel.getId());
        System.out.println(channel.getIdLong());
        System.out.println();
    }
}
