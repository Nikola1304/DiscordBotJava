package net.cowtopia.dscjava.komande;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class DiscordBot extends ListenerAdapter
{
    static final String prefix = "!";
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


        String mentionUser = message.getAuthor().getAsMention();


        if(event.getAuthor().isBot()) return;

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
            channel.sendMessage("radi").queue();
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
                channel.sendMessage("Nije zavrseno, ali makar ima permission sistem :)").queue();

                
                // zavrsi ovaj code
                // stavio sam example iz chatdjipitija u StaDodati.md
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
            else if (cmd.equals("slowmode")) {
                if(!message.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `MANAGE_CHANNEL` permission").queue();
                    return;
                }
                try {
                    int slowmodeKojiUserZeli = Integer.valueOf(niz_reci[1]);
                    guildChannelEvil.getManager().setSlowmode(slowmodeKojiUserZeli).queue();
                    channel.sendMessage(mentionUser + " set the slowmode to " + niz_reci[1] + " seconds").queue();

                } catch (NumberFormatException e) {
                    // ako opet zaboravim da napisem .queue() mislim da ce se lose stvari desiti
                    channel.sendMessage("Input you provided is not a number!").queue();
                    return;
                }
            }
            else if (cmd.equals("slowmode?")) {

                // iskreno ne volim ovo Integer.toString() u javi ali sta ces
                // i == je broken pa moras da koristis .equals()
                String getSlowChannel = Integer.toString(guildChannelEvil.getSlowmode());

                channel.sendMessage("The current slowmode in this channel is `" + getSlowChannel + "` seconds.").queue();
                // e i da ako citas ovo volim te
                // volim te i ako ne citas ali razumes poentu
                // <3
            }
            else if (cmd.equals("members")) {
                // problem: broji i botove, mogu da resim problem jednom FOR petljom ali bas mi se ne svidja to resenje
                channel.sendMessage("There are " + Integer.toString(event.getGuild().getMemberCount()) + " members in this server").queue();

                /*
                event.getGuild().loadMembers().onSuccess(members -> {
                    int users = 0;
                    int bots = 0;
                    for(Member member: members){
                        if (member.getUser().isBot()) bots++;
                        else users++;
                    }
                }); // kod zla koji ne treba implementirati, ali nek stoji ovde za slucaj da mi zatreba
                */
            }
            else if (cmd.equals("say")) {
                // moj ID, jer drugi nisu dostojni ove komande
                if(message.getAuthor().getId().equals("716962243400564786")) {
                    message.delete().queue();
                    channel.sendMessage(content.substring(5)).queue();

                    // problem: prikazuje samo jednu rec iz cele poruke, jako me mrzi da popravljam, ovo iznad tehnicki radi iako nije najlepse
                    //channel.sendMessage(niz_reci[1]).queue();
                } else System.out.println(message.getAuthor().getName() + " pokusava da koristi ovu komandu");
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
        event.getGuild().getVoiceChannelById(membersvcid).getManager().setName("Members: " + event.getGuild().getMemberCount()).queue();
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        //Guild guild = event.getGuild();
        TextChannel leavechannel = event.getGuild().getTextChannelById(leavechid);
        // bruhh sta je ovo
        //List<TextChannel> leavechlist = event.getGuild().getTextChannelsByName("welcome",true);


        // Isto kao i za welcome
        leavechannel.sendMessage(event.getUser().getAsMention() + " has left the server.").queue();
        event.getGuild().getVoiceChannelById(membersvcid).getManager().setName("Members: " + event.getGuild().getMemberCount()).queue();
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
}
