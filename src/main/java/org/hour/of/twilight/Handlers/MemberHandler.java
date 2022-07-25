package org.hour.of.twilight.Handlers;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.hour.of.twilight.utilities.HelperFunctions;
import org.hour.of.twilight.utilities.SoapHelper;
import org.xml.sax.SAXException;

import javax.annotation.Nonnull;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class MemberHandler extends ListenerAdapter
{

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event)
    {
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() ->
                SoapHelper.sendSoapRequest(String.format("discord status %s",
                event.getUser().getId()),true));
        stringCompletableFuture.thenApply(
                msg -> {
                    try {
                        return ReadStatusAndAssignRoles(event, msg, stringCompletableFuture);
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }
                    return msg;
                });
    }

    static String ReadStatusAndAssignRoles(GuildMemberJoinEvent event, String response, CompletableFuture<String> stringCompletableFuture) throws ParserConfigurationException, IOException, SAXException {
        String parsedMessage = HelperFunctions.ReadSoapXML(response);
        if(parsedMessage.contains("YesACC"))
        {
            HelperFunctions.GiveRole(event, "735923634098536522");

            if (parsedMessage.contains("Yes2FA"))
                HelperFunctions.GiveRole(event, "900154361525715004");

            if (parsedMessage.contains("YesAlpha"))
                HelperFunctions.GiveRole(event, "726158319974088877");
        }
        return "";
    }
}
