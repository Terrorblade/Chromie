package org.hour.of.twilight.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.hour.of.twilight.Interfaces.SlashCommandInterface;

public class RegisterSlashCommand implements SlashCommandInterface
{
    @Override
    public SlashCommandData BuildCommand()
    {
        final SlashCommandData slash = Commands.slash("register", "Opens up our registration form.");
        return slash;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event)
    {
        TextInput username = TextInput.create("username", "Game Account Username", TextInputStyle.SHORT)
                .setPlaceholder("Enter your username.")
                .setRequiredRange(3, 16)
                .build();

        TextInput password = TextInput.create("password", "Game Account Password", TextInputStyle.SHORT)
                .setPlaceholder("Enter your password.")
                .setRequiredRange(3, 16)
                .build();

        Modal modal = Modal.create(event.getUser().getId() +  ":registration", "Registration Form")
                .addActionRows(ActionRow.of(username), ActionRow.of(password))
                .build();

        event.replyModal(modal).queue();
    }
}
