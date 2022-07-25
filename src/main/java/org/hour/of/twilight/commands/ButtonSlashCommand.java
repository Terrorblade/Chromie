package org.hour.of.twilight.commands;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.hour.of.twilight.Interfaces.SlashCommandInterface;

import java.awt.*;


public class ButtonSlashCommand implements SlashCommandInterface
{
    @Override
    public SlashCommandData BuildCommand()
    {
        final SlashCommandData slash = Commands.slash("button", "Sends infographic for various means to channels.");
        slash.setGuildOnly(true);
        slash.setDefaultPermissions(DefaultMemberPermissions.DISABLED);
        slash.addOptions(GetOptions());
        return slash;
    }

    public OptionData GetOptions()
    {
        final OptionData options = new OptionData(OptionType.BOOLEAN, "downloadinfo", "Determine which infographic to send.", true);
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        event.getHook().setEphemeral(true).sendMessage("Sending message").queue();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor("Chromie", null, event.getJDA().getSelfUser().getAvatarUrl());
        eb.setImage("https://cdn.discordapp.com/attachments/745717735270383637/899416233114493018/HoTDiscordBannerLogoless.png");
        eb.setThumbnail("https://cdn.discordapp.com/attachments/745717735270383637/899416236205674506/HoTLogoThumbnail.png");
        eb.setColor(Color.decode("#634533"));
        if(!event.getOption("downloadinfo").getAsBoolean()) {
            eb.setDescription("Welcome to the Hour of Twilight server, below you will see buttons for account management and link to info about installing our latest patch!");
            event.getChannel().sendMessageEmbeds(eb.build()).setActionRow(
                            Button.primary(":register", "Register Account!"),
                            Button.secondary(":forgotusername", "Forgot Username?"),
                            Button.danger(":forgotpassword", "Forgot Password?"),
                            Button.link("https://discord.com/channels/693514576981131347/945008058097082421", "Patch Download"))
                    .queue();
        }
        else
        {
            eb.setTitle("Patch Download");
            eb.setDescription("The patch is only compatible with unmodified 3.3.5 WOTLK enUS or enGB clients." + System.lineSeparator() + "There are two options for downloading the client patch.");
            eb.addField("Automated Installation", "You can download our Blizzard Downloader tool that will automatically download and install the patch seamlessly. Simply place it in your WoW directory and run it.", event.getOption("inline").getAsBoolean());
            eb.addField("Manual Patch Install", "We understand not everyone is comfortable with downloading and running an unsigned executable. We provide the following ZIP archive which contains the patch-4.mpq which needs to be placed inside your WoW ``Data`` folder, and a ``Wow_HourOfTwilight.exe`` file which needs to be placed and run inside your WoW directory.", event.getOption("inline").getAsBoolean());
            eb.addField("Realmlist", "Update your realmlist file to: ``set realmlist logon.houroftwilight.net``",event.getOption("inline").getAsBoolean());
            event.getChannel().sendMessageEmbeds(eb.build()).setActionRow(Button.link("http://download.houroftwilight.net/patch/HourOfTwilight_Downloader.exe", "Automated Installer"),
                    Button.link("http://download.houroftwilight.net/patch/HourOfTwilight_Manual.zip", "Manual Patch Install"),
                    Button.link("https://discord.com/channels/693514576981131347/733563299811033118", "Account Management")).queue();

        }
    }
}
