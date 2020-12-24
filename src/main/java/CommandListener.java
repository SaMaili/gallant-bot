import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();

        if (event.isFromType(ChannelType.TEXT)) {
            TextChannel textChannel = event.getTextChannel();
            if (message.startsWith(".gl ")) {
                String[] args = message.substring(4).split(" ");
                if (args.length > 0) {
                    if (!Gallant.INSTANCE.getCmdManager().perform(args[0], event.getMember(), textChannel, event.getMessage())) {
                        textChannel.sendMessage("im sure this command does not exist.").queue();
                    }
                }
            }
        }
    }
}
