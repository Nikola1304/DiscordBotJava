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
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String prefix = "!";

        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();


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
                message.reply("[Discord Link](https://discord.gg/zrEUQENmRr)").queue();
            }
            else if (cmd.equals("ping")) {
                //channel.sendMessage(message.getAuthor().getAsMention() + " my ping is `implement actual ping cmd`").queue();

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
                //List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
                //if(!mentionedMembers.isEmpty)
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
                //event.getChannel().sendMessage("message").setEmbeds(embed.build()).queue();
                embedBuilder.clear();

            }
            else if (cmd.equals("slowmode")) {
                channel.sendMessage("implement later plz").queue();
            }
            else if (cmd.equals("members")) {
                // problem: broji i botove, mogu da resim problem jednom FOR petljom ali bas mi se ne svidja to resenje
                channel.sendMessage("There are " + Integer.toString(message.getGuild().getMemberCount()) + " members in this server").queue();

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
        }

        // samo provera kada je bilo koja poruka poslata na server, ukloniti u finalnoj verziji da bi se smanjilo trosenje resursa
        //event.getChannel().sendMessage("This was sent: " + content).queue();
        System.out.println("Poruka je poslata (java prejaka)");
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        //Guild guild = event.getGuild();
        TextChannel welcomechannel = event.getGuild().getTextChannelById(910130241664602144L);

        // naravno sve ovo treba poboljsati ali baza je tu
        // dodati lepu porukicu, updatovanje channela sa member listama, potencijalno davanje nekih rolova

        //guild.getDefaultChannel().sendMessage("Welcome " + event.getUser().getAsMention() + " to the server!").queue();
        welcomechannel.sendMessage("Welcome " + event.getUser().getAsMention() + " to the server!").queue();
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        //Guild guild = event.getGuild();
        TextChannel leavechannel = event.getGuild().getTextChannelById(910130241664602144L);

        // Isto kao i za welcome

        leavechannel.sendMessage(event.getUser().getAsMention() + " has left the server.").queue();
    }

    // potencijalno maknuti deo koda ispod ovog komentara
    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent event) {
        String channelName = event.getChannel().getName();

        TextChannel general = event.getGuild().getTextChannelById(1211361791406506136L);

        if(general != null) {
            general.sendMessage("The channel: \"" + channelName + "\" voz dilited").queue();
        }
    }
}
