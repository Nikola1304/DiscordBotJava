package net.cowtopia.dscjava.commands;

import net.cowtopia.dscjava.Main;
import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Rules implements ICommand {
    @Override
    public String getName() {
        return "rules";
    }

    @Override
    public String getDescription() {
        return "Shows you server rules";
    }

    @Override
    public List<OptionData> getOptions() {
        //return List.of();
        return null;
    }

    @Override
    public DefaultMemberPermissions getPermission() {
        return DefaultMemberPermissions.ENABLED;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.replyEmbeds(Main.rulesEmbedBuilder).queue();
    }
}
