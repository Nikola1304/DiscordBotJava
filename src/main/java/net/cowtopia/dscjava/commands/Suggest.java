package net.cowtopia.dscjava.commands;

import net.cowtopia.dscjava.Main;
import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Suggest implements ICommand {
    @Override
    public String getName() {
        return "suggest";
    }

    @Override
    public String getDescription() {
        return "Posts a suggestion so people can vote on it";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING,"content","Thing youre suggesting",true));
        return options;
    }

    @Override
    public DefaultMemberPermissions getPermission() {
        return DefaultMemberPermissions.ENABLED;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String content = (event.getOption("content")).getAsString();

        Member authorMember = event.getMember();
        User author = authorMember.getUser();

        // https://charbase.com/1f44d-unicode-thumbs-up-sign
        EmbedBuilder sugEmbedBuild = new EmbedBuilder();
        sugEmbedBuild.setTitle(author.getName() + " suggests");
        sugEmbedBuild.setDescription(content);
        sugEmbedBuild.setColor(5724148);
        sugEmbedBuild.setFooter(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(LocalDateTime.now()),authorMember.getUser().getAvatarUrl());
        MessageEmbed sugEmbed = sugEmbedBuild.build();

        event.getGuild().getTextChannelById(Main.greader.getSuggestId()).sendMessageEmbeds(sugEmbed).queue(suggestmsg -> {
            suggestmsg.addReaction(Emoji.fromUnicode("\u2705")).queue();
            suggestmsg.addReaction(Emoji.fromUnicode("\u274c")).queue();
        });
        sugEmbedBuild.clear();

        event.reply("You just created a new suggestion").setEphemeral(true).queue();
    }
}
