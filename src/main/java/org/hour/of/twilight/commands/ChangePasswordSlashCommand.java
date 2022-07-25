package org.hour.of.twilight.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.hour.of.twilight.Interfaces.SlashCommandInterface;
import org.hour.of.twilight.utilities.HelperFunctions;


public class ChangePasswordSlashCommand implements SlashCommandInterface
{
    @Override
    public SlashCommandData BuildCommand()
    {
        final SlashCommandData slash = Commands.slash("changepassword", "Change your game account password.");
        slash.addOptions(GetOptions());
        return slash;
    }


    public OptionData GetOptions()
    {
        final OptionData options = new OptionData(OptionType.STRING, "newpassword", "New password for your game account password.", true);
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event)
    {
        if(!HelperFunctions.isAlphaNumeric(event.getOption("newpassword").getAsString()))
        {
            event.getHook().setEphemeral(true).sendMessage("Your username password may only contain numbers and letters.").queue();
            return;
        }

        HelperFunctions.SendSoapHandleResult(event, String.format("discord changepassword %s %s", event.getUser().getId(), event.getOption("newpassword").getAsString()),false);
    }
}
