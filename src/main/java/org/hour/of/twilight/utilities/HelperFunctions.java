package org.hour.of.twilight.utilities;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.CompletableFuture;

public class HelperFunctions
{
    public static void sendPrivateMessage(User user, String content)
    {
        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(content))
                .queue();
    }

    public static void sendEphemeralMessage(SlashCommandInteractionEvent event, String message)
    {
        sendEphemeralMessage(event, message, false);
    }

    public static void sendEphemeralMessage(ButtonInteractionEvent event, String message)
    {
        sendEphemeralMessage(event, message, false);
    }

    public static void sendEphemeralMessage(SlashCommandInteractionEvent event, String message, boolean ignoreDefer)
    {
        if(!ignoreDefer)
            event.deferReply(true).queue();
        event.getHook().setEphemeral(true).sendMessage(message).queue();
    }

    public static void sendEphemeralMessage(ButtonInteractionEvent event, String message, boolean ignoreDefer)
    {
        if(!ignoreDefer)
            event.deferReply(true).queue();
        event.getHook().setEphemeral(true).sendMessage(message).queue();
    }

    public static void SendSoapHandleResult(SlashCommandInteractionEvent event, String command, boolean realm)
    {
        event.deferReply(true).queue();

        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> SoapHelper.sendSoapRequest(command, realm));
        stringCompletableFuture.thenApply(
                msg -> {
                    try {
                        return ReadSoapResultAndSend(event, msg, stringCompletableFuture);
                    } catch (Exception e) {
                        SendToBotLogs(event, e.getMessage());
                        e.printStackTrace();
                    }
                    stringCompletableFuture.cancel(true);
                    sendEphemeralMessage(event, "An parsing server response.", true);
                    return msg;
                });
    }

    private static String ReadSoapResultAndSend(SlashCommandInteractionEvent event, String response, CompletableFuture<String> stringCompletableFuture) throws ParserConfigurationException, IOException, SAXException
    {
        //HelperFunctions.sendEphemeralMessage(event, response, true);
        if(response.length() > 0)
        {
            if(response.contains("<result>")){
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(response)));
                String commandResponse = doc.getElementsByTagName("result").item(0).getTextContent();
                if (commandResponse.length() > 0)
                    SimpleEmbed(event, "Command Result", commandResponse, true);
                //sendEphemeralMessage(event, commandResponse, true);
            }
            else {
                SendToBotLogs(event, response);
                sendEphemeralMessage(event, "An error occurred sending the command to the server.", true);
            }
        }

        stringCompletableFuture.complete("true");
        return "a";
    }

    public static void SendSoapHandleResult(ButtonInteractionEvent event, String command, boolean realm)
    {
        //event.deferReply(true).queue();

        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> SoapHelper.sendSoapRequest(command, realm));
        stringCompletableFuture.thenApply(
                msg -> {
                    try {
                        return ReadSoapResultAndSend(event, msg, stringCompletableFuture);
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }
                    stringCompletableFuture.cancel(true);
                    sendEphemeralMessage(event, "An parsing server response.", true);
                    return msg;
                });
    }

    private static String ReadSoapResultAndSend(ButtonInteractionEvent event, String response, CompletableFuture<String> stringCompletableFuture) throws ParserConfigurationException, IOException, SAXException
    {
        //HelperFunctions.sendEphemeralMessage(event, response, true);
        if(response.length() > 0)
        {
            if(response.contains("<result>")){
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(response)));
                String commandResponse = doc.getElementsByTagName("result").item(0).getTextContent();
                if (commandResponse.length() > 0)
                    sendEphemeralMessage(event, commandResponse, true);
            }
            else
                sendEphemeralMessage(event, "An error occurred sending the command to the server.", true);
        }

        stringCompletableFuture.complete("true");
        return "a";
    }

    public static String ReadSoapXML(String soap) throws ParserConfigurationException, IOException, SAXException
    {
        if(soap.length() > 0)
        {
            if(soap.contains("<result>")){
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(soap)));
                String commandResponse = doc.getElementsByTagName("result").item(0).getTextContent();
                if (commandResponse.length() > 0)
                    return commandResponse;
            }
        }
        return ""; //Don't expose whatever was sent.
    }

    public static void SendToBotLogs(SlashCommandInteractionEvent event, MessageEmbed message)
    {
        final TextChannel textChannelById = event.getGuild().getTextChannelById("734980082627510284");
        textChannelById.sendMessageEmbeds(message).queue();
    }

    public static void SendToBotLogs(SlashCommandInteractionEvent event, String message)
    {
        final TextChannel textChannelById = event.getGuild().getTextChannelById("734980082627510284");
        textChannelById.sendMessage(message).queue();
    }

    public static void SimpleEmbed(SlashCommandInteractionEvent event, String title, String message, boolean logo)
    {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor("Chromie",null, event.getJDA().getSelfUser().getAvatarUrl());
        eb.setTitle(title);
        if(logo)
            eb.setThumbnail("https://cdn.discordapp.com/attachments/745717735270383637/899416236205674506/HoTLogoThumbnail.png");
        eb.setDescription(message);
        eb.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        event.getHook().setEphemeral(true).sendMessageEmbeds(eb.build()).queue();
    }

    public static void SimpleEmbed(ModalInteractionEvent event, String title, String message, boolean logo)
    {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor("Chromie",null, event.getJDA().getSelfUser().getAvatarUrl());
        eb.setTitle(title);
        if(logo)
            eb.setThumbnail("https://cdn.discordapp.com/attachments/745717735270383637/899416236205674506/HoTLogoThumbnail.png");
        eb.setDescription(message);
        eb.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        event.getHook().setEphemeral(true).sendMessageEmbeds(eb.build()).queue();
    }

    public static void GiveRole(SlashCommandInteractionEvent event, String role)
    {
        Guild g = event.getJDA().getGuildById("693514576981131347");
        g.addRoleToMember(event.getUser(), g.getRoleById(role)).queue();
    }

    public static void GiveRole(ButtonInteractionEvent event, String role)
    {
        Guild g = event.getJDA().getGuildById("693514576981131347");
        g.addRoleToMember(event.getUser(), g.getRoleById(role)).queue();
    }

    public static void GiveRole(GuildMemberJoinEvent event, String role)
    {
        Guild g = event.getJDA().getGuildById("693514576981131347");
        g.addRoleToMember(event.getUser(), g.getRoleById(role)).queue();
    }

    public static void GiveRole(ModalInteractionEvent event, String role)
    {
        Guild g = event.getJDA().getGuildById("693514576981131347");
        g.addRoleToMember(event.getUser(), g.getRoleById(role)).queue();
    }
    public static boolean isAlphaNumeric(String s) {
        return s != null && s.matches("^[a-zA-Z0-9]*$");
    }
}
