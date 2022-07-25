package org.hour.of.twilight.commands;

import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.hour.of.twilight.Interfaces.CommandInterface;

public class LookupPlayerCommand implements CommandInterface {

    @Override
    public CommandData BuildCommand()
    {
        CommandData command = Commands.user("lookup");
        command.setGuildOnly(true);
        command.setDefaultPermissions(DefaultMemberPermissions.DISABLED);
        return command;
    }

    @Override
    public void execute(UserContextInteractionEvent event)
    {
        event.deferReply(true).queue();
        event.getHook().setEphemeral(true).sendMessage("Hello, you interacted with " + event.getTarget().getName() + " :)").queue();
    }
}
