package net.cowtopia.dscjava.komande;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DiscordBot extends ListenerAdapter
{
    public static final String prefix = "!";
    static final long welcomechid = 910130241664602144L;
    static final long leavechid = welcomechid;
    static final long generalchid = 910094414175694879L;
    static final long membersvcid = 1211789463475327067L;


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {


        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();

        // ja ne kontam razliku izmedju ovih channela pa sam samo hakovao ovo jer ne znam sta bih drugo
        TextChannel guildChannelEvil = event.getGuild().getTextChannelById(channel.getId());


        User author = message.getAuthor();
        String mentionUser = author.getAsMention();




        if(author.isBot()) return;

        // problem: attachment moze da bude nesto drugo osim slike (zip, pdf, bla bla bla)
        // mozda problem: mozes da napises tekst kad posaljes sliku
        if(channel.getName().equals("images")) {
            if(message.getAttachments().isEmpty()) {
                message.delete().queue();
                return;
            }
        //if(channel.getId().equals("910103074490699826")) {
        }

        if(content.equals("ping")) {
            channel.sendMessage("Pong!").queue();
        }

        if(content.contains(event.getJDA().getSelfUser().getAsMention())) {
            // zadatak: promeni ovo u nesto korisno
            channel.sendMessage("Tagovanje bota funkcionise jejjjj").queue();
            return; // da sprecim da korisnik radi nesto sa botom
        }

        // KOMANDE SA PREFIXOM
        //
        //
        // donji tekst
        if(content.startsWith(prefix)) {
            String necmd = content.substring(1);
            String cmd = necmd.replaceAll("\\s.*", "").toLowerCase();
            //channel.sendMessage(cmd).queue();

            // pretvara celu poruku u niz stringova, onda mogu da biram sta mi treba :)
            // bug: ako stavim 2 razmaka napravi gluposti
            String[] niz_reci = necmd.split("\\s");

            if(cmd.equals("help")) {
                channel.sendMessage("Nema tebi pomoci decko").queue();
            }
            else if (cmd.equals("invite")) {
                message.reply("https://discord.gg/zrEUQENmRr").queue();
            }
            else if (cmd.equals("ping")) {

                // ukrao iz chat djipitija
                long startTime = System.currentTimeMillis(); // Record the current time
                channel.sendMessage("Pinging...").queue(response -> {
                    long endTime = System.currentTimeMillis(); // Record the time after receiving the message
                    // Calculate the bot's ping
                    long ping = endTime - startTime;
                    // Reply with the ping
                    response.editMessageFormat("Ping: %dms", ping).queue();
                });
            }
            else if(cmd.equals("ban")) {
                if(!message.getMember().hasPermission(Permission.BAN_MEMBERS)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `BAN_MEMBERS` permission").queue();
                    return;
                }

                // lista svih mentioned membera, sa koje cemo, ako nije prazna, skinuti prvog i njega banovati
                List<Member> banTargetovi = event.getMessage().getMentions().getMembers();

                // prebacio sam ovo u funkciju jer ce mi trebati za kick (jezivo znam)
                String reasonBan = bankickjezivafunkcija(niz_reci);

                if(!banTargetovi.isEmpty()) {
                    //User banUser = banTargetovi.get(0).getUser();
                    //banUser.openPrivateChannel().flatMap(chenl -> chenl.sendMessage("DMovan si")).queue();

                    event.getGuild().ban(banTargetovi.get(0),0, TimeUnit.MINUTES).reason(reasonBan).queue();
                            //.queueAfter(500, TimeUnit.MILLISECONDS); // timeunit odredjuje koliko ce da ide nazad i obrise poruke
                    channel.sendMessage(banTargetovi.get(0).getAsMention() + " has been banned!").queue();
                }
                else {
                    channel.sendMessage("Specify who you want banned").queue();
                }
            }
            else if (cmd.equals("kick")) {
                if(!message.getMember().hasPermission(Permission.KICK_MEMBERS)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `KICK_MEMBERS` permission").queue();
                    return;
                }

                List<Member> kickTargets = event.getMessage().getMentions().getMembers();
                String reasonKick = bankickjezivafunkcija(niz_reci);

                if(!kickTargets.isEmpty()) {
                    event.getGuild().kick(kickTargets.get(0)).reason(reasonKick).queue();
                    channel.sendMessage(kickTargets.get(0).getAsMention() + " has been kicked!").queue();
                }
                else {
                    channel.sendMessage("Specify who you want kicked").queue();
                }
            }
            else if (cmd.equals("mute")) {
                if(!message.getMember().hasPermission(Permission.MODERATE_MEMBERS)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `MODERATE_MEMBERS` permission").queue();
                    return;
                }

                List<Member> muteTargets = event.getMessage().getMentions().getMembers();

                TimeUnit vremenskaJedinica = TimeUnit.MINUTES;
                int duzinaMuta = 5;
                int jedinicaMuta = 2;
                //if(niz_reci.length == 1) channel.sendMessage("Specify who you want muted").queue();
                //else if (niz_reci.length == 2) channel.sendMessage("Give time").queue();
                //else if (niz_reci.length == 3) channel.sendMessage("Give time unit").queue();
                if (niz_reci.length >= 4)
                {
                    try
                    {
                        duzinaMuta = Integer.valueOf(niz_reci[2]);
                        jedinicaMuta = Integer.valueOf(niz_reci[3]);

                    } catch (NumberFormatException e)
                    {
                        // ako opet zaboravim da napisem .queue() mislim da ce se lose stvari desiti
                        channel.sendMessage("Input you provided is not a number!").queue();
                        return;
                    }
                }
                else if (niz_reci.length == 3) {
                    try
                    {
                        duzinaMuta = Integer.valueOf(niz_reci[2]);

                    }
                    catch (NumberFormatException e)
                    {
                        channel.sendMessage("Input you provided is not a number!").queue();
                        return;
                    }
                }

                if(duzinaMuta < 0) duzinaMuta = 5;

                if (jedinicaMuta == 1) {
                    vremenskaJedinica = TimeUnit.SECONDS;
                    if (duzinaMuta > 10800) duzinaMuta = 10800;
                }
                else if (jedinicaMuta == 3)
                {
                    vremenskaJedinica = TimeUnit.HOURS;
                    if (duzinaMuta > 169) duzinaMuta = 169;
                } else if (jedinicaMuta == 4)
                {
                    vremenskaJedinica = TimeUnit.DAYS;
                    if (duzinaMuta > 28) duzinaMuta = 28;
                } else
                {
                    vremenskaJedinica = TimeUnit.MINUTES;
                    if (duzinaMuta > 1400) duzinaMuta = 1400;
                }


                if(!muteTargets.isEmpty()) {
                    try {
                        event.getGuild().timeoutFor(muteTargets.get(0), duzinaMuta, vremenskaJedinica).queue();
                        channel.sendMessage(muteTargets.get(0).getAsMention() + " has been muted!").queue();
                    }
                    catch (HierarchyException e) {
                        channel.sendMessage("Sorry, I am not allowed to do that").queue();
                    }


                }
                else {
                    channel.sendMessage("Specify who you want muted").queue();
                }

            }
            else if (cmd.equals("unmute")) {
                if(!message.getMember().hasPermission(Permission.MODERATE_MEMBERS)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `MODERATE_MEMBERS` permission").queue();
                    return;
                }

                List<Member> unmuteTargets = event.getMessage().getMentions().getMembers();

                if(!unmuteTargets.isEmpty()) {
                    // ne proverava da li korisnik ima timeout, ali nije vazno
                    // ako ne moze da uradi to (fizicki, nema permission, member je jaci od njega), samo ce da baci error, fixaj to kasnije
                    event.getGuild().removeTimeout(unmuteTargets.get(0)).queue();
                    channel.sendMessage(unmuteTargets.get(0).getAsMention() + " has been unmuted!").queue();
                }
                else {
                    channel.sendMessage("Specify who you want unmuted").queue();
                }
            }
            else if (cmd.equals("unban")) {
                if (!message.getMember().hasPermission(Permission.BAN_MEMBERS)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `BAN_MEMBERS` permission").queue();
                    return;
                }

                if(niz_reci.length == 1)  channel.sendMessage("Specify who you want unbanned").queue();

                // fix this mess

                long userID;
                try {
                    userID = Long.valueOf(niz_reci[1]);
                }
                catch (NumberFormatException e ) {
                    channel.sendMessage("Input you provided is not a number!").queue();
                    return;
                }
                try {
                    User target = event.getJDA().getUserById(userID);
                    event.getGuild().unban(target);
                    channel.sendMessage("User with the id " + userID + " has been unbanned!");
                } catch (IllegalArgumentException e) {
                    // ako opet zaboravim da napisem .queue() mislim da ce se lose stvari desiti
                    channel.sendMessage("Input you provided is not an ID!").queue();
                    return;
                }
                //channel.sendMessage((CharSequence) event.getGuild().retrieveBanList()).queue();
            }
            else if (cmd.equals("slowmode")) {
                if(!message.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `MANAGE_CHANNEL` permission").queue();
                    return;
                }
                try {
                    // int slowmodeKojiUserZeli = Math.Abs(Integer.valueOf(niz_reci[1]));
                    short slowmodeKojiUserZeli = Short.valueOf(niz_reci[1]); // ne postoji razlog zasto sam ga ogranicio na short
                    if(slowmodeKojiUserZeli < 0) slowmodeKojiUserZeli *= -1;
                    if(slowmodeKojiUserZeli > 21600) slowmodeKojiUserZeli = 21600; // hard limit za slowmode

                    guildChannelEvil.getManager().setSlowmode(slowmodeKojiUserZeli).queue();
                    channel.sendMessage(mentionUser + " set the slowmode to " + slowmodeKojiUserZeli + " seconds").queue();

                } catch (NumberFormatException e) {
                    // ako opet zaboravim da napisem .queue() mislim da ce se lose stvari desiti
                    channel.sendMessage("Input you provided is not a number!").queue();
                    return;
                }
            }
            else if (cmd.equals("slowmode?")) {
                // iskreno ne volim ovo Integer.toString() u javi ali sta ces
                // i == je broken pa moras da koristis .equals()
                //String getSlowChannel = Integer.toString(guildChannelEvil.getSlowmode());

                channel.sendMessage("The current slowmode in this channel is `" + guildChannelEvil.getSlowmode() + "` seconds.").queue();
                // e i da ako citas ovo volim te
                // volim te i ako ne citas ali razumes poentu
                // <3
            }
            else if (cmd.equals("members")) {
                // problem: broji i botove, mogu da resim problem jednom FOR petljom ali bas mi se ne svidja to resenje
                channel.sendMessage("There are " + event.getGuild().getMemberCount() + " members in this server").queue();
            }
            else if (cmd.equals("say")) {
                // moj ID, jer drugi nisu dostojni ove komande
                if(author.getId().equals("716962243400564786")) {
                    message.delete().queue();
                    channel.sendMessage(content.substring(5)).queue();

                    // problem: prikazuje samo jednu rec iz cele poruke, jako me mrzi da popravljam, ovo iznad tehnicki radi iako nije najlepse
                    //channel.sendMessage(niz_reci[1]).queue();
                } else System.out.println(author.getName() + " pokusava da koristi ovu komandu");
            }
            else if (cmd.equals("embed")) {
                EmbedBuilder embedBuilder = new EmbedBuilder();

                // Set the title of the embed
                embedBuilder.setTitle("Example Embed");

                // Set the description of the embed
                embedBuilder.setDescription("This is a simple example of an embed message.");

                embedBuilder.addField("Fraza 1)", "Stuff", false);
                embedBuilder.addField("Fraza 2)", "Stuff", false);

                // Set the color of the embed
                embedBuilder.setColor(Color.GREEN);

                embedBuilder.setFooter("Bot created by person", event.getGuild().getOwner().getUser().getAvatarUrl());

                // Build the embed message
                MessageEmbed embed = embedBuilder.build();

                // Send the embed message to the channel
                channel.sendMessageEmbeds(embed).queue();
                //channel.sendMessage("message").setEmbeds(embedBuilder.build()).queue();
                embedBuilder.clear();

            }
            else if (cmd.equals("license")) {
                // nemam pojma da li je ovo vazno da uradim ali eto nek bude tu
                channel.sendMessage("https://github.com/Nikola1304/DiscordBotJava/blob/main/LICENSE").queue();
            }
        }

        // samo provera kada je bilo koja poruka poslata na server, ukloniti u finalnoj verziji da bi se smanjilo trosenje resursa
        //event.getChannel().sendMessage("This was sent: " + content).queue();
        System.out.println("Poruka je poslata (java prejaka)");
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event)
    {
        //Guild guild = event.getGuild();
        TextChannel welcomechannel = event.getGuild().getTextChannelById(welcomechid);

        // naravno sve ovo treba poboljsati ali baza je tu
        // dodati lepu porukicu, updatovanje channela sa member listama, potencijalno davanje nekih rolova

        //guild.getDefaultChannel().sendMessage("Welcome " + event.getUser().getAsMention() + " to the server!").queue();
        welcomechannel.sendMessage("Welcome " + event.getUser().getAsMention() + " to the server!").queue();


        // ovo nije najefikasnije na velikim serverima ali ako ikada postanem toliko veliki resavanje ovog problema bice jedan od manjih problema
        // takodje discord API ogranicava promene na channelima na dve promene u 10 minuta

        //event.getGuild().getVoiceChannelById(1211789463475327067L).getManager().setName("Total Users: " + event.getGuild().getMembers().size()).queue();
        // ubijam API ovime, ostavicu ga disabled za sada (27.02.2024.)
        //event.getGuild().getVoiceChannelById(membersvcid).getManager().setName("Members: " + event.getGuild().getMemberCount()).queue();
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        //Guild guild = event.getGuild();
        TextChannel leavechannel = event.getGuild().getTextChannelById(leavechid);
        // bruhh sta je ovo
        //List<TextChannel> leavechlist = event.getGuild().getTextChannelsByName("welcome",true);


        // Isto kao i za welcome
        leavechannel.sendMessage(event.getUser().getAsMention() + " has left the server.").queue();
        //event.getGuild().getVoiceChannelById(membersvcid).getManager().setName("Members: " + event.getGuild().getMemberCount()).queue();
    }

    // potencijalno maknuti deo koda ispod ovog komentara
    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent event) {
        String channelName = event.getChannel().getName();

        TextChannel general = event.getGuild().getTextChannelById(generalchid);

        if(general != null) {
            general.sendMessage("The channel: \"" + channelName + "\" voz dilited").queue();
        }
    }

    private String bankickjezivafunkcija(String[] niz_reci) {

        // bukvalno me ne osudjujte zbog ovoga
        String reasonBan = "bug";
        if(niz_reci.length == 2) reasonBan = "No reason provided. " + DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
        else if (niz_reci.length == 3) {
            reasonBan = niz_reci[2];
        }
        else if (niz_reci.length > 3) {
            reasonBan = niz_reci[2];
            for (int i = 3; i < niz_reci.length; i++) {
                reasonBan = reasonBan + " " + niz_reci[i];
            }
        }
        return reasonBan;
    }
}
