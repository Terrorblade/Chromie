package org.hour.of.twilight.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.hour.of.twilight.Interfaces.SlashCommandInterface;

import java.awt.*;

public class ForgotPasswordSlashCommand implements SlashCommandInterface
{
    @Override
    public SlashCommandData BuildCommand()
    {
        final SlashCommandData slash = Commands.slash("forgotpassword", "Recover your password.");
        return slash;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event)
    {
        event.deferReply(true).queue();
        InteractionHook hook = event.getHook();
        hook.setEphemeral(true);
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Confirmation");
        eb.setColor(Color.red);
        eb.setDescription("Are you sure you forget your password? We will generate a new one for you to use.");
        hook.sendMessageEmbeds(eb.build()).addActionRow(
                Button.primary(event.getUser().getId() + ":confirmedforgotpassword", "Yes"),
                Button.secondary(event.getUser().getId() + ":declinedforgotpassword", "No")).queue();
    }
}
