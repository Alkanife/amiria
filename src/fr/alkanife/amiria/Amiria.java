package fr.alkanife.amiria;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import fr.alkanife.amiria.music.TrackScheduler;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.HashMap;

public class Amiria {

    private static String version = "2.0.0";

    private static Logger logger;

    private static HashMap<String, Object> configurationValues;

    private static String token;

    private static Guild enyxia;

    private static Role megapolice;
    private static Role people;
    private static Role master;

    private static TextChannel hrp;
    private static TextChannel logs;

    private static String defaultPlaylist;

    private static JDA jda;

    private static AudioPlayerManager playerManager;
    private static AudioPlayer player;
    private static TrackScheduler trackScheduler;

    public static void main(String[] args) {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        logger = LoggerFactory.getLogger(Amiria.class);

        logger.info("––––––––––––––––––––––––––––––––––––––––––––");
        logger.info("Starting Amiria " + version);

        try {
            logger.info("Reading configuration.yml file");

            configurationValues = YmlReader.read("configuration");

            if (configurationValues == null) {
                logger.error("Configuration file not found");
                return;
            }

            Object token = configurationValues.get("token");

            if (token == null) {
                logger.error("Invalid token");
                return;
            }

            Amiria.token = String.valueOf(token);

            logger.info("Starting JDA");
            JDABuilder jdaBuilder = JDABuilder.createDefault(Amiria.token);
            jdaBuilder.setRawEventsEnabled(true);
            jdaBuilder.setStatus(OnlineStatus.ONLINE);
            jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
            jdaBuilder.enableIntents(GatewayIntent.GUILD_VOICE_STATES);
            jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
            jdaBuilder.addEventListeners(new Events());
            jda = jdaBuilder.build();

        } catch (Exception exception) {
            logger.error("Failed to start", exception);
        }
    }

    public static String getVersion() {
        return version;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static HashMap<String, Object> getConfigurationValues() {
        return configurationValues;
    }

    public static String getToken() {
        return token;
    }

    public static Guild getEnyxia() {
        return enyxia;
    }

    public static void setEnyxia(Guild enyxia) {
        Amiria.enyxia = enyxia;
    }

    public static JDA getJda() {
        return jda;
    }

    public static Role getMegapolice() {
        return megapolice;
    }

    public static void setMegapolice(Role megapolice) {
        Amiria.megapolice = megapolice;
    }

    public static Role getPeople() {
        return people;
    }

    public static void setPeople(Role people) {
        Amiria.people = people;
    }

    public static Role getMaster() {
        return master;
    }

    public static void setMaster(Role master) {
        Amiria.master = master;
    }

    public static TextChannel getHrp() {
        return hrp;
    }

    public static void setHrp(TextChannel hrp) {
        Amiria.hrp = hrp;
    }

    public static TextChannel getLogs() {
        return logs;
    }

    public static void setLogs(TextChannel logs) {
        Amiria.logs = logs;
    }

    public static String getDefaultPlaylist() {
        return defaultPlaylist;
    }

    public static void setDefaultPlaylist(String defaultPlaylist) {
        Amiria.defaultPlaylist = defaultPlaylist;
    }

    public static AudioPlayerManager getPlayerManager() {
        return playerManager;
    }

    public static void setPlayerManager(AudioPlayerManager playerManager) {
        Amiria.playerManager = playerManager;
    }

    public static AudioPlayer getPlayer() {
        return player;
    }

    public static void setPlayer(AudioPlayer player) {
        Amiria.player = player;
    }

    public static TrackScheduler getTrackScheduler() {
        return trackScheduler;
    }

    public static void setTrackScheduler(TrackScheduler trackScheduler) {
        Amiria.trackScheduler = trackScheduler;
    }

    public static void broadLog(EmbedBuilder embedBuilder) {
        broadLog(embedBuilder.build());
    }

    public static void broadLog(MessageEmbed messageEmbed) {
        Amiria.getLogs().sendMessage(messageEmbed).queue();
    }

    public static void broadLog(MessageBuilder messageBuilder) {
        Amiria.getLogs().sendMessage(messageBuilder.build()).queue();
    }

    public static void broadLog(Message message) {
        Amiria.getLogs().sendMessage(message).queue();
    }

    public static void broadcastError(Exception exception) {
        String message = "";

        if (exception != null)
            if (exception.getCause() != null)
                if (exception.getCause().getMessage() != null)
                    message = exception.getCause().getMessage();

        broadcastError(message);
    }

    public static void broadcastError(String message) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Une erreur est servenue");
        embedBuilder.setDescription("*" + Utils.errorMessage() + "*\n\n`" + message + "`\n\nConsultez les logs au plus vite.");
        embedBuilder.setColor(new Color(122, 0, 0));
        embedBuilder.setThumbnail("https://c.tenor.com/EGJ71F2kAdQAAAAM/akai-haato-hololive.gif");

        MessageBuilder messageBuilder = new MessageBuilder(Amiria.getMaster().getAsMention());
        messageBuilder.setEmbed(embedBuilder.build());

        broadLog(messageBuilder);
    }


}
