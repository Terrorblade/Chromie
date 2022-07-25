package org.hour.of.twilight.Handlers;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.hour.of.twilight.utilities.HelperFunctions;
import org.hour.of.twilight.utilities.SoapHelper;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.CompletableFuture;

public class ModalHandler  extends ListenerAdapter {
    @Override
    public void onModalInteraction(@Nonnull final ModalInteractionEvent event)
    {
        event.deferReply(true).queue();
        String[] id = event.getModalId().split(":"); // this is the custom id we specified in our button
        String authorId = id[0];
        String type = id[1];

        if (!authorId.equals(event.getUser().getId()) || !type.equals("registration"))
        {
            event.getHook().setEphemeral(true).sendMessage("Error occurred reading your input, please try again.").queue();
            return;
        }

        String username = event.getValue("username").getAsString();
        String password = event.getValue("password").getAsString();
        if(!HelperFunctions.isAlphaNumeric(username) || !HelperFunctions.isAlphaNumeric(password))
        {
            event.getHook().setEphemeral(true).sendMessage("Your username and password may only contain numbers and letters.").queue();
            return;
        }
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> SoapHelper.sendSoapRequest(String.format("discord registeraccount %s %s %s",
                event.getUser().getId(), username, password),false));
        stringCompletableFuture.thenApply(
                msg -> {
                    try {
                        return Response(event, username, msg, stringCompletableFuture);
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

    static String Response(ModalInteractionEvent event, String username, String response, CompletableFuture<String> stringCompletableFuture) throws ParserConfigurationException, IOException, SAXException {
        String userReply = "An internal error occurred, sorry for the inconvenience and try again. If this keeps happening contact us for assistance.";


        if (response.contains("<result>")) {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(response)));
            String commandResponse = doc.getElementsByTagName("result").item(0).getTextContent();
            if (commandResponse.length() > 0)
                userReply = commandResponse;
        }


        HelperFunctions.SimpleEmbed(event, "Account Register", userReply, true);
        if(userReply.contains("Account created"))
            HelperFunctions.GiveRole(event, "735923634098536522");
        //HelperFunctions.sendEphemeralMessage(event, response, true);
        stringCompletableFuture.complete("true");
        return "";
    }
}
