package net.cowtopia.dscjava.commands;

import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Avatar implements ICommand {
    @Override
    public String getName() {
        return "avatar";
    }

    @Override
    public String getDescription() {
        return "Presents you with someones avatar";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER,"user","user you want avatar shown"));
        return options;
    }

    @Override
    public DefaultMemberPermissions getPermission() {
        return DefaultMemberPermissions.ENABLED;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping avatarOption = event.getOption("user");
        Member avatarMember = event.getMember();
        if (avatarOption != null) {
            avatarMember = avatarOption.getAsMember();
        }
        EmbedBuilder avatarEmbed = new EmbedBuilder();
        avatarEmbed.setTitle(avatarMember.getUser().getName() + "'s avatar");
        avatarEmbed.setImage(avatarMember.getUser().getAvatarUrl());
        avatarEmbed.setFooter("Requested by " + event.getMember().getUser().getName());
        avatarEmbed.setColor(Color.BLUE);

        event.replyEmbeds(avatarEmbed.build()).queue();
        avatarEmbed.clear();
    }
}
