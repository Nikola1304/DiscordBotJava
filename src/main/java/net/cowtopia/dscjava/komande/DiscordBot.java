package net.cowtopia.dscjava.komande;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.requests.restaction.CacheRestAction;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.net.CacheRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DiscordBot extends ListenerAdapter
{
    public static final String prefix = "!";
    static final long welcomechid = 910130241664602144L;
    static final long leavechid = welcomechid;
    static final long generalchid = 910094414175694879L;
    static final long membersvcid = 1211789463475327067L;
    static final long suggestchid = 1213992313332699166L;
    static final long ticketsCat = 1219763274141405214L;


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        Guild guild = event.getGuild();
        String name = event.getName();
        MessageChannel channel = event.getChannel();

        User author = event.getMember().getUser();
        String mentionUser = author.getAsMention();

        Member authorMember = event.getMember();
        TextChannel textChannel = guild.getTextChannelById(channel.getId());

        Role everyoneRole = guild.getPublicRole();

        String hierarchyExMsg = "Sorry, I do not have sufficient permissions to do that";

        if(name.equals("help")) {
            //event.reply("You just farted").queue();

            event.deferReply().queue();
            //event.getHook().sendMessage("You just farted").queue();

            // samo user vidi ovo
            // bar se nadam
            event.getHook().sendMessage("Nema tebi pomoci decko").setEphemeral(true).queue();
        }
        else if(name.equals("food")) {

            OptionMapping option = event.getOption("foodname");

            if(option.equals(null)) {
                event.reply("for some reason I cant explain, a food name was not provided").queue();
                return;
            }

            String favouriteFood = option.getAsString();
            event.reply("Your favourite food is " + favouriteFood).queue();
        }
        else if(name.equals("sum")) {
            OptionMapping operand1 = event.getOption("operand1");
            OptionMapping operand2 = event.getOption("operand2");

            if(operand1 == null || operand2 == null) {
                event.reply("No numbers were provided!").queue();
                return;
            }

            int sum = operand1.getAsInt() + operand2.getAsInt();
            event.reply("The sum is: " + sum).queue();
        }
        else if(name.equals("invite")) {
            event.reply("https://discord.gg/zrEUQENmRr").queue();
        }
        else if(name.equals("ping")) {
            long startTime = System.currentTimeMillis();
            event.reply("Pinging...").queue(response -> {
                long endTime = System.currentTimeMillis();
                long ping = endTime - startTime;
                response.editOriginal("Ping: " + ping + "ms").queue();
            });
        }
        else if(name.equals("members")) {
            event.reply("There are" + guild.getMemberCount() + "members in this server").queue();
        }
        else if(name.equals("slowmode-amount")) {
            event.reply("The current slowmode in this channel is `" + textChannel.getSlowmode() + "` seconds.").queue();
        }
        else if(name.equals("avatar")) {
            OptionMapping avatarOption = event.getOption("user");
            Member avatarMember = authorMember;
            if(avatarOption != null) {
                avatarMember = avatarOption.getAsMember();
            }

            EmbedBuilder avatarEmbed = new EmbedBuilder();
            avatarEmbed.setTitle(avatarMember.getUser().getName() + "'s avatar");
            avatarEmbed.setImage(avatarMember.getUser().getAvatarUrl());
            avatarEmbed.setFooter("Requested by " + author.getName());
            avatarEmbed.setColor(Color.BLUE);

            event.replyEmbeds(avatarEmbed.build()).queue();
            avatarEmbed.clear();
        }
        else if(name.equals("purge")) {
            int brojPoruka = (event.getOption("amount")).getAsInt();
            List<Message> delMsg = channel.getHistory().retrievePast(brojPoruka).complete();
            event.getGuildChannel().deleteMessages(delMsg).queue();
            event.reply("Successfully purged " + brojPoruka + " messages").queue();
        }
        else if(name.equals("suggest")) {
            String content = (event.getOption("content")).getAsString();

            // https://charbase.com/1f44d-unicode-thumbs-up-sign
            EmbedBuilder sugEmbedBuild = new EmbedBuilder();
            sugEmbedBuild.setTitle(author.getName() + " suggests");
            sugEmbedBuild.setDescription(content);
            sugEmbedBuild.setColor(5724148);
            sugEmbedBuild.setFooter(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(LocalDateTime.now()),authorMember.getUser().getAvatarUrl());
            MessageEmbed sugEmbed = sugEmbedBuild.build();

            guild.getTextChannelById(suggestchid).sendMessageEmbeds(sugEmbed).queue(suggestmsg -> {
                suggestmsg.addReaction(Emoji.fromUnicode("\u2705")).queue();
                suggestmsg.addReaction(Emoji.fromUnicode("\u274c")).queue();
            });
            sugEmbedBuild.clear();

            event.reply("You just created a new suggestion").setEphemeral(true).queue();
        }
        else if(name.equals("mute")) {
            if(!authorMember.hasPermission(Permission.BAN_MEMBERS)) {
                event.reply("You don't have right permissions to execute this command. You need `BAN_MEMBERS` permission").queue();
                return;
            }
            OptionMapping muteUsr = event.getOption("user");
            OptionMapping amountMute = event.getOption("amount");
            OptionMapping typeMute = event.getOption("type");
            //OptionMapping reasonMute = event.getOption("reason");

            Member muteUser = muteUsr.getAsMember();
            int duzinaMuta = amountMute.getAsInt();
            int jedinicaMuta = typeMute.getAsInt();
            /*
            String reason = "No reason provided";
            if(reasonMute != null) {
                reason = reasonMute.getAsString();
            }*/

            TimeUnit vremenskaJedinica = TimeUnit.MINUTES;

            if (jedinicaMuta == 1) {
                vremenskaJedinica = TimeUnit.SECONDS;
                if (duzinaMuta > 10800) duzinaMuta = 10800;
            }
            else if (jedinicaMuta == 3) {
                vremenskaJedinica = TimeUnit.HOURS;
                if (duzinaMuta > 169) duzinaMuta = 169;
            } else if (jedinicaMuta == 4) {
                vremenskaJedinica = TimeUnit.DAYS;
                if (duzinaMuta > 28) duzinaMuta = 28;
            } else {
                vremenskaJedinica = TimeUnit.MINUTES;
                if (duzinaMuta > 1400) duzinaMuta = 1400;
            }
            try {
                guild.timeoutFor(muteUser, duzinaMuta, vremenskaJedinica).queue();
                event.reply(muteUser.getUser().getName() + " has been muted").queue();
            }
            catch (HierarchyException e) {
                event.reply(hierarchyExMsg).queue();
            }
        }
        else if(name.equals("kick")) {
            OptionMapping kickUsr = event.getOption("user");
            OptionMapping reasonKick = event.getOption("reason");

            Member kickUser = kickUsr.getAsMember();
            String reason = "No reason provided";

            if(reasonKick != null)
                reason = reasonKick.getAsString();

            try {
                guild.kick(kickUser).reason(reason).queue();
                event.reply(kickUser.getUser().getName() + " has been kicked").queue();
            }
            catch (HierarchyException e) {
                event.reply(hierarchyExMsg).queue();
            }
        }
        else if(name.equals("ban")) {
            if(!authorMember.hasPermission(Permission.BAN_MEMBERS)) {
                event.reply("You don't have right permissions to execute this command. You need `BAN_MEMBERS` permission").queue();
                return;
            }
            OptionMapping banUsr = event.getOption("user");
            OptionMapping reasonBan = event.getOption("reason");

            Member banUser = banUsr.getAsMember();
            String reason = "No reason provided";

            if(reasonBan != null) {
                reason = reasonBan.getAsString();
            }

            try {
                guild.ban(banUser,0,TimeUnit.MINUTES).reason(reason).queue();
                event.reply(banUser.getUser().getName() + " has been banned").queue();
            }
            catch (HierarchyException e) {
                event.reply(hierarchyExMsg).queue();
            }
        }
        else if(name.equals("ticket")) {
            String chStart = "ticket-";
            EnumSet<Permission> permsTicket = EnumSet.of(Permission.VIEW_CHANNEL,Permission.MESSAGE_SEND);
            OptionMapping argumnt = event.getOption("argument");
            String argument = argumnt.getAsString();

            Category ticketsCategory = event.getGuild().getCategoryById(ticketsCat);

            if(channel.getName().startsWith(chStart)) {
                if(authorMember.hasPermission(Permission.MANAGE_CHANNEL)) {
                    if(argument.equals("delete")) {
                        channel.delete().queue();
                        event.deferReply().queue();
                    }
                    else if(argument.equals("lock")) {
                        // dis stil daznt vrk kil mi
                        String imeTicketOwnera = channel.getName().substring(7);
                        Long idTicketOwnera = Long.parseLong(imeTicketOwnera);
                        User memberTicketOw = event.getJDA().getUserById(idTicketOwnera);
                        Member memberTicketOwner = event.getGuild().getMember(memberTicketOw); //.useCache(false)
                        textChannel.upsertPermissionOverride(memberTicketOwner).clear(permsTicket).queue();
                        event.reply("Ticket successfully locked").queue();
                    }
                    else {
                        event.reply("Wrong input provided").queue();
                    }
                }
                else event.reply("Insufficient permissions").queue();
            }
            else {
                ticketsCategory.createTextChannel(chStart + author.getId()).setTopic("This ticket was created by " + author.getName() + " with reason: " + argument).queue(ticketch -> {
                    ticketch.upsertPermissionOverride(authorMember).grant(permsTicket).queue();
                    ticketch.upsertPermissionOverride(everyoneRole).deny(permsTicket).queue();
                    event.reply("<#" + ticketch.getId() +"> successfully created!").queue();
                });
            }
        }
        else if(name.equals("slowmode")) {
            int amount = (event.getOption("amount")).getAsInt();
            textChannel.getManager().setSlowmode(amount).queue();
            event.reply(mentionUser + " set the slowmode to " + amount + " seconds").queue();
        }
        else if(name.equals("unmute")) {
            OptionMapping unmuteUsr = event.getOption("user");
            Member unmuteUser = unmuteUsr.getAsMember();

            try {
                guild.removeTimeout(unmuteUser).queue();
                event.reply(unmuteUser.getUser().getName() + " has been unmuted").queue();
            }
            catch (HierarchyException e) {
                event.reply(hierarchyExMsg).queue();
            }
        }
        else if(name.equals("lock")) {
            textChannel.upsertPermissionOverride(everyoneRole).deny(Permission.MESSAGE_SEND).queue();
            event.reply("Channel successfully locked!").setEphemeral(true).queue();
        }
        else if(name.equals("unlock")) {
            textChannel.upsertPermissionOverride(everyoneRole).clear(Permission.MESSAGE_SEND).queue();
            event.reply("Channel successfully unlocked!").setEphemeral(true).queue();
        }
        else if(name.equals("serverinfo")){

            EmbedBuilder sinfoEmb = new EmbedBuilder()
                    .setTitle(guild.getName())
                    .setThumbnail(guild.getIconUrl())
                    .setColor(Color.BLUE)

                    .addField("Owner", guild.getOwner().getUser().getName(), true)
                    .addField("Members", Integer.toString(guild.getMemberCount()), true)
                    .addField("Roles", Integer.toString(guild.getRoles().size()), true)
                    .addField("Category Channels", Integer.toString(guild.getCategories().size()), true)
                    .addField("Text Channels", Integer.toString(guild.getTextChannels().size()), true)
                    .addField("Voice Channels", Integer.toString(guild.getVoiceChannels().size()), true)

                    .setFooter("ID: " + guild.getId() + " | Server created: " + DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(guild.getTimeCreated()));

            event.replyEmbeds(sinfoEmb.build()).queue();
        }
        else if(name.equals("embed")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            // Set the title of the embed
            embedBuilder.setTitle("Example Embed");
            embedBuilder.setAuthor("setAuthor +-/*");

            // Set the description of the embed
            embedBuilder.setDescription("This is a simple example of an embed message.");

            embedBuilder.addField("Fraza 1)", "Stuff", true); // false == imace svoj odvojen red
            embedBuilder.addField("Fraza 2)", "Stuff", true); // jedan red se popuni kad ih ima 3 pa predje u naredni
            embedBuilder.addField("Fraza 3)", "Stuff", true);
            embedBuilder.addField("Fraza 4)", "Stuff", true);
            embedBuilder.addField("Fraza 5)", "Stuff", true);
            embedBuilder.addField("Fraza 6)", "Stuff", true);

            // Set the color of the embed
            embedBuilder.setColor(Color.GREEN);

            embedBuilder.setFooter("Bot created by person", guild.getOwner().getUser().getAvatarUrl());

            // Build the embed message
            MessageEmbed embed = embedBuilder.build();

            // Send the embed message to the channel
            //channel.sendMessageEmbeds(embed).queue();
            event.replyEmbeds(embed).queue();
            //channel.sendMessage("message").setEmbeds(embedBuilder.build()).queue();
            embedBuilder.clear();
        }
        else if(name.equals("license")) {
            event.reply("https://github.com/Nikola1304/DiscordBotJava/blob/main/LICENSE").queue();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();

        User author = event.getAuthor();
        String mentionUser = author.getAsMention();
        Guild guild = event.getGuild();

        Member authorMember = event.getMember();

        // lista svih membera koji su mentionovani u poruci sa koje mozemo da skinemo recimo prvog kada radimo ban, kick, mute itd
        List<Member> mentionedPeople = message.getMentions().getMembers();

        // ja ne kontam razliku izmedju ovih channela pa sam samo hakovao ovo jer ne znam sta bih drugo
        TextChannel textChannel = guild.getTextChannelById(channel.getId());


        //GuildChannel guildChannel = guild.getGuildChannelById(channel.getId());
        GuildChannel guildChannel = event.getGuildChannel();

        Role everyoneRole = guild.getPublicRole();


        if(author.isBot()) return;
        // this exists to prevent user to acidentally ban this bot
        if(content.contains(event.getJDA().getSelfUser().getAsMention())) {
            return;
        }

        /*
        channel.sendMessage("author: " + author +
                "\nmember: " + authorMember + "" +
                "\nmember1: " + guild.getMemberById(author.getId())).queue();
         */


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
        else if(content.equals("kys")){
            channel.sendMessage("No, u").queue();
        }

        if (content.startsWith("!say")) {
            // moj ID, jer drugi nisu dostojni ove komande
            if(author.getId().equals("716962243400564786")) {
                try {
                    channel.sendMessage(content.substring(5)).queue();
                    message.delete().queue();
                }
                catch (StringIndexOutOfBoundsException e) {
                    channel.sendMessage("Tell me what to say").queue();
                }
            } else System.out.println(author.getName() + " pokusava da koristi ovu komandu");
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event)
    {
        Guild guild = event.getGuild();
        TextChannel welcomechannel = guild.getTextChannelById(welcomechid);

        // naravno sve ovo treba poboljsati ali baza je tu
        // dodati lepu porukicu, updatovanje channela sa member listama, potencijalno davanje nekih rolova

        //guild.getDefaultChannel().sendMessage("Welcome " + event.getUser().getAsMention() + " to the server!").queue();
        welcomechannel.sendMessage("Welcome " + event.getUser().getAsMention() + " to the server!").queue();


        // ovo nije najefikasnije na velikim serverima ali ako ikada postanem toliko veliki resavanje ovog problema bice jedan od manjih problema
        // takodje discord API ogranicava promene na channelima na dve promene u 10 minuta

        //guild.getVoiceChannelById(1211789463475327067L).getManager().setName("Total Users: " + guild.getMembers().size()).queue();
        // ubijam API ovime, ostavicu ga disabled za sada (27.02.2024.)
        //guild.getVoiceChannelById(membersvcid).getManager().setName("Members: " + guild.getMemberCount()).queue();
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        Guild guild = event.getGuild();
        TextChannel leavechannel = guild.getTextChannelById(leavechid);
        // bruhh sta je ovo
        //List<TextChannel> leavechlist = guild.getTextChannelsByName("welcome",true);


        // Isto kao i za welcome
        leavechannel.sendMessage(event.getUser().getAsMention() + " has left the server.").queue();
        //guild.getVoiceChannelById(membersvcid).getManager().setName("Members: " + guild.getMemberCount()).queue();
    }

    private String bankickjezivafunkcija(String[] niz_reci) {

        // bukvalno me ne osudjujte zbog ovoga
        String reasonBan = "bug";
        if(niz_reci.length == 2) reasonBan = "No reason provided. " + DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
        else if (niz_reci.length > 2) {
            reasonBan = niz_reci[2];
            if (niz_reci.length > 3) {
                for (int i = 3; i < niz_reci.length; i++) {
                    reasonBan = reasonBan + " " + niz_reci[i];
                }
            }
        }
        return reasonBan;
    }

    private void permMsg(Permission perm, String permtxt, Message message, MessageChannel channel) {
        if(!message.getMember().hasPermission(perm)) {
            channel.sendMessage("You don't have right permissions to execute this command. You need `" + permtxt + "` permission").queue();
            return;
        }
    }

    // potencijalno maknuti deo koda ispod ovog komentara
    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent event) {
        String channelName = event.getChannel().getName();

        TextChannel channel = event.getGuild().getTextChannelById(1218562604830560266L);

        if(channel != null) {
            channel.sendMessage("The channel: \"" + channelName + "\" was deleted").queue();
        }
    }

    /*
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Guild guild = event.getJDA().getGuildById(817404696687673454L);
        TextChannel channel = guild.getTextChannelById(generalchid);
        channel.sendMessage("Podigao sam se iz mrtvih").queue();
    }*/
}
