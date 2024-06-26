package net.cowtopia.dscjava.commands;

import net.cowtopia.dscjava.Main;
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

public class Kick implements ICommand {
    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getDescription() {
        return "Kicks user";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER,"user","user you want kicking",true));
        options.add(new OptionData(OptionType.STRING,"reason","Reason for kicking user",false));
        return options;
    }

    @Override
    public DefaultMemberPermissions getPermission() {
        return DefaultMemberPermissions.DISABLED;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping kickUsr = event.getOption("user");
        OptionMapping reasonKick = event.getOption("reason");

        Member kickUser = kickUsr.getAsMember();
        String reason = "No reason provided";

        if(reasonKick != null)
            reason = reasonKick.getAsString();

        try {
            event.getGuild().kick(kickUser).reason(reason).queue();
            event.reply(kickUser.getUser().getName() + " has been kicked").queue();
        }
        catch (HierarchyException e) {
            event.reply(Main.hierarchyExMsg).queue();
        }
    }
}
