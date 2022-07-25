package org.hour.of.twilight.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.hour.of.twilight.Interfaces.SlashCommandInterface;
import org.hour.of.twilight.utilities.HelperFunctions;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class RconSlashCommand implements SlashCommandInterface
{
    @Override
    public SlashCommandData BuildCommand()
    {
        final SlashCommandData slash = Commands.slash("rcon", "Sends a remote command to the server.");
        slash.setGuildOnly(true);
        slash.setDefaultPermissions(DefaultMemberPermissions.DISABLED);
        slash.addOptions(GetOptions());
        return slash;
    }

    public List<OptionData> GetOptions()
    {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "realm", "Number of winners").addChoice("live", "live").
                addChoice("dev","dev").setRequired(true));
        options.add(new OptionData(OptionType.STRING, "command", "Command to send to the server").setRequired(true));
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event)
    {
        if(event.getOption("realm").getAsString() == "live")
            LiveExecute(event);
        else
            DevExecute(event);
    }

    public void LiveExecute(SlashCommandInteractionEvent event)
    {
        event.getHook().sendMessage("Command sent.").queue();
        HelperFunctions.SendSoapHandleResult(event, event.getOption("command").getAsString(), true);
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Command Log");
        eb.setColor(Color.BLUE);
        eb.setAuthor(event.getUser().getAsTag(),null, event.getUser().getAvatarUrl());
        eb.addField("Command", event.getOption("command").getAsString(), false);
        eb.setDescription(event.getUser().getName() + " issued an RCon command to the live realm.");
        eb.setFooter(event.getUser().getId());
        eb.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        HelperFunctions.SendToBotLogs(event, eb.build());
    }

    public void DevExecute(SlashCommandInteractionEvent event)
    {
        HelperFunctions.SendSoapHandleResult(event, event.getOption("command").getAsString(), false);
        event.getHook().sendMessage("Command sent.").queue();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Command Log");
        eb.setColor(Color.GREEN);
        eb.setAuthor(event.getUser().getAsTag(),null, event.getUser().getAvatarUrl());
        eb.addField("Command", event.getOption("command").getAsString(), false);
        eb.setDescription(event.getUser().getName() + " issued an RCon command to the dev realm.");
        eb.setFooter(event.getUser().getId());
        eb.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        HelperFunctions.SendToBotLogs(event, eb.build());
    }
}
