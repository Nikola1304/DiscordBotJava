package net.cowtopia.dscjava.komande;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
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
                guild.createTextChannel(chStart + author.getId()).setTopic("This ticket was created by " + author.getName() + " with reason: " + argument).queue(ticketch -> {
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
        else if(name.equals("embed")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            // Set the title of the embed
            embedBuilder.setTitle("Example Embed");

            // Set the description of the embed
            embedBuilder.setDescription("This is a simple example of an embed message.");

            embedBuilder.addField("Fraza 1)", "Stuff", false);
            embedBuilder.addField("Fraza 2)", "Stuff", false);

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
        TextChannel guildChannelEvil = guild.getTextChannelById(channel.getId());


        //GuildChannel guildChannel = guild.getGuildChannelById(channel.getId());
        GuildChannel guildChannel = event.getGuildChannel();

        Role everyoneRole = guild.getPublicRole();


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
        else if(content.equals("kys")){
            channel.sendMessage("No, u").queue();
        }

        // this exists to prevent user to acidentally ban this bot
        if(content.contains(event.getJDA().getSelfUser().getAsMention())) {
            return;
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

            // radi proveru da li je korisnik uneo jos neki podatak osim same komande
            boolean contentPostoji = true;
            String msgContentRaw = "Placeholder";
            if(niz_reci.length > 1) msgContentRaw = content.substring(cmd.length()+2);
            else contentPostoji = false;

            if(cmd.equals("help")) {
                channel.sendMessage("Nema tebi pomoci decko").queue();
            }
            else if (cmd.equals("invite")) {
                message.reply("https://discord.gg/zrEUQENmRr").queue();
            }
            else if (cmd.equals("eksperiment")) {
                // nemam pojma sta ovo radi
                channel.sendMessage(author.retrieveProfile().toString()).queue();
                channel.sendMessage("Kolicina podatka data botu: " + Integer.toString(niz_reci.length)).queue();
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
            else if (cmd.equals("purge")) {
                if(!message.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `MESSAGE_MANAGE` permission").queue();
                    return;
                }
                if(!contentPostoji) {
                    channel.sendMessage("Please specify an amount of messages you want deleted!").queue();
                    return;
                }

                int brojPoruka;
                try {
                    brojPoruka = Integer.valueOf(niz_reci[1]);
                    if(brojPoruka < 0) brojPoruka *= -1; // magic number
                    brojPoruka += 1; // da obrise i moju purge poruku
                    if(brojPoruka > 100) brojPoruka = 100;
                }
                catch (NumberFormatException e) {
                    channel.sendMessage("Input you provided is not a number!").queue();
                    return;
                }

                List<Message> delMsg = channel.getHistory().retrievePast(brojPoruka).complete();
                event.getGuildChannel().deleteMessages(delMsg).queue();
                channel.sendMessage("Successfully purged " + (brojPoruka - 1) + " messages").queue();

            }
            else if(cmd.equals("ban")) {
                if(!message.getMember().hasPermission(Permission.BAN_MEMBERS)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `BAN_MEMBERS` permission").queue();
                    return;
                }

                // prebacio sam ovo u funkciju jer ce mi trebati za kick (jezivo znam)
                String reasonBan = bankickjezivafunkcija(niz_reci);

                if(!mentionedPeople.isEmpty()) {
                    //User banUser = mentionedPeople.get(0).getUser();
                    //banUser.openPrivateChannel().flatMap(chenl -> chenl.sendMessage("DMovan si")).queue();

                    guild.ban(mentionedPeople.get(0),0, TimeUnit.MINUTES).reason(reasonBan).queue();
                            //.queueAfter(500, TimeUnit.MILLISECONDS); // timeunit odredjuje koliko ce da ide nazad i obrise poruke
                    channel.sendMessage(mentionedPeople.get(0).getAsMention() + " has been banned!").queue();
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

                String reasonKick = bankickjezivafunkcija(niz_reci);

                if(!mentionedPeople.isEmpty()) {
                    guild.kick(mentionedPeople.get(0)).reason(reasonKick).queue();
                    channel.sendMessage(mentionedPeople.get(0).getAsMention() + " has been kicked!").queue();
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

                TimeUnit vremenskaJedinica = TimeUnit.MINUTES;
                int duzinaMuta = 5;
                int jedinicaMuta = 2;
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

                if(duzinaMuta < 0) duzinaMuta *= -1;

                if (jedinicaMuta == 1) {
                    vremenskaJedinica = TimeUnit.SECONDS;
                    if (duzinaMuta > 10800) duzinaMuta = 10800;
                }
                else if (jedinicaMuta == 3)
                {
                    vremenskaJedinica = TimeUnit.HOURS;
                    if (duzinaMuta > 169) duzinaMuta = 169;
                }
                else if (jedinicaMuta == 4)
                {
                    vremenskaJedinica = TimeUnit.DAYS;
                    if (duzinaMuta > 28) duzinaMuta = 28;
                }
                else
                {
                    vremenskaJedinica = TimeUnit.MINUTES;
                    if (duzinaMuta > 1400) duzinaMuta = 1400;
                }


                if(!mentionedPeople.isEmpty()) {
                    try {
                        guild.timeoutFor(mentionedPeople.get(0), duzinaMuta, vremenskaJedinica).queue();
                        channel.sendMessage(mentionedPeople.get(0).getUser().getName() + " has been muted!").queue();
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

                if(!mentionedPeople.isEmpty()) {
                    // ne proverava da li korisnik ima timeout, ali nije vazno
                    // ako ne moze da uradi to (fizicki, nema permission, member je jaci od njega), samo ce da baci error, fixaj to kasnije
                    guild.removeTimeout(mentionedPeople.get(0)).queue();
                    channel.sendMessage(mentionedPeople.get(0).getUser().getName() + " has been unmuted!").queue();
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

                if(niz_reci.length == 1) {
                    channel.sendMessage("Specify who you want unbanned").queue();
                    return;
                }

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
                    guild.unban(target);
                    channel.sendMessage("User with the id " + userID + " has been unbanned!").queue();
                } catch (IllegalArgumentException e) {
                    // ako opet zaboravim da napisem .queue() mislim da ce se lose stvari desiti
                    channel.sendMessage("Input you provided is not an ID!").queue();
                    return;
                }
                //channel.sendMessage((CharSequence) event.getGuild().retrieveBanList()).queue();
            }
            else if (cmd.equals("ticket")) {

                String chStart = "ticket-";
                EnumSet<Permission> permsTicket = EnumSet.of(Permission.VIEW_CHANNEL,Permission.MESSAGE_SEND);

                if(channel.getName().startsWith(chStart)) {
                    if(contentPostoji) {
                    //if(niz_reci.length >= 2) {
                        if (niz_reci[1].equals("delete")) {
                            channel.delete().queue();
                        }
                        else if (niz_reci[1].equals("lock")) {

                            String imeTicketOwnera = channel.getName().substring(7);
                            Long idTicketOwnera = Long.parseLong(imeTicketOwnera);
                            //User userTicketOwner = event.getJDA().getUserById(idTicketOwnera);
                            Member memberTicketOwner = guild.getMemberById(idTicketOwnera);

                            // task za buduceg mene: skontaj kako da clearas sve da bude default bez da to unosis manualno
                            // (da se user fizicki izbrise iz channel cuda, a ne samo da mu povuce perms)
                            guildChannelEvil.upsertPermissionOverride(memberTicketOwner).clear(permsTicket).queue();



                            // Role role = guild.getRoleById(817427547678703668L);
                            //PermissionOverride override = guildChannelEvil.getPermissionOverride(role); // Get an existing override for a role
                            //if (override.equals(null)) {
                            //    override = guildChannelEvil.createPermissionOverride(role);//.complete(); // Create a new override if it doesn't exist
                            //}
                            //override.getManager().grant(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND).queue();

                            // List<PermissionOverride> overrides = guildChannelEvil.upsertPermissionOverride()

                            // Remove all existing permission overrides
                            //for (PermissionOverride override : overrides) {
                            //    override.delete().queue();
                            //}

                            //guildChannelEvil.upsertPermissionOverride(role).deny(Permission.VIEW_CHANNEL).queue();

                            // Member member = guild.getMemberById("member_id_here");
                            // guild.addRoleToMember(member, role).queue();
                            // guildChannelEvil.getMemberPermissionOverrides(Permission.MESSAGE_SEND)
                        }
                        else {
                            channel.sendMessage("Provide me with something valid").queue();
                        }
                    }
                    else {
                        channel.sendMessage("Please provide me with some arguments").queue();
                    }
                }
                else {
                    guild.createTextChannel(chStart + author.getId()).setTopic("This ticket was created by " + author.getName() + " with reason: " + msgContentRaw).queue(ticketch -> {
                        ticketch.upsertPermissionOverride(authorMember).grant(permsTicket).queue();
                        ticketch.upsertPermissionOverride(everyoneRole).deny(permsTicket).queue();
                    });
                }
            }
            else if (cmd.equals("slowmode")) {
                if(!message.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `MANAGE_CHANNEL` permission").queue();
                    return;
                }
                if(!contentPostoji) {
                    message.reply("Please provide me with slowmode you want set.").queue();
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
            else if (cmd.equals("suggest")) {
                if(!contentPostoji) {
                    message.reply("Please provide a valid suggestion").queue();
                }
                else
                {
                    // https://charbase.com/1f44d-unicode-thumbs-up-sign
                    message.addReaction(Emoji.fromUnicode("\ud83d\udc4d")).queue();
                    EmbedBuilder sugEmbedBuild = new EmbedBuilder();
                    sugEmbedBuild.setTitle(author.getName() + " suggests");
                    sugEmbedBuild.setDescription(msgContentRaw);
                    sugEmbedBuild.setColor(5724148);
                    sugEmbedBuild.setFooter(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(LocalDateTime.now()),authorMember.getUser().getAvatarUrl());
                    MessageEmbed sugEmbed = sugEmbedBuild.build();

                    guild.getTextChannelById(suggestchid).sendMessageEmbeds(sugEmbed).queue(suggestmsg -> {
                        suggestmsg.addReaction(Emoji.fromUnicode("\u2705")).queue();
                        suggestmsg.addReaction(Emoji.fromUnicode("\u274c")).queue();
                    });
                    sugEmbedBuild.clear();
                }
            }
            else if (cmd.equals("members")) {
                // problem: broji i botove, mogu da resim problem jednom FOR petljom ali bas mi se ne svidja to resenje
                channel.sendMessage("There are " + guild.getMemberCount() + " members in this server").queue();
            }
            else if (cmd.equals("lock")) {
                if(!message.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `MANAGE_CHANNEL` permission").queue();
                    return;
                }
                guildChannelEvil.upsertPermissionOverride(everyoneRole).deny(Permission.MESSAGE_SEND).queue();
            }
            else if (cmd.equals("unlock")) {
                if(!message.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `MANAGE_CHANNEL` permission").queue();
                    return;
                }
                guildChannelEvil.upsertPermissionOverride(everyoneRole).clear(Permission.MESSAGE_SEND).queue();
            }
            else if (cmd.equals("avatar")) {
                Member avatarMember;
                if(mentionedPeople.isEmpty()) {
                    avatarMember = authorMember;
                }
                else {
                    avatarMember = mentionedPeople.get(0);
                }
                EmbedBuilder avatarEmbed = new EmbedBuilder();
                avatarEmbed.setTitle(avatarMember.getUser().getName() + "'s avatar");
                // podesi velicinu avatara
                avatarEmbed.setImage(avatarMember.getUser().getAvatarUrl());
                // https://www.tabnine.com/code/java/methods/net.dv8tion.jda.core.entities.User/getEffectiveAvatarUrl
                avatarEmbed.setFooter("Requested by " + author.getName());
                avatarEmbed.setColor(Color.BLUE);
                channel.sendMessageEmbeds(avatarEmbed.build()).queue();
                avatarEmbed.clear();
            }
            else if (cmd.equals("say")) {
                // moj ID, jer drugi nisu dostojni ove komande
                if(author.getId().equals("716962243400564786")) {
                    if(niz_reci.length > 1) {
                        message.delete().queue();
                        channel.sendMessage(msgContentRaw).queue();
                    }
                    else {
                        channel.sendMessage("Tell me what to say").queue();
                    }
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

                embedBuilder.setFooter("Bot created by person", guild.getOwner().getUser().getAvatarUrl());

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
        // System.out.println("Poruka je poslata (java prejaka)");
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
