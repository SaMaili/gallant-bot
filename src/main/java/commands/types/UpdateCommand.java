package commands.types;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.JSONException;
import org.json.JSONObject;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

import static java.lang.Thread.sleep;

public class UpdateCommand implements ServerCommand {

    final public static String API_KEY = "PDAKTGFAGATEPCIR07JDVZI5REI8M";
    static ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
    public static File converter = new File("convertAsciiToUtf8.js");


    @Override
    public void performCommand(Member member, TextChannel channel, Message message) {

        if (channel.getIdLong() != 790293768695054397L) {
            channel.sendMessage("I am not allowed to that in this channel. Look in " + Objects.requireNonNull(channel.getGuild().getTextChannelCache().getElementById(790293768695054397L)).getAsMention() + " for the last update i made.").queue();
            System.out.println("\"" + member.getNickname() + "\" tried to update the list in " + channel.getName() + " [" + channel.getId() + "]");
            return;
        }
        String[] command = message.getContentRaw().split(" ");
        message.delete().queue();
        try {
            if (command.length == 3) {
                switch (command[2]) {
                    case "1":
                        checkAverageXP(channel, 526412, 1900, 340); //Gallant
                        break;
                    case "2":
                        checkAverageXP(channel, 1108627, 1680, 340); //Gallant II
                        break;
                    case "3":
                        checkAverageXP(channel, 1152725, 1400, 340); //Gallant Valiant
                        break;
                    case "4":
                        checkAverageXP(channel, 1269820, 1400, 340); //Gallant NA
                        break;
                }
            } else if (command.length < 3) {
                channel.sendMessage("please specify what clan do you want to update").queue();
            }
        } catch (IOException e) {
            e.printStackTrace();
            channel.sendMessage("API overloaded I'll wait 15 minutes. then it will be fine ^^").queue();
        }
        //}
    }

    public static String convertASCIIToUTF8(String ascii) {
        try {
            engine.eval(new FileReader(converter));
            Invocable invocable = (Invocable) engine;
            return (String) invocable.invokeFunction("decode", ascii);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ascii;
    }

    public static JSONObject getOnlineJSON(String URL) throws Exception {
        java.net.URL obj = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        //add request header
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        //System.out.println("Sending '" + connection.getRequestMethod() + "' request to URL : " + URL);
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String finalResponse;

        if (response.charAt(0) == '[') {
            finalResponse = "{    \"Array\": " + response.toString() + "}";
        } else {
            finalResponse = response.toString();
        }
        //Read JSON response and print

        return new JSONObject(finalResponse);
    }

    public static int getDifferenceDays(Date d1, Date d2) {
        int daysDiff;
        long diff = d2.getTime() - d1.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
        daysDiff = (int) diffDays;
        return daysDiff;
    }

    private static boolean isCharCJK(final String string) {
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if ((Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
                    || (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A)
                    || (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B)
                    || (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS)
                    || (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS)
                    || (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT)
                    || (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION)
                    || (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.ENCLOSED_CJK_LETTERS_AND_MONTHS)) {
                return true;
            }
        }
        return false;
    }

    public static void checkAverageXP(TextChannel channel, int clanId, int eloReq, int minXp) throws IOException {
        JSONObject clan = null;
        while (true) {
            try {
                clan = getOnlineJSON("https://api.brawlhalla.com/clan/" + clanId + "/?api_key=" + API_KEY);
                break;
            } catch (UnknownHostException | ConnectException e) {
                try {
                    sleep(1000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        assert clan != null;
        embedBuilder.setTitle("__**" + clan.getString("clan_name") + "**__");
        switch (clan.getString("clan_name")) {
            case "Gallant":
                embedBuilder.setColor(Color.decode("#120078"));
                break;
            case "Gallant II":
                embedBuilder.setColor(Color.decode("#9d0191"));
                break;
            case "Gallant Valiant":
                embedBuilder.setColor(Color.decode("#ffb396"));
                break;
            case "Gallant NA":
                embedBuilder.setColor(Color.decode("#fff5c0"));
                break;
        }

        Date now = new Date();
        embedBuilder.appendDescription("***Calculated on: " + now + "***");
        System.out.println(embedBuilder.getDescriptionBuilder().toString());
        channel.sendMessage(embedBuilder.build()).queue();
        StringBuilder stringBuilder = new StringBuilder();

        JSONObject player;

        int averages = 0;
        int i;

        int line = 0;

        stringBuilder.append("```diff\n");

        for (i = 0; i < clan.getJSONArray("clan").length(); i++) {
            player = clan.getJSONArray("clan").getJSONObject(i);
            int playerRanked = 0;
            while (true) {
                try {
                    playerRanked = getOnlineJSON("https://api.brawlhalla.com/player/" + clan.getJSONArray("clan").getJSONObject(i).getInt("brawlhalla_id") + "/ranked?api_key=" + API_KEY).getInt("peak_rating");
                    sleep(500);
                    break;
                } catch (UnknownHostException e) {
                    try {
                        sleep(2000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                } catch (JSONException e) {
                    playerRanked = -1;
                    try {
                        sleep(500);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    break;
                } catch (IOException e) {
                    throw new IOException(e);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int dayIn = getDifferenceDays(new Date(player.getLong("join_date") * 1000), now);
            int playerAverage = (player.getInt("xp") / dayIn);
            String name = convertASCIIToUTF8(player.getString("name"));

            int charSize = isCharCJK(name) ? -28 : -32;
            String format;

            if ((minXp <= playerAverage || dayIn < 7) && playerRanked >= eloReq) {
                format = String.format("+  NAME: %" + charSize + "s\tDAYS_IN_CLAN: %-4d\tAVG_XP_PER_DAY: %-4d\tPEAK_ELO: %s%n",
                        name,
                        dayIn,
                        (player.getInt("xp") / dayIn),
                        playerRanked);
                System.out.print(format);
            } else if ((minXp > playerAverage) && playerRanked < eloReq) {
                format = String.format("-  NAME: %" + charSize + "s\tDAYS_IN_CLAN: %-4d\tAVG_XP_PER_DAY: %-4d\tPEAK_ELO: %s%n",
                        name,
                        dayIn,
                        (player.getInt("xp") / dayIn),
                        playerRanked > -1 ? playerRanked : "hasn't played ranked yet.");
                System.out.print(format);
            } else if (minXp > playerAverage) {
                format = String.format("   NAME: %" + charSize + "s\tDAYS_IN_CLAN: %-4d\tAVG_XP_PER_DAY: %-4d\tPEAK_ELO: %s%n",
                        name,
                        dayIn,
                        (player.getInt("xp") / dayIn),
                        playerRanked);
                System.out.print(format);
            } else {
                format = String.format("---NAME: %" + charSize + "s\tDAYS_IN_CLAN: %-4d\tAVG_XP_PER_DAY: %-4d\tPEAK_ELO: %s%n",
                        name,
                        dayIn,
                        (player.getInt("xp") / dayIn),
                        playerRanked > -1 ? playerRanked : "hasn't played ranked yet.");
                System.out.print(format);
            }
            stringBuilder.append(format);
            line++;

            if (line >= 13 || i == clan.getJSONArray("clan").length() - 1) {
                stringBuilder.append("```\n");
                if (!(i == clan.getJSONArray("clan").length() - 1)) {
                    stringBuilder.append("```diff\n");
                }
                line = 0;
            }
            averages += playerAverage;
        }

        String[] messages = stringBuilder.toString().split("```\n");
        for (String s : messages) {
            channel.sendMessage(s + "```\n").queue();
        }

        Color color = embedBuilder.build().getColor();
        embedBuilder.clear();
        embedBuilder.setColor(color);
        String stats = "\n***clan average XP: " + averages / clan.getJSONArray("clan").length() + "***\n" +
                "***xp-requirement: " + minXp + "***\n" +
                "***elo-requirement: " + eloReq + "***\n" +
                "***Members: " + clan.getJSONArray("clan").length() + "***\n";
        embedBuilder.setDescription(stats);
        channel.sendMessage(embedBuilder.build()).queue();

        String rules = "```diff\n" +
                "HOW TO READ THE TABLES:\n" +
                "If the Player is less than 7 days in the clan the average clan xp won't count.\n" +
                "if a name is...\n\n" +
                "+  green, this player is a good member. *Peaked elo-requirement *Getting above the xp-requirement.\n" +
                "-  red, this player is in trouble. *NOT peaked elo-requirement *NOT getting above the xp-requirement.\n" +
                "   normal text, this player is skilled, but isn't really active. *Peaked elo-requirement *NOT getting above the xp-requirement.\n" +
                "---grey, this player is active, but haven't reached the elo yet. *NOT peaked elo-requirement *Getting above the xp-requirement.\n```";

        if (clan.getString("clan_name").contains("NA")) {
            channel.sendMessage(rules).queue();
        }

        System.out.println("\nDone.");
    }
}
