package net.cowtopia.dscjava;


import net.cowtopia.dscjava.komande.DiscordBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Main
{
    public static void main(String[] args) throws LoginException, InterruptedException
    {

        JDA bot = JDABuilder.createDefault(BOT_TOKEN_INSERT_HERE)
                .setActivity(Activity.watching("you. Ping me for help!"))
                .addEventListeners(new DiscordBot())
                // dozvole koje discord trazi da dodam iz nekog razloga (check msg content, check member)
                .enableIntents(GatewayIntent.GUILD_MEMBERS,GatewayIntent.GUILD_PRESENCES,GatewayIntent.GUILD_MESSAGES,GatewayIntent.MESSAGE_CONTENT)
                //.enableCache(CacheFlag.MEMBER_OVERRIDES,CacheFlag.ACTIVITY)
                .build().awaitReady();
        // ovaj line cini bota 10000x brzim
        System.out.println("Hello world!");

        // Global and Guild Commands
        // Global Commands - can be used anywhere: any guild that your bot is in and also in DMs
        // njih dodajem tako sto umesto guild. dole napisem bot. direktno
        // ali njima se necu igrati
        // Guild Commands - can only be used in a specific guild (I need this)

        Guild guild = bot.getGuildById(817404696687673454L);

        // mrzim chatgpt shizofreniju
        /*
        Command.Choice choiceSeconds = new Command.Choice("Seconds", "seconds");
        Command.Choice choiceMinutes = new Command.Choice("Minutes", "minutes");
        Command.Choice choiceHours = new Command.Choice("Hours", "hours");
        Command.Choice choiceDays = new Command.Choice("Days", "days");*/


        if(guild != null) {
            guild.upsertCommand("fart","Fart really hard").queue();
            guild.upsertCommand("food","Otkriva ti tajne univerzuma")
                    .addOption(OptionType.STRING, "foodname", "ime tvoje omiljene hrane",true)
                    .queue();
            guild.upsertCommand("sum","add two numbers together")
                    //.addOption(OptionType.INTEGER,"operand1","the first number",true)
                    //.addOption(OptionType.INTEGER,"operand2","the second number",true)
                    .addOptions(
                            new OptionData(OptionType.INTEGER,"operand1","the first number",true)
                                    .setRequiredRange(1,Integer.MAX_VALUE),
                            new OptionData(OptionType.INTEGER,"operand2","the second number",true)
                                    .setRequiredRange(1,Integer.MAX_VALUE))
                    .queue();
            guild.upsertCommand("ping","server ping").queue();
            guild.upsertCommand("members","Shows you amount of members in server").queue();
            guild.upsertCommand("slowmode-amount","Shows you slowmode duration in your current channel").queue();
            guild.upsertCommand("avatar","Presents you with someones avatar")
                    .addOption(OptionType.USER,"user","user you want avatar shown")
                    .queue();
            guild.upsertCommand("mute","Mutes user")
                    .addOptions(
                            new OptionData(OptionType.USER,"user","user you want muted",true),
                            new OptionData(OptionType.INTEGER,"amount","amount of time user will be muted",true)
                                    .setRequiredRange(1,2419200), // broj sekundi u 28 dana
                            new OptionData(OptionType.INTEGER, "type","Type of time measurement",true)
                            //, new OptionData(OptionType.STRING, "reason","Reason for muting this user")
                    )
                    .queue();
        }

        // ovo ne radi
        /*CommandListUpdateAction commands = bot.updateCommands();
        commands.addCommants(
                Commands.slash("fart","Fart really hard"),
                Commands.slash("food","Otkriva ti tajne univerzuma")
                        .addOption(OptionType.STRING, "foodname", "ime tvoje omiljene hrane",true)
        );
        command.queue();*/
    }
}
