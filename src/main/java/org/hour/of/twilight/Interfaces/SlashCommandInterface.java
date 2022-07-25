package org.hour.of.twilight.Interfaces;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;


public interface SlashCommandInterface {
    SlashCommandData BuildCommand();

    void execute(SlashCommandInteractionEvent event);
}
