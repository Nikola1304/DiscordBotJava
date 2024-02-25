package net.cowtopia.dscjava;


import net.cowtopia.dscjava.komande.DiscordBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Main
{
    public static void main(String[] args) throws LoginException
    {
        JDA bot = JDABuilder.createDefault("BOT_TOKEN")
                .setActivity(Activity.watching("you. Ping me for help!"))
                .addEventListeners(new DiscordBot())
                // dozvole koje discord trazi da dodam iz nekog razloga (check msg content, check member)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT,GatewayIntent.GUILD_MEMBERS)
                .build();
        // ovaj line cini bota 10000x brzim
        System.out.println("Hello world!");
    }
}