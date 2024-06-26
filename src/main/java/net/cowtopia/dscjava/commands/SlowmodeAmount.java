package net.cowtopia.dscjava.commands;

import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class SlowmodeAmount implements ICommand {
    @Override
    public String getName() {
        return "slowmode-amount";
    }

    @Override
    public String getDescription() {
        return "Shows you slowmode duration in your current channel";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public DefaultMemberPermissions getPermission() {
        return DefaultMemberPermissions.ENABLED;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("The current slowmode in this channel is `" + event.getChannel().asTextChannel().getSlowmode() + "` seconds.").queue();
    }
}
