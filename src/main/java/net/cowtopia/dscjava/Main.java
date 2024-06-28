package net.cowtopia.dscjava;

import net.cowtopia.dscjava.commands.fun.*;
import net.cowtopia.dscjava.commands.help.*;
import net.cowtopia.dscjava.commands.moderation.*;
import net.cowtopia.dscjava.libs.*;
import net.cowtopia.dscjava.listeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import javax.security.auth.login.LoginException;
import java.io.*;
import static net.dv8tion.jda.api.utils.MemberCachePolicy.ALL;

public class Main
{
    // this is hardcoded because I don't care about preferences
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) throws LoginException, InterruptedException, FileNotFoundException {
        GSonConfig json_config = GSonConfig.get();
        final String config = "config.json";

        if(createConfig(config)) return;
        createRules("rules.txt");

        if (json_config.getDBName().equals("OBJECT_NOT_FOUND.db")) {
            // possible bug: person named the file this way, but if that's the case I really don't care
            // maybe I could write this to token but it feels unsafe to read token more times than necessary
            System.out.println(ANSI_RED + "Please fill out the " + config + " with all required information" + ANSI_RESET);
            return;
        }

        createDatabase(json_config);

        // RSSFetchThread objekat = new RSSFetchThread();
        // objekat.start();

        // manager.add(new ());

        CommandManager manager = new CommandManager();

        // fun
        manager.add(new Avatar());
        manager.add(new Cat());
        manager.add(new Embed());
        manager.add(new Figlet());
        manager.add(new Food());
        manager.add(new Multiply());
        manager.add(new Ping());
        manager.add(new Sum());
        manager.add(new Sup());
        manager.add(new UpTime());

        // help
        manager.add(new Help());
        manager.add(new Invite());
        manager.add(new License());
        manager.add(new Members());
        manager.add(new Rules());
        manager.add(new ServerInfo());
        manager.add(new Suggest());
        manager.add(new Ticket());

        // moderation
        manager.add(new Ban());
        manager.add(new DelWarn());
        manager.add(new DelWarnings());
        manager.add(new Kick());
        manager.add(new Lock());
        manager.add(new Mute());
        manager.add(new Purge());
        manager.add(new Slowmode());
        manager.add(new SlowmodeAmount());
        manager.add(new Unlock());
        manager.add(new Unmute());
        manager.add(new Warn());
        manager.add(new Warnings());

        JDA bot;
        try {
            bot = JDABuilder.createDefault(json_config.getToken())
                    .setActivity(Activity.watching("you. Ping me for help!"))
                    .addEventListeners(new DiscordBot(), new ButtonListeners(), new WelcomeLeaveListeners(), manager)
                    // dozvole koje discord trazi da dodam iz nekog razloga (check msg content, check member)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                    //.enableCache(CacheFlag.MEMBER_OVERRIDES,CacheFlag.ACTIVITY)
                    .setMemberCachePolicy(ALL)
                    .build().awaitReady();
        } catch (InvalidTokenException e) {
            System.out.println(ANSI_RED + "Invalid token provided!" + ANSI_RESET);
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("Token may be null");
            return;
        }


        // Global and Guild Commands
        // Global Commands - can be used anywhere: any guild that your bot is in and also in DMs
        // njih dodajem tako sto umesto guild. dole napisem bot. direktno
        // ali njima se necu igrati
        // Guild Commands - can only be used in a specific guild (I need this)



        // ovaj line cini bota 10000x brzim
        System.out.println(ANSI_GREEN + "Hello world!" + ANSI_RESET);
    }

    private static boolean createConfig(String configFile) throws FileNotFoundException {

        // I'm too lazy to write something smarter
        File cj = new File(configFile);
        if (!cj.isFile()) {
            PrintWriter writer = new PrintWriter(configFile);
            writer.println("{");
            writer.println("    \"bot_token\": \"\",");
            writer.println("    \"server_id\": \"\",");
            writer.println("    \"db_name\": \"botbase\",");
            writer.println("    \"welcome_ch_id\": \"\",");
            writer.println("    \"leave_ch_id\": \"\",");
            writer.println("    \"members_vc_id\": \"\",");
            writer.println("    \"staff_ch_id\": \"\",");
            writer.println("    \"suggest_ch_id\": \"\",");
            writer.println("    \"tickets_cat\": \"\"");
            writer.println("}");
            writer.close();

            System.out.println(ANSI_GREEN + "Please fill out the " + configFile + " with all required information" + ANSI_RESET);
            return true;
        }
        else return false;
    }

    private static void createRules(String ruleFile) throws FileNotFoundException {
        File rulesTxt = new File(ruleFile);
        if (!rulesTxt.isFile()) {
            PrintWriter writer = new PrintWriter(ruleFile);
            writer.println("Add rules to " + ruleFile + " and restart bot");
            writer.close();
        }
    }

    private static void createDatabase(GSonConfig json_config) throws FileNotFoundException {
        File f = new File(json_config.getDBName());
        if (!f.isFile()) {
            SqliteMan.createNewDatabase(json_config.getDBName());
            SqliteMan.connect(json_config.getDBName());
            SqliteMan.createWarningsTable(json_config.getDBName());
            System.out.println(ANSI_GREEN + "Database file has been successfully created!" + ANSI_RESET);
        }
    }
}