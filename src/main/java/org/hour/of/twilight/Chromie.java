package org.hour.of.twilight;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.hour.of.twilight.Handlers.ButtonHandler;
import org.hour.of.twilight.Handlers.MemberHandler;
import org.hour.of.twilight.Handlers.ModalHandler;
import org.hour.of.twilight.Managers.CommandManager;
import org.hour.of.twilight.utilities.ConfigHelper;

import javax.security.auth.login.LoginException;

public class Chromie
{
    public static void main(String[] args) throws LoginException, InterruptedException {
        ConfigHelper.LoadConfig();
        JDA jda = JDABuilder.create(ConfigHelper.botToken, GatewayIntent.GUILD_MEMBERS) // Member intent for join events
                .addEventListeners(new ButtonHandler())
                .addEventListeners(new MemberHandler())
                .addEventListeners(new CommandManager())
                .addEventListeners(new ModalHandler())
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.ONLINE_STATUS, CacheFlag.VOICE_STATE, CacheFlag.CLIENT_STATUS, CacheFlag.MEMBER_OVERRIDES,CacheFlag.EMOJI, CacheFlag.STICKER)
                .build().awaitReady();

        jda.getPresence().setActivity(Activity.watching("the timeline"));

    }
}
