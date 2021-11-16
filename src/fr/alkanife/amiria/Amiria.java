package fr.alkanife.amiria;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import fr.alkanife.amiria.commands.Handler;
import fr.alkanife.amiria.music.TrackScheduler;
import fr.alkanife.botcommons.Utils;
import fr.alkanife.botcommons.YMLReader;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class Amiria {

    private static String version = "2.1.0";

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
    private static Handler handler;

    private static AudioPlayerManager playerManager;
    private static AudioPlayer player;
    private static TrackScheduler trackScheduler;

    public static void main(String[] args) {
        Utils.clearTerminal();

        logger = LoggerFactory.getLogger(Amiria.class);
        handler = new Handler();

        logger.info("––––––––––––––––––––––––––––––––––––––––––––");
        logger.info("Starting Amiria " + version);

        try {
            logger.info("Reading configuration.yml file");

            configurationValues = YMLReader.read("configuration");

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

    public static Handler getHandler() {
        return handler;
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
        embedBuilder.setDescription("*" + errorMessage() + "*\n\n`" + message + "`\n\nConsultez les logs au plus vite.");
        embedBuilder.setColor(new Color(122, 0, 0));
        embedBuilder.setThumbnail("https://c.tenor.com/EGJ71F2kAdQAAAAM/akai-haato-hololive.gif");

        MessageBuilder messageBuilder = new MessageBuilder(Amiria.getMaster().getAsMention());
        messageBuilder.setEmbed(embedBuilder.build());

        broadLog(messageBuilder);
    }

    public static String errorMessage() {
        String[] m = {
                "E R R O R",
                "Oups j'ai tout cassé",
                "EH BAH BRAVO NILS !!",
                "Erreur.",
                "C'est qui qu'il faut taper cette fois ?",
                "Pas ma faute !",
                "Mise en place du protocol d'urgence. Code : TERMINATOR.",
                "C'est pas moi c'est Discord",
                "Fallait s'y attendre",
                "— 911 what's your emergency?\n— My developer sucks!"
        };

        int random = new Random().nextInt(m.length);

        return m[random];
    }

    public static String getOKMemeURL() {
        String[] gifs = {
                "https://i.pinimg.com/736x/f8/0e/ea/f80eea105e4e34591964c8438f465185.jpg",
                "https://tarheeltrailblazers.com/wp/wp-content/uploads/2020/08/computer-kid.jpeg",
                "https://memegenerator.net/img/images/15978004.jpg",
                "https://s.keepmeme.com/files/en_posts/20200819/f9f6f589f3bc55abf4a23d8bb4a621af2-cats-with-thumbs-up-sign-like.jpg",
                "https://memegenerator.net/img/instances/38115705/thumbs-up-bro.jpg"
        };

        int random = new Random().nextInt(gifs.length);

        return gifs[random];
    }

    /*public static String getMusicGif() {
        String[] gifs = {
                "https://c.tenor.com/xoq0Xxd13koAAAAC/anime-music.gif",
                "https://64.media.tumblr.com/bdc2337467c6406de133e044bfcd99c3/tumblr_mijdkt6bTT1rby56yo1_500.gif",
                "https://i.pinimg.com/originals/e8/43/f5/e843f5e4507d83ef51df7fb27b4d0bb2.gif",
                "https://i.imgur.com/oY9ukdI.gif",
                "https://i.pinimg.com/originals/e8/e8/96/e8e896acf29098647761b34ca53a9808.gif",
                "https://i.pinimg.com/originals/78/a4/65/78a4650a3b24c63e6634f93ec2dc7c38.gif",
                "https://i.pinimg.com/originals/9b/8f/cb/9b8fcbab75208f6c5c15e4abf8f7d70a.gif",
                "https://c.tenor.com/_OA-44hy1-4AAAAC/anime-music.gif"
        };

        int random = new Random().nextInt(gifs.length);

        return gifs[random];
    }*/


}
