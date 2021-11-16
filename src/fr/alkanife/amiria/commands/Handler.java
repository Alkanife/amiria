package fr.alkanife.amiria.commands;

import fr.alkanife.amiria.Amiria;
import fr.alkanife.botcommons.CommandHandler;
import fr.alkanife.botcommons.Lang;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Handler extends CommandHandler {

    @Override
    public void success(SlashCommandEvent slashCommandEvent) {
        User user = slashCommandEvent.getUser();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Lang.t("logs-command-executed"));
        embedBuilder.setThumbnail(user.getAvatarUrl());
        embedBuilder.setColor(new Color(172, 172, 172));
        embedBuilder.addField("Membre", user.getName() + " (" + user.getAsMention() + ")", true);
        embedBuilder.addField("Salon", slashCommandEvent.getChannel().getName() + " (" + slashCommandEvent.getTextChannel().getAsMention() + ")", true);

        String command = slashCommandEvent.getName();

        if (slashCommandEvent.getSubcommandName() != null)
            command += " " + slashCommandEvent.getSubcommandName();

        for (OptionMapping option : slashCommandEvent.getOptions())
            command += " " + option.getAsString();

        embedBuilder.addField("Commande", command, false);

        Amiria.broadLog(embedBuilder.build());
    }

    @Override
    public void fail(SlashCommandEvent slashCommandEvent, Exception e) {
        Amiria.getLogger().error("-----------------------");
        Amiria.getLogger().error("Failed to execute a command");
        Amiria.getLogger().error("Command name: " + slashCommandEvent.getName());
        Amiria.getLogger().error("sub group " + slashCommandEvent.getSubcommandGroup());
        Amiria.getLogger().error("sub name " + slashCommandEvent.getSubcommandName());

        for (OptionMapping optionMapping : slashCommandEvent.getOptions())
            Amiria.getLogger().error("Option " + optionMapping.getName() + " -> " + optionMapping.getAsString());

        Amiria.getLogger().error("Executed by " + slashCommandEvent.getUser().getName() + " (" + slashCommandEvent.getUser().getId() + ")");
        Amiria.getLogger().error("In " + slashCommandEvent.getTextChannel().getName() + " (" + slashCommandEvent.getTextChannel().getId() + ")");

        Amiria.getLogger().error(e.getCause().getMessage(), e);

        Amiria.getLogger().error("-----------------------");

        slashCommandEvent.reply(Lang.t("command-error"))
                .queue(msg -> Amiria.broadcastError(e));
    }
}