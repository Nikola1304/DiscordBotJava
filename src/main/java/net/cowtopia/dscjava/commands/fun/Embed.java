package net.cowtopia.dscjava.commands.fun;

import net.cowtopia.dscjava.libs.CmdType;
import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.List;

public class Embed implements ICommand {
    @Override
    public String getName() {
        return "embed";
    }

    @Override
    public String getDescription() {
        return "Creates example embed";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public Boolean isAdminCommand() {
        return false;
    }

    @Override
    public CmdType getCmdType() {
        return CmdType.Fun;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
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

        embedBuilder.setFooter("Bot created by person", event.getGuild().getOwner().getUser().getAvatarUrl());

        // Build the embed message
        MessageEmbed embed = embedBuilder.build();

        // Send the embed message to the channel
        // channel.sendMessageEmbeds(embed).queue();
        event.replyEmbeds(embed).queue();
        //channel.sendMessage("message").setEmbeds(embedBuilder.build()).queue();
        embedBuilder.clear();
    }
}
