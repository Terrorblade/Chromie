package org.hour.of.twilight.commands;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.managers.Presence;
import org.hour.of.twilight.Interfaces.SlashCommandInterface;
import org.hour.of.twilight.utilities.HelperFunctions;

import java.util.ArrayList;
import java.util.List;

public class StatusSlashCommand implements SlashCommandInterface
{
    @Override
    public SlashCommandData BuildCommand()
    {
        final SlashCommandData slash = Commands.slash("setstatus", "Set Chromie's status.");
        slash.setGuildOnly(true);
        slash.setDefaultPermissions(DefaultMemberPermissions.DISABLED);
        slash.addOptions(GetOptions());
        return slash;
    }

    public List<OptionData> GetOptions()
    {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER, "type", "Type of message. (1 playing, 2 streaming (requires url), 3 listening, 4 watching, 5 competing").setRequired(true));
        options.add(new OptionData(OptionType.STRING, "status", "The text to say.").setRequired(true));
        options.add(new OptionData(OptionType.STRING, "url", "url, required for some statuses"));
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event)
    {
        int type = (int)event.getOption("type").getAsLong();
        String message = event.getOption("status").getAsString();
        Presence presence = event.getJDA().getPresence();
        switch(type)
        {
            case 1:
                presence.setActivity(Activity.playing(message));
                break;
            case 2:
                presence.setActivity(Activity.streaming(message, event.getOption("url").getAsString()));
                break;
            case 3:
                presence.setActivity(Activity.listening(message));
                break;
            case 4:
                presence.setActivity(Activity.watching(message));
                break;
            case 5:
                presence.setActivity(Activity.competing(message));
                break;
            default:
                HelperFunctions.sendEphemeralMessage(event, "Invalid type " + type + ".");
        }
        HelperFunctions.sendEphemeralMessage(event, "Status updated.");
    }
}
