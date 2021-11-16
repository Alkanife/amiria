package fr.alkanife.amiria.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.alkanife.amiria.*;
import fr.alkanife.amiria.characters.Character;
import fr.alkanife.amiria.characters.Characters;
import fr.alkanife.amiria.music.Music;
import fr.alkanife.botcommons.Command;
import fr.alkanife.botcommons.Lang;
import fr.alkanife.botcommons.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Commands {

    @Command(name = "sudo")
    public void sudo(SlashCommandEvent slashCommandEvent) {
        String sudoCommand = slashCommandEvent.getOption("command").getAsString();

        Member member = slashCommandEvent.getMember();

        if (member == null)
            return;
        
        boolean access = false;

        for (Role role : member.getRoles())
            if (role.getId().equals(Amiria.getMegapolice().getId()))
                access = true;

        if (!access) {
            slashCommandEvent.reply(Lang.t("sudo-command-no-permissions")).queue();
            return;
        }

        String lowerCaseCommand = sudoCommand.toLowerCase(java.util.Locale.ROOT);
        String[] words = lowerCaseCommand.split(" ");

        switch (words[0]) {
            case "help":
                slashCommandEvent.reply("`[help]- Commandes disponibles : 'help', 'shutdown', 'status', 'rename <nom>', 'reload playlists', 'reload translations', 'reload characters'.`").queue();
                break;

            case "shutdown":
                slashCommandEvent.reply("`[shutdown]- Arrêt en cours...`").queue(msg -> {
                    Amiria.getLogger().info("Shutdown by " + slashCommandEvent.getUser().getName());
                    Amiria.getJda().shutdownNow();
                    System.exit(0);
                });
                break;

            case "status":
                slashCommandEvent.reply(new MessageBuilder("`[status]- État des services : `")
                        .setEmbed(new EmbedBuilder().setDescription(new StatusReport(true).getStatus()).build()).build()).queue();
                break;

            case "rename":
                if (lowerCaseCommand.equals("rename")) {
                    slashCommandEvent.reply("`[rename]- /sudo rename <nouveau nom>`").queue();
                    break;
                }

                String name = sudoCommand.replace("rename ", "");

                slashCommandEvent.getGuild().getSelfMember().modifyNickname(name).queue();
                slashCommandEvent.reply("`[rename]- Je m'appelle désormais " + name + "`").queue();
                break;

            case "reload":
                if (lowerCaseCommand.equals("reload")) {
                    slashCommandEvent.reply("`[reload]- /reload <translations/characters>`").queue();
                    break;
                }

                switch (words[1]) {
                    case "translations":
                        Amiria.getLogger().info("Reloading translations");
                        //String tResult = new StatusReport().checkTranslations(!Lang.load()).getStatus();

                        StatusReport statusReport = new StatusReport();

                        try {
                            Lang.load();
                            statusReport.checkTranslations();
                        } catch (Exception exception) {
                            statusReport.checkTranslations(true);
                            Amiria.getLogger().error("Failed to load translations", exception);
                        }

                        slashCommandEvent.reply(new MessageBuilder("`[reload]- Rechargement des traductions effectué. Résultat :`")
                                .setEmbed(new EmbedBuilder().setDescription(statusReport.getStatus()).build()).build()).queue();
                        Amiria.getLogger().info(Lang.getTranslations().size() + " (re)loaded translations");
                        break;

                    case "characters":
                        Amiria.getLogger().info("Reloading characters");
                        String cResult = new StatusReport().checkCharacters(!Characters.load()).getStatus();
                        slashCommandEvent.reply(new MessageBuilder("`[reload]- Rechargement des personnages effectué. Résultat :`")
                                .setEmbed(new EmbedBuilder().setDescription(cResult).build()).build()).queue();
                        Amiria.getLogger().info(Characters.getCharacters().size() + " (re)loaded characters");
                        break;

                    default:
                        slashCommandEvent.reply("`[reload]- /reload <translations/characters>`").queue();
                        break;
                }
                break;

            default:
                slashCommandEvent.reply("`[sudo]- Commande inconnue, tapez '/sudo help' pour avoir de l'aide.`").queue();
                break;
        }
    }

    @Command(name = "help")
    public void help(SlashCommandEvent slashCommandEvent) {
        slashCommandEvent.reply(Lang.t("help-command")).queue();
    }

    @Command(name = "enyxia")
    public void enyxia(SlashCommandEvent slashCommandEvent) {
        slashCommandEvent.reply(Lang.t("enyxia-command")).queue();
    }

    @Command(name = "characters")
    public void characters(SlashCommandEvent slashCommandEvent) {

        if (Characters.getCharacters().size() == 0) {
            slashCommandEvent.reply(Lang.t("command-disabled")).queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setDescription(Lang.t("characters-command-title"));

        String[] authors = { //TODO authors in conf
                "Sheele",
                "FateHard",
                "Hypix",
                "KuYuDo",
                "RelGainSlide",
                "Sully",
                "Wellarkel",
                "Jeffaria",
                "Mathsky",
                "Izzumaki"
        };

        for (String author : authors) {
            List<Character> characters = Characters.searchByAuthor(author);

            String characterList = "";

            if (characters.size() != 0) {

                int i = 0;

                for (Character character : characters) {

                    if (i > 0)
                        characterList += " ";

                    characterList += "`" + character.getFull_name() + "`";

                    i++;

                    if (i != characters.size())
                        characterList += ",";
                }
            }

            embedBuilder.addField(Lang.t("characters-command-list", author) + " (" + characters.size() + ")", characterList, false);
        }

        slashCommandEvent.replyEmbeds(embedBuilder.build()).queue();
    }

    @Command(name = "character")
    public void character(SlashCommandEvent slashCommandEvent) {
        if (Characters.getCharacters().size() == 0) {
            slashCommandEvent.reply(Lang.t("command-disabled")).queue();
            return;
        }

        String characterName = slashCommandEvent.getOption("character").getAsString();

        Character character = Characters.search(characterName);

        if (character == null) {
            slashCommandEvent.reply(Lang.t("character-command-not-found")).queue();
            return;
        }

        StringBuilder sheetBuilder = new StringBuilder();

        if (character.getFull_name() != null)
            sheetBuilder.append(Lang.t("character-command-full-name", character.getFull_name())).append("\n");

        if (character.getTitle() != null)
            sheetBuilder.append(Lang.t("character-command-title", character.getTitle())).append("\n");

        if (character.getRace() != null)
            sheetBuilder.append(Lang.t("character-command-race", character.getRace())).append("\n");

        if (character.getAge() != null)
            sheetBuilder.append(Lang.t("character-command-age", character.getAge())).append("\n");

        if (character.getSex() != null)
            sheetBuilder.append(Lang.t("character-command-sex", character.getSex())).append("\n");

        if (character.getFloor() != null)
            sheetBuilder.append(Lang.t("character-command-floor", character.getFloor())).append("\n");

        if (character.getSpecializations() != null)
            sheetBuilder.append(Lang.t("character-command-specialisations", character.getSpecializations())).append("\n");

        if (character.getWeapons() != null)
            sheetBuilder.append(Lang.t("character-command-weapons", character.getWeapons())).append("\n");

        if (character.getCharacter_traits() != null)
            sheetBuilder.append(Lang.t("character-command-character-traits", character.getCharacter_traits())).append("\n");


        final StringBuilder part2 = new StringBuilder();

        if (character.getAspect() != null)
            part2.append(Lang.t("character-command-aspect", character.getAspect())).append("\n");

        if (character.getOwn_power() != null)
            part2.append(Lang.t("character-command-own-power", character.getOwn_power())).append("\n");

        if (character.getState() != null)
            part2.append(Lang.t("character-command-state", character.getState())).append("\n");

        if (character.getOriginal_owner() != null)
            part2.append(Lang.t("character-command-original-owner", character.getOriginal_owner())).append("\n");

        if (character.getOwner_inspirations() != null)
            part2.append(Lang.t("character-command-owner-inspirations", character.getOwner_inspirations())).append("\n");

        if (character.getOwner_notes() != null)
            part2.append(Lang.t("character-command-owner-notes", character.getOwner_notes())).append("\n");

        if (character.isRepresentation_image() && character.getFull_name() != null)
            part2.append(Lang.t("character-command-representation-image", "https://enyxia.alkanife.fr/amiria/images/"
                    + character.getFull_name().split(" ")[0]
                    .toLowerCase(java.util.Locale.ROOT)
                    .replaceAll("é", "e")
                    .replaceAll("ï", "i")
                    .replaceAll("ō", "o")
                    .replaceAll("û", "u"))).append(".png\n");


        slashCommandEvent.reply(Lang.t("character-command-success"))
                .queue(msg -> slashCommandEvent.getChannel().sendMessage(sheetBuilder)
                        .queue(msg1 -> slashCommandEvent.getChannel().sendMessage(part2)
                                .queue()));
    }

    @Command(name = "jukebox")
    public void music(SlashCommandEvent slashCommandEvent) {
        String subCommand = slashCommandEvent.getSubcommandName();

        switch (subCommand) {

            case "play":

                String url = slashCommandEvent.getOption("input").getAsString();

                if (url.equalsIgnoreCase("default"))
                    url = Amiria.getDefaultPlaylist();
                else
                    if (!Utils.isURL(url))
                        url = "ytsearch: " + url;

                Music.loadAndPlay(slashCommandEvent, url);
                break;

            case "skip":
                if (Amiria.getPlayer().getPlayingTrack() == null) {
                    slashCommandEvent.reply(Lang.t("jukebox-command-no-current")).queue();
                    return;
                }

                OptionMapping option = slashCommandEvent.getOption("input");

                int skip = 0;

                if (option != null) {
                    long optionLong = option.getAsLong();

                    if (optionLong >= Amiria.getTrackScheduler().getQueue().size()) {
                        slashCommandEvent.reply(Lang.t("jukebox-command-skip-nope")).queue();
                        return;
                    }

                    for (skip = 0; skip < optionLong; skip++) {
                        Amiria.getTrackScheduler().getQueue().remove();
                    }
                }

                Amiria.getTrackScheduler().nextTrack();

                if (option == null)
                    slashCommandEvent.reply(Lang.t("jukebox-command-skip-one")).queue();
                else
                    slashCommandEvent.reply(Lang.t("jukebox-command-skip-mult", String.valueOf(skip))).queue();

                break;

            case "stop":
                slashCommandEvent.reply(Lang.t("jukebox-command-stop")).queue();
                Amiria.getEnyxia().getAudioManager().closeAudioConnection();
                break;

            case "shuffle":
                List<AudioTrack> audioTracks = new ArrayList<>(Amiria.getTrackScheduler().getQueue());

                Collections.shuffle(audioTracks);

                BlockingQueue<AudioTrack> blockingQueue = new LinkedBlockingQueue<>();

                for (AudioTrack audioTrack : audioTracks)
                    blockingQueue.offer(audioTrack);

                Amiria.getTrackScheduler().setQueue(blockingQueue);

                slashCommandEvent.reply(Lang.t("jukebox-command-shuffle")).queue();
                break;

            case "clear":
                Amiria.getTrackScheduler().setQueue(new LinkedBlockingQueue<>());
                slashCommandEvent.reply(Lang.t("jukebox-command-clear")).queue();
                break;

            case "queue":
                AudioTrack current = Amiria.getPlayer().getPlayingTrack();

                if (current == null) {
                    slashCommandEvent.reply(Lang.t("jukebox-command-no-current")).queue();
                    return;
                }

                slashCommandEvent.deferReply().queue();

                EmbedBuilder embedBuilder = new EmbedBuilder();
                String desc = "";
                if (Amiria.getTrackScheduler().getQueue().size() == 0) {
                    embedBuilder.setTitle(Lang.t("jukebox-command-queue-now-playing"));
                    embedBuilder.setThumbnail("https://img.youtube.com/vi/" + current.getIdentifier() + "/0.jpg");
                    desc += "**[" + current.getInfo().title + "](" + current.getInfo().uri + ")** [" + Utils.musicDuration(current.getDuration()) + "]";
                } else {                                                                           // v because String.valueOf don't work?
                    embedBuilder.setTitle(Lang.t("jukebox-command-queue-queued-title", "~"+Amiria.getTrackScheduler().getQueue().size()));
                    embedBuilder.setThumbnail(Lang.t("jukebox-command-plgif"));
                    desc = "__" + Lang.t("jukebox-command-queue-queued-now-playing") + "__\n" +
                            "**[" + current.getInfo().title + "](" + current.getInfo().uri + ")** [" + Utils.musicDuration(current.getDuration()) + "]\n" +
                            "\n" +
                            "__" + Lang.t("jukebox-command-queue-queued-incoming") + "__\n";

                    int i = 0;

                    for (AudioTrack audioTrack : Amiria.getTrackScheduler().getQueue()) {
                        i++;
                        desc += "`" + i + ".` [" + audioTrack.getInfo().title + "](" + audioTrack.getInfo().uri + ") [" + Utils.musicDuration(audioTrack.getDuration()) + "]\n";

                        if (i > 10)
                            break;
                    }

                    desc += "\n__" + Lang.t("jukebox-command-queue-queued-time") + "__ `" + Utils.musicDuration(Amiria.getTrackScheduler().getQueueDuration()) + "`";
                }

                embedBuilder.setDescription(desc);

                slashCommandEvent.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
                break;
        }
    }

}
