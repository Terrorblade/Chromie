package org.hour.of.twilight.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.hour.of.twilight.Interfaces.SlashCommandInterface;
import org.hour.of.twilight.utilities.HelperFunctions;
import org.hour.of.twilight.utilities.SoapHelper;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class RegisterKeySlashCommand implements SlashCommandInterface
{
    @Override
    public SlashCommandData BuildCommand()
    {
        final SlashCommandData slash = Commands.slash("alphakey", "Register a key for alpha access.");
        slash.addOptions(GetOptions());
        return slash;
    }

    public OptionData GetOptions()
    {
        final OptionData options = new OptionData(OptionType.STRING, "key", "Alpha Key your are registering to your account.", true);
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event)
    {
        event.deferReply(true).queue();
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> SoapHelper.sendSoapRequest(String.format("discord setup2fa %s", event.getUser().getId()), false));
        stringCompletableFuture.thenApply(
                msg -> {
                    try {
                        return Response(event, msg, stringCompletableFuture);
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }
                    stringCompletableFuture.complete("true");
                    return msg;
                });
    }

    static String Response(SlashCommandInteractionEvent event, String response, CompletableFuture<String> stringCompletableFuture) throws ParserConfigurationException, IOException, SAXException {
        String parsedResponse = HelperFunctions.ReadSoapXML(response);
        if(parsedResponse.contains("You redeemed"))
        {
            HelperFunctions.SimpleEmbed(event, "Alpha Register", "You have been granted access to our alpha test, check <#735910519260577896> for our latest announcements regarding alphas and latest patch links.", true);
            HelperFunctions.GiveRole(event, "726158319974088877");
        }
        else
            HelperFunctions.SimpleEmbed(event, "Alpha Register", "There was an error registering your key, most likely it was invalid.", true);

        stringCompletableFuture.complete("true");
        return "";
    }
}
