package org.hour.of.twilight.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.hour.of.twilight.Interfaces.SlashCommandInterface;


public class HelpSlashCommand implements SlashCommandInterface
{
    @Override
    public SlashCommandData BuildCommand()
    {
        final SlashCommandData slash = Commands.slash("help", "Display various helpful links and commands for our server.");
        return slash;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event)
    {
        EmbedBuilder eb = new EmbedBuilder();
        boolean isDeveloper =  event.getJDA().getGuildById("693514576981131347").getMember(event.getUser()).hasPermission(Permission.ADMINISTRATOR); //event.getMember().hasPermission(Permission.ADMINISTRATOR);
        eb.setAuthor("Chromie",null, event.getJDA().getSelfUser().getAvatarUrl());
        if(isDeveloper) {
            eb.addField("Button", "/button will output the help message in <#733563299811033118>.", false);
            eb.addField("Rcon", "/rcon <dev/live> will send a remote command to the server.", false);
            eb.addField("Setstatus", "/setstatus will set the bot's current status.", false);
        }
        eb.addField("Register", "/register will allow you register an account to our server, we currently limit 1 per discord account.", false);
        eb.addField("Alpha Key","/alphakey will allow you to register an alpha key for access to our alpha tests.", false);
        eb.addField("2fa", "/2fa <setup/finishsetup> will allow you to enable two factor authenication via google authenicator or similar on your game account.", false);
        eb.addField("Changepassword", "/changepassword allows you to change the password to your account.", false);
        eb.addField("Forgotusername","/forgotusername will recover your username to your account.", false);
        eb.addField("Forgotpassword","/forogotpassword will reset your password to a random password in the case you forgot yours.", false);
        event.replyEmbeds(eb.build()).setEphemeral(true).queue();
    }
}
