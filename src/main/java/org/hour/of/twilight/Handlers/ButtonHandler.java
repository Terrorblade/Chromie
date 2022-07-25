package org.hour.of.twilight.Handlers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.hour.of.twilight.utilities.HelperFunctions;

import java.awt.*;

public class ButtonHandler  extends ListenerAdapter
{
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event)
    {
        String[] id = event.getComponentId().split(":");
        String authorId = id[0];
        String type = id[1];

        event.deferEdit().queue();

        switch (type)
        {
            case "register": {
                OnRegisterEvent(event);
                break;
            }
            case "forgotpassword": {
                OnForgotPasswordEvent(event);
                break;
            }
            case "confirmedforgotpassword": {
                if (!authorId.equals(event.getUser().getId()))
                {
                    event.getHook().setEphemeral(true).sendMessage("We were unable to process that, please again later.").queue();
                    break;
                }
                OnConfirmedForgotPassword(event);
                break;
            }
            case "forgotusername": {
                OnForgotUsernameEvent(event);
                break;
            }
            case "declinedforgotpassword": {
                if (!authorId.equals(event.getUser().getId()))
                {
                    event.getHook().setEphemeral(true).sendMessage("We were unable to process that, please again later.").queue();
                    break;
                }
                OnDeclinedForgotPassword(event);
                break;
            }
        }
    }

    static private void OnRegisterEvent(ButtonInteractionEvent event)
    {
        InteractionHook hook = event.getHook();
        hook.setEphemeral(true);
        MessageBuilder mb = new MessageBuilder();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Registration Help");
        eb.setColor(Color.blue);
        eb.setDescription("It is important that after typing \"/register\" you press enter to register the command. It will then prompt for your username and password, which you can press tab to switch between.");
        eb.addField("Important Notice", "It is important a pop up above your chat appears when typing the command else it will not work and will send like a normal message.", false);
        eb.setImage("https://i.gyazo.com/9d69ff745f32c9b38942f21ec7ad9e1c.png");
        mb.setEmbeds(eb.build());
        hook.sendMessage(mb.build()).queue();
    }

    static private void OnForgotPasswordEvent(ButtonInteractionEvent event)
    {
        InteractionHook hook = event.getHook();
        hook.setEphemeral(true);
        MessageBuilder mb = new MessageBuilder();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Confirmation");
        eb.setColor(Color.red);
        eb.setDescription("Are you sure you forget your password? We will generate a new one for you to use.");
        mb.setEmbeds(eb.build());
        hook.sendMessage(mb.build()).addActionRow(
                Button.primary(event.getUser().getId() + ":confirmedforgotpassword", "Yes"),
                Button.secondary(event.getUser().getId() + ":declinedforgotpassword", "No")).queue();
    }

    static private void OnConfirmedForgotPassword(ButtonInteractionEvent event)
    {
        event.getHook().editOriginal(event.getMessage()).setActionRow(
                Button.primary(event.getUser().getId() + ":confirmedforgotpassword", "Yes").asDisabled(),
                Button.secondary(event.getUser().getId() + ":declinedforgotpassword", "No").asDisabled()).queue();
        HelperFunctions.SendSoapHandleResult(event, String.format("discord forgotpassword %s", event.getUser().getId()),false);
    }

    static private void OnForgotUsernameEvent(ButtonInteractionEvent event)
    {
        HelperFunctions.SendSoapHandleResult(event, String.format("discord forgotusername %s", event.getUser().getId()),false);
    }

    static private void OnDeclinedForgotPassword(ButtonInteractionEvent event)
    {
        event.getHook().editOriginal(event.getMessage()).setActionRow(
                Button.primary(event.getUser().getId() + ":confirmedforgotpassword", "Yes").asDisabled(),
                Button.secondary(event.getUser().getId() + ":declinedforgotpassword", "No").asDisabled()).queue();
    }
}
