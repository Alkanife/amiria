package fr.alkanife.amiria;

import java.lang.management.ManagementFactory;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static String absolutePath() {
        return Paths.get("").toAbsolutePath().toString();
    }

    public static String limit(String value, int length) {
        StringBuilder buf = new StringBuilder(value);
        if (buf.length() > length) {
            buf.setLength(length - 3);
            buf.append("...");
        }

        return buf.toString();
    }

    public static long getUpDays(){
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        return TimeUnit.MILLISECONDS.toDays(uptime);
    }

    public static String musicDuration(long duration) {
        if (duration >= 3600000) {
            return String.format("%02d:%02d:%02d",  TimeUnit.MILLISECONDS.toHours(duration),
                    TimeUnit.MILLISECONDS.toMinutes(duration) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(duration) % TimeUnit.MINUTES.toSeconds(1));
        } else {
            return String.format("%02d:%02d",  TimeUnit.MILLISECONDS.toMinutes(duration) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(duration) % TimeUnit.MINUTES.toSeconds(1));
        }
    }

    public static boolean isURL(String s) {
        try {
            new URI(s);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
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
