package org.hour.of.twilight.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.hour.of.twilight.Interfaces.SlashCommandInterface;
import org.hour.of.twilight.utilities.HelperFunctions;


public class ForgotUsernameSlashCommand implements SlashCommandInterface
{
    @Override
    public SlashCommandData BuildCommand()
    {
        final SlashCommandData slash = Commands.slash("forgotusername", "Recover your username.");
        return slash;
    }

    //todo: Write a confirm of are you sure you forget your username?
    @Override
    public void execute(SlashCommandInteractionEvent event)
    {
        HelperFunctions.SendSoapHandleResult(event, String.format("discord forgotusername %s", event.getUser().getId()),false);
    }
}
