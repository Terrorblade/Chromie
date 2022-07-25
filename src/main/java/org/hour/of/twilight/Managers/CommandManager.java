package org.hour.of.twilight.Managers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.hour.of.twilight.Interfaces.CommandInterface;
import org.hour.of.twilight.Interfaces.SlashCommandInterface;
import org.hour.of.twilight.commands.*;
import org.hour.of.twilight.utilities.ConfigHelper;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class CommandManager extends ListenerAdapter {
    Map<String, SlashCommandInterface> registeredCommands;
    Map<String, CommandInterface> registeredUserCommands;
    JDA jdaInstance;

    @Override
    public void onReady(@NotNull final ReadyEvent event) {
        jdaInstance = event.getJDA();
        RegisterCommands();
        UpsertCommands();
    }

    public void RegisterCommands()
    {
        registeredCommands = new HashMap<>();
        registeredUserCommands = new HashMap<>();
        registeredCommands.put("button", new ButtonSlashCommand());
        registeredCommands.put("changepassword", new ChangePasswordSlashCommand());
        registeredCommands.put("forgotpassword", new ForgotPasswordSlashCommand());
        registeredCommands.put("forgotusername", new ForgotUsernameSlashCommand());
        registeredCommands.put("giveaway", new GiveawaySlashCommand());
        registeredCommands.put("help", new HelpSlashCommand());
        registeredCommands.put("rcon", new RconSlashCommand());
        registeredCommands.put("register", new RegisterSlashCommand());
        registeredCommands.put("registerkey", new RegisterKeySlashCommand());
        registeredCommands.put("status", new StatusSlashCommand());
        registeredUserCommands.put("lookup", new LookupPlayerCommand());
        //registeredCommands.put("twofactor", new TwoFactorCommand());
    }

    public void UpsertCommands()
    {
        if(ConfigHelper.updateCommands)
        {
            Guild guild = jdaInstance.getGuildById("693514576981131347");
            if(guild == null)
            {
                System.err.println("Couldn't find guild, closing.");
                System.exit(1);
            }
            List<SlashCommandData> registerCommands = new Vector<>();
            List<CommandData> registerUserCommands = new Vector<>();
            for (Map.Entry<String, SlashCommandInterface> commands : registeredCommands.entrySet())
            {
                System.out.println(commands.getValue());
                registerCommands.add(commands.getValue().BuildCommand());
            }
            for (Map.Entry<String, CommandInterface> commands : registeredUserCommands.entrySet())
            {
                System.out.println(commands.getValue());
                registerUserCommands.add(commands.getValue().BuildCommand());
            }
            guild.updateCommands().addCommands(registerCommands).addCommands(registerUserCommands).queue();
            ConfigHelper.UpdateConfig("updateCommands", "false");
        }
    }

    @Override
    public void onSlashCommandInteraction(final SlashCommandInteractionEvent event)
    {
        SlashCommandInterface foundSlashCommand = registeredCommands.get(event.getName());
        if(foundSlashCommand != null)
            foundSlashCommand.execute(event);
        else
        {
            event.deferReply(true).queue();
            event.getHook().setEphemeral(true).sendMessage("Error executing command, if this continues happens contact a member of staff.").queue();
        }
    }

    @Override
    public void onUserContextInteraction(final UserContextInteractionEvent event)
    {
        CommandInterface foundCommand = registeredUserCommands.get(event.getName());
        if (foundCommand != null)
            foundCommand.execute(event);
        else
        {
            event.deferReply(true).queue();
            event.getHook().setEphemeral(true).sendMessage("Error executing command, if this continues shits broke.").queue();
        }
    }

}
