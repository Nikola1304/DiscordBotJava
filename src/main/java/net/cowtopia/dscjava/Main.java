package net.cowtopia.dscjava;

import net.cowtopia.dscjava.libs.*;
import net.cowtopia.dscjava.listeners.ButtonListeners;
import net.cowtopia.dscjava.listeners.DiscordBot;
import net.cowtopia.dscjava.listeners.SlashListeners;
import net.cowtopia.dscjava.listeners.WelcomeLeaveListeners;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

import java.io.File;

import static net.dv8tion.jda.api.utils.MemberCachePolicy.ALL;

public class Main
{
    public static final long welcomechid = 910130241664602144L;
    public static final long leavechid = welcomechid;
    public static final long generalchid = 910094414175694879L;
    public static final long membersvcid = 1211789463475327067L;
    public static final long staffchid = 1220415121730306048L;
    public static final long suggestchid = 1213992313332699166L;
    public static final long ticketsCat = 1219763274141405214L;
    public static final long serverId = 817404696687673454L;
    public static String databaseName = "botbase.db";


    public static void main(String[] args) throws LoginException, InterruptedException
    {
        File f = new File(databaseName);
        if(!f.isFile()) {
            SqliteMan.createNewDatabase(databaseName);
            SqliteMan.connect(databaseName);
            SqliteMan.createWarningsTable(databaseName);
        }

        JDA bot = JDABuilder.createDefault(BOT_TOKEN_INSERT_HERE)
                .setActivity(Activity.watching("you. Ping me for help!"))
                .addEventListeners(new DiscordBot(), new ButtonListeners(), new SlashListeners(), new WelcomeLeaveListeners())
                // dozvole koje discord trazi da dodam iz nekog razloga (check msg content, check member)
                .enableIntents(GatewayIntent.GUILD_MEMBERS,GatewayIntent.GUILD_PRESENCES,GatewayIntent.GUILD_MESSAGES,GatewayIntent.MESSAGE_CONTENT)
                //.enableCache(CacheFlag.MEMBER_OVERRIDES,CacheFlag.ACTIVITY)
                .setMemberCachePolicy(ALL)
                .build().awaitReady();


        // Global and Guild Commands
        // Global Commands - can be used anywhere: any guild that your bot is in and also in DMs
        // njih dodajem tako sto umesto guild. dole napisem bot. direktno
        // ali njima se necu igrati
        // Guild Commands - can only be used in a specific guild (I need this)

        Guild guild = bot.getGuildById(serverId);

        // ignorisati ovo, test
        Command.Choice choiceSeconds = new Command.Choice("Seconds", 1);
        Command.Choice choiceMinutes = new Command.Choice("Minutes", 2);
        Command.Choice choiceHours = new Command.Choice("Hours", 3);
        Command.Choice choiceDays = new Command.Choice("Days", 4);
        Command.Choice choiceUser = new Command.Choice("User", 5);
        Command.Choice choiceMod = new Command.Choice("Moderator", 6);


        if(guild != null) {
            guild.upsertCommand("help","Helps you (in a way)").queue();
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
                                    .setRequiredRange(1,Integer.MAX_VALUE)
                    )
                    .queue();
            guild.upsertCommand("sup","Says sup to someone").queue();
            guild.upsertCommand("multiply","Multiplies two numbers").queue();
            guild.upsertCommand("invite","Sends you permanent server invite link").queue();
            guild.upsertCommand("ping","server ping").queue();
            guild.upsertCommand("members","Shows you amount of members in server").queue();
            guild.upsertCommand("slowmode-amount","Shows you slowmode duration in your current channel").queue();
            guild.upsertCommand("figlet","Creates ascii art from text")
                    .addOption(OptionType.STRING,"text","text you want converted to ascii art",true)
                    .queue();
            guild.upsertCommand("avatar","Presents you with someones avatar")
                    .addOption(OptionType.USER,"user","user you want avatar shown")
                    .queue();
            guild.upsertCommand("purge","Deletes specified amount of messages")
                    .addOptions(
                            new OptionData(OptionType.INTEGER,"amount","Amount of messages you want deleted",true)
                                    .setRequiredRange(1,100)
                    )
                    .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                    .queue();
            guild.upsertCommand("suggest","Posts a suggestion so people can vote on it")
                    .addOption(OptionType.STRING,"content","Thing youre suggesting",true)
                    .queue();
            guild.upsertCommand("warn","Warns a user")
                    .addOptions(
                            new OptionData(OptionType.USER,"user","user you want warned",true),
                            new OptionData(OptionType.STRING,"reason","reason you warned that user",false)
                    )
                    .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                    .queue();
            guild.upsertCommand("warnings","Lists all warnings user has")
                    .addOption(OptionType.USER,"user","user you want warnings shown",true)
                    .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                    .queue();
            guild.upsertCommand("delwarn","Delets warning")
                    .addOptions(
                            new OptionData(OptionType.INTEGER,"warningid", "ID of warning you want removed",true)
                                .setRequiredRange(1,999999999)
                    )
                    .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                    .queue();
            guild.upsertCommand("delwarnings","Delets all warnings made by mod or assigned to a certain user")
                    .addOptions(
                            new OptionData(OptionType.INTEGER,"usermod","Choose which warnings you want deleted",true)
                                    .addChoices(choiceUser, choiceMod),
                            new OptionData(OptionType.USER,"user","user with warns or mod who slapped those warns",true)
                    )
                    .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                    .queue();
            guild.upsertCommand("mute","Mutes user")
                    .addOptions(
                            new OptionData(OptionType.USER,"user","user you want muted",true),
                            new OptionData(OptionType.INTEGER,"amount","amount of time user will be muted",true)
                                    .setRequiredRange(1,2419200), // broj sekundi u 28 dana
                            new OptionData(OptionType.INTEGER, "type","Type of time measurement",true)
                                    .addChoices(choiceSeconds,choiceMinutes,choiceHours,choiceDays)
                            //, new OptionData(OptionType.STRING, "reason","Reason for muting user")
                    )
                    .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                    .queue();
            guild.upsertCommand("kick","Kicks user")
                    .addOptions(
                            new OptionData(OptionType.USER,"user","user you want kicking",true),
                            new OptionData(OptionType.STRING,"reason","Reason for kicking user",false)
                    )
                    .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                    .queue();
            guild.upsertCommand("ban","Bans user")
                    .addOptions(
                            new OptionData(OptionType.USER,"user","user you want banned",true),
                            new OptionData(OptionType.STRING,"reason","Reason for banning user",false)
                    )
                    .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                    .queue();
            guild.upsertCommand("ticket","Creates ticket")
                    .addOptions(
                            new OptionData(OptionType.STRING,"argument","Delete/Lock command",true)
                    )
                    .queue();
            guild.upsertCommand("slowmode","Sets slowmode in current channel")
                    .addOptions(
                            new OptionData(OptionType.INTEGER,"amount","New slowmode time (seconds)",true)
                                    .setRequiredRange(0,21600)
                    )
                    .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                    .queue();
            guild.upsertCommand("unmute","Unmutes user")
                    .addOptions(
                            new OptionData(OptionType.USER,"user","user you want unmuted",true)
                    )
                    //.addOption(OptionType.USER,"user","user you want unmuted",true)
                    .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                    .queue();
            guild.upsertCommand("lock","Locks channel").setDefaultPermissions(DefaultMemberPermissions.DISABLED).queue();
            guild.upsertCommand("unlock","Unlocks channel").setDefaultPermissions(DefaultMemberPermissions.DISABLED).queue();
            guild.upsertCommand("serverinfo","Shows server info").queue();
            guild.upsertCommand("embed","Creates example embed").queue();
            guild.upsertCommand("license","Shows you code license (maybe useful)").queue();

        }

        // ovo ne radi
        /*CommandListUpdateAction commands = bot.updateCommands();
        commands.addCommants(
                Commands.slash("fart","Fart really hard"),
                Commands.slash("food","Otkriva ti tajne univerzuma")
                        .addOption(OptionType.STRING, "foodname", "ime tvoje omiljene hrane",true)
        );
        command.queue();*/

        // ovaj line cini bota 10000x brzim
        System.out.println("Hello world!");
    }
}
