package fr.alkanife.amiria;

import fr.alkanife.amiria.characters.Characters;
import fr.alkanife.amiria.commands.CommandHandler;
import fr.alkanife.amiria.commands.Commands;
import fr.alkanife.amiria.music.Music;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Events extends ListenerAdapter {

    public static List<LoggedMessage> sentMessages = new ArrayList<>();

    @Override
    public void onReady(ReadyEvent readyEvent) {
        Amiria.getLogger().info("Connected to Discord");
        Amiria.getLogger().info("Fetching Discord informations");

        JDA jda = readyEvent.getJDA();


        //search enyxia
        Guild enyxia = jda.getGuildById("341997146519633922");

        if (enyxia == null) {
            Amiria.getLogger().error("Enyxia was not found");
            jda.shutdown();
            return;
        }

        Amiria.setEnyxia(enyxia);
        Amiria.getLogger().info("Enyxia was found, " + enyxia.getMemberCount() + " members");


        //search megapolice role
        Object megapoliceID = Amiria.getConfigurationValues().get("megapolice-id");

        if (megapoliceID == null) {
            Amiria.getLogger().error("Invalid configuration: megapolice-id is null");
            jda.shutdown();
            return;
        }

        Role megapolice = enyxia.getRoleById(String.valueOf(megapoliceID));

        if (megapolice == null) {
            Amiria.getLogger().error("Enyxian Megapolice not found");
            jda.shutdown();
            return;
        }

        Amiria.setMegapolice(megapolice);
        Amiria.getLogger().info(megapolice.getName() + " role was found");


        //search people role
        Object peopleID = Amiria.getConfigurationValues().get("people-id");

        if (peopleID == null) {
            Amiria.getLogger().error("Invalid configuration: people-id is null");
            jda.shutdown();
            return;
        }

        Role people = enyxia.getRoleById(String.valueOf(peopleID));

        if (people == null) {
            Amiria.getLogger().error("People not found");
            jda.shutdown();
            return;
        }

        Amiria.setPeople(people);
        Amiria.getLogger().info(people.getName() + " role was found");


        //search master role
        Object masterID = Amiria.getConfigurationValues().get("master-id");

        if (masterID == null) {
            Amiria.getLogger().error("Invalid configuration: master-id is null");
            jda.shutdown();
            return;
        }

        Role master = enyxia.getRoleById(String.valueOf(masterID));

        if (master == null) {
            Amiria.getLogger().error("Master not found");
            jda.shutdown();
            return;
        }

        Amiria.setMaster(master);
        Amiria.getLogger().info(master.getName() + " role was found");


        //search #hrp channel
        Object hrpID = Amiria.getConfigurationValues().get("hrp-id");

        if (hrpID == null) {
            Amiria.getLogger().error("Invalid configuration: hrp-id is null");
            jda.shutdown();
            return;
        }

        TextChannel hrp = enyxia.getTextChannelById(String.valueOf(hrpID));

        if (hrp == null) {
            Amiria.getLogger().error("#hrp was not found");
            jda.shutdown();
            return;
        }

        Amiria.setHrp(hrp);
        Amiria.getLogger().info("#" + hrp.getName() + " channel was found");


        //search #logs channel
        Object logsID = Amiria.getConfigurationValues().get("logs-id");

        if (logsID == null) {
            Amiria.getLogger().error("Invalid configuration: logs-id is null");
            jda.shutdown();
            return;
        }

        TextChannel logs = enyxia.getTextChannelById(String.valueOf(logsID));

        if (logs == null) {
            Amiria.getLogger().error("#logs was not found");
            jda.shutdown();
            return;
        }

        Amiria.setLogs(logs);
        Amiria.getLogger().info("#" + logs.getName() + " channel was found");


        //search default playlist
        Object defaultPlaylist = Amiria.getConfigurationValues().get("default-playlist");

        if (defaultPlaylist == null) {
            Amiria.getLogger().error("Invalid configuration: default-playlist is null");
            jda.shutdown();
            return;
        }

        if (!Utils.isURL((String) defaultPlaylist)) {
            Amiria.getLogger().error("Invalid default playlist");
            jda.shutdown();
            return;
        }

        Amiria.setDefaultPlaylist((String) defaultPlaylist);
        Amiria.getLogger().info("Default playlist found (" + defaultPlaylist + ")");


        //MUSIC
        Music.initialize();
        //////////////////


        StatusReport statusReport = new StatusReport().version().newLine();

        Amiria.getLogger().info("Loading translations");
        statusReport.checkTranslations(!Lang.load());
        Amiria.getLogger().info(Lang.getTranslations().size() + " loaded translations");

        Amiria.getLogger().info("Loading characters");
        statusReport.checkCharacters(!Characters.load());
        Amiria.getLogger().info(Characters.getCharacters().size() + " loaded characters");

        Amiria.getLogger().info("Setting up commands");
        CommandHandler.registerCommands(new Commands());
        statusReport.checkCommands();
        Amiria.getLogger().info(CommandHandler.getCommands().size() + " loaded commands");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Mise en tension réussie");
        embedBuilder.setDescription("**État des différents services :**\n" + statusReport.getStatus());
        embedBuilder.setThumbnail(Utils.getOKMemeURL());
        embedBuilder.setColor(new Color(150, 224, 136));
        Amiria.broadLog(embedBuilder.build());

        Amiria.getLogger().info("Ready!");
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        CommandHandler.redirect(event);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent guildMessageReceivedEvent) {
        if (guildMessageReceivedEvent.getAuthor().isBot())
            return;

        if (sentMessages.size() >= 20)
            sentMessages.remove(0);

        sentMessages.add(new LoggedMessage(guildMessageReceivedEvent.getMessageIdLong(), guildMessageReceivedEvent.getMessage().getContentDisplay(), guildMessageReceivedEvent.getAuthor().getIdLong()));
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent guildMemberJoinEvent) {
        String[] welcomeMessages = Lang.t("welcome-messages", guildMemberJoinEvent.getMember().getAsMention()).split("\n");

        int random = new Random().nextInt(welcomeMessages.length);

        String message = welcomeMessages[random];

        Amiria.getHrp().sendMessage(message).queue();

        guildMemberJoinEvent.getGuild().modifyMemberRoles(guildMemberJoinEvent.getMember(), Amiria.getPeople()).queue();

        User user = guildMemberJoinEvent.getMember().getUser();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Lang.t("logs-member-join"));
        embedBuilder.setThumbnail(user.getAvatarUrl());
        embedBuilder.setColor(new Color(93, 154, 74));
        embedBuilder.addField("Membre", user.getName() + " (" + user.getAsMention() + ")", true);

        Amiria.broadLog(embedBuilder.build());
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent guildMemberRemoveEvent) {
        User user = guildMemberRemoveEvent.getUser();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Lang.t("logs-member-left"));
        embedBuilder.setThumbnail(user.getAvatarUrl());
        embedBuilder.setColor(new Color(141, 61, 61));
        embedBuilder.addField("Membre", user.getName() + " (" + user.getAsMention() + ")", true);

        Amiria.broadLog(embedBuilder.build());
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent messageUpdateEvent) {
        String content = null;

        for (LoggedMessage sentMessage : sentMessages)
            if (sentMessage.getId() == messageUpdateEvent.getMessageIdLong())
                content = sentMessage.getContent();

        if (content == null)
            return;

        User user = messageUpdateEvent.getAuthor();
        TextChannel textChannel = messageUpdateEvent.getTextChannel();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Lang.t("logs-message-edited"));
        embedBuilder.setThumbnail(user.getAvatarUrl());
        embedBuilder.setColor(new Color(61, 141, 132));
        embedBuilder.addField("Membre", user.getName() + " (" + user.getAsMention() + ")", true);
        embedBuilder.addField("Salon", textChannel.getName() +  " (" + textChannel.getAsMention() + ")", true);
        embedBuilder.addField("Message avant modification", Utils.limit(content, 1000), false);
        embedBuilder.addField("Message après modification", Utils.limit(messageUpdateEvent.getMessage().getContentDisplay(), 1000), false);

        Amiria.broadLog(embedBuilder.build());
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent guildVoiceLeaveEvent) {
        if (guildVoiceLeaveEvent.getMember().getId().equalsIgnoreCase(Amiria.getJda().getSelfUser().getId()))
            Music.reset();
    }

    @Override
    public void onMessageDelete(MessageDeleteEvent messageDeleteEvent) {
        String content = null;
        long authorID = 0;

        for (LoggedMessage sentMessage : sentMessages) {
            if (sentMessage.getId() == messageDeleteEvent.getMessageIdLong()) {
                content = sentMessage.getContent();
                authorID = sentMessage.getAuthor();
            }
        }

        if (content == null)
            return;

        Member member = Amiria.getEnyxia().getMemberById(authorID);

        if (member == null)
            return;

        User user = member.getUser();
        TextChannel textChannel = messageDeleteEvent.getTextChannel();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Lang.t("logs-message-deleted"));
        embedBuilder.setThumbnail(user.getAvatarUrl());
        embedBuilder.setColor(new Color(141, 61, 61));
        embedBuilder.addField("Auteur du message", user.getName() + " (" + user.getAsMention() + ")", true);
        embedBuilder.addField("Salon", textChannel.getName() +  " (" + textChannel.getAsMention() + ")", true);
        embedBuilder.addField("Message", Utils.limit(content, 1000), false);

        Amiria.broadLog(embedBuilder.build());
    }

}
