package net.cowtopia.dscjava.listeners;

import net.cowtopia.dscjava.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SlashListeners extends ListenerAdapter
{
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
        else if(name.equals("sup")) {
            TextInput nameModal = TextInput.create("sup-name","Name", TextInputStyle.SHORT)
                    .setMinLength(1)
                    .setRequired(true)
                    .setPlaceholder("The recipient")
                    .build();

            TextInput messageModal = TextInput.create("sup-message","Message",TextInputStyle.PARAGRAPH)
                    .setMinLength(10)
                    .setMaxLength(100)
                    .setRequired(true)
                    .setValue("You smell like a wet sock")
                    .build();

            Modal modal = Modal.create("sup-modal","Say Wassup")
                    .addActionRows(ActionRow.of(nameModal), ActionRow.of(messageModal))
                    .build();

            event.replyModal(modal).queue();
        }
        else if (name.equals("multiply")) {
            TextInput operand1 = TextInput.create("operand1","Operand 1",TextInputStyle.SHORT)
                    .setPlaceholder("Enter a number")
                    .setMinLength(1)
                    .setMaxLength(11)
                    .setRequired(true)
                    .build();

            TextInput operand2 = TextInput.create("operand2","Operand 2",TextInputStyle.SHORT)
                    .setPlaceholder("Enter a number")
                    .setMinLength(1)
                    .setMaxLength(11)
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("multiply-modal","Multiply")
                    .addActionRows(ActionRow.of(operand1),ActionRow.of(operand2))
                    .build();

            event.replyModal(modal).queue();
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

            guild.getTextChannelById(Main.suggestchid).sendMessageEmbeds(sugEmbed).queue(suggestmsg -> {
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

            Category ticketsCategory = event.getGuild().getCategoryById(Main.ticketsCat);

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

            event.replyEmbeds(sinfoEmb.build()).addActionRow(
                    Button.primary("view-roles-button", "View Roles"),
                    Button.primary("view-emojis-button", "View Emojis")
            ).queue();
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
}
