package net.cowtopia.dscjava.commands;


import net.cowtopia.dscjava.Main;
import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Ticket implements ICommand {
    @Override
    public String getName() {
        return "ticket";
    }

    @Override
    public String getDescription() {
        return "Creates ticket";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING,"argument","Delete/Lock command",true));
        return options;
    }

    @Override
    public DefaultMemberPermissions getPermission() {
        return DefaultMemberPermissions.ENABLED;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String chStart = "ticket-";
        EnumSet<Permission> permsTicket = EnumSet.of(Permission.VIEW_CHANNEL,Permission.MESSAGE_SEND);
        OptionMapping argumnt = event.getOption("argument");
        String argument = argumnt.getAsString();

        Category ticketsCategory = event.getGuild().getCategoryById(Main.greader.getTicketsCat());

        Channel channel = event.getChannel();
        Member authorMember = event.getMember();
        User author = authorMember.getUser();
        TextChannel textChannel = event.getChannel().asTextChannel();

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
                ticketch.upsertPermissionOverride(event.getGuild().getPublicRole()).deny(permsTicket).queue();
                event.reply("<#" + ticketch.getId() +"> successfully created!").queue();
            });
        }
    }
}
