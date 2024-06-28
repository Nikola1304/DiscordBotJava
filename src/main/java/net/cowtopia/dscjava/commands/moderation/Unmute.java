package net.cowtopia.dscjava.commands.moderation;

import net.cowtopia.dscjava.Main;
import net.cowtopia.dscjava.libs.GSonConfig;
import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class Unmute implements ICommand {
    @Override
    public String getName() {
        return "unmute";
    }

    @Override
    public String getDescription() {
        return "Unmutes user";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER,"user","user you want unmuted",true));
        return options;
    }

    @Override
    public Boolean isAdminCommand() {
        return true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member unmuteUser = event.getOption("user").getAsMember();

        try {
            event.getGuild().removeTimeout(unmuteUser).queue();
            //event.deferReply().queue();
            event.reply(unmuteUser.getUser().getName() + " has been unmuted").queue();
            //event.getHook().sendMessage(unmuteUser.getUser().getName() + " has been unmuted").setEphemeral(true).queue();
        }
        catch (HierarchyException e) {
            event.reply(GSonConfig.get().getHierarchyExMsg()).queue();
        }
    }
}
