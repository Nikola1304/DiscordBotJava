package net.cowtopia.dscjava.commands.moderation;

import net.cowtopia.dscjava.Main;
import net.cowtopia.dscjava.libs.CmdType;
import net.cowtopia.dscjava.libs.GSonConfig;
import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Ban implements ICommand {
    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String getDescription() {
        return "Bans user";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER,"user","user you want banned",true));
        options.add(new OptionData(OptionType.STRING,"reason","Reason for banning user",false));
        return options;
    }

    @Override
    public Boolean isAdminCommand() {
        return true;
    }

    @Override
    public CmdType getCmdType() {
        return CmdType.Moderation;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping banUsr = event.getOption("user");
        OptionMapping reasonBan = event.getOption("reason");

        Member banUser = banUsr.getAsMember();
        String reason = "No reason provided";

        if(reasonBan != null) {
            reason = reasonBan.getAsString();
        }

        try {
            event.getGuild().ban(banUser,0, TimeUnit.MINUTES).reason(reason).queue();
            event.reply(banUser.getUser().getName() + " has been banned").queue();
        }
        catch (HierarchyException e) {
            event.reply(GSonConfig.get().getHierarchyExMsg()).queue();
        }
    }
}
