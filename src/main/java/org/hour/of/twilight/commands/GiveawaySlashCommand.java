package org.hour.of.twilight.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.hour.of.twilight.Interfaces.SlashCommandInterface;
import org.hour.of.twilight.utilities.HelperFunctions;

import java.util.ArrayList;
import java.util.List;


public class GiveawaySlashCommand implements SlashCommandInterface
{
    @Override
    public SlashCommandData BuildCommand()
    {
        final SlashCommandData slash = Commands.slash("giveaway", "Gathers reactions from the message and selects random winners from them.");
        slash.setGuildOnly(true);
        slash.setDefaultPermissions(DefaultMemberPermissions.DISABLED);
        slash.addOptions(GetOptions());
        return slash;
    }

    public List<OptionData> GetOptions()
    {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER, "numberofwinners", "Number of winners").setRequired(true));
        options.add(new OptionData(OptionType.CHANNEL, "channel", "Channel of giveaway").setRequired(true));
        options.add(new OptionData(OptionType.STRING, "message", "Message to display with the giveaway").setRequired(true));
        return options;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event)
    {
        event.deferReply().queue();
        int numberofwinners = (int) event.getOption("numberofwinners").getAsLong();
        String giveawaymessage = event.getOption("message").getAsString();
        MessageChannel channel = event.getOption("channel").getAsChannel().asTextChannel();
        List<User> giveawaylist = new ArrayList<>();
        channel.getHistory().retrievePast(1)
                .map(messages -> messages.get(0))
                .queue(message -> {
                    for (MessageReaction reaction : message.getReactions())
                    {
                        reaction.retrieveUsers().queue(users ->
                        {
                            for (User reactor : users) {
                                if (!reactor.isBot() && !giveawaylist.contains(reactor))
                                    giveawaylist.add(reactor);
                            }
                        });
                    }
                });
        HelperFunctions.sendEphemeralMessage(event, "Not yet completed.");
    }

}
