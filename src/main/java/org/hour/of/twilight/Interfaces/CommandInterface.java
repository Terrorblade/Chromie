package org.hour.of.twilight.Interfaces;

import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface CommandInterface {
    CommandData BuildCommand();
    void execute(UserContextInteractionEvent event);
}
