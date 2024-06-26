package net.cowtopia.dscjava.commands;

import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Unlock implements ICommand {
    @Override
    public String getName() {
        return "unlock";
    }

    @Override
    public String getDescription() {
        return "Unlocks channel";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public DefaultMemberPermissions getPermission() {
        return DefaultMemberPermissions.DISABLED;

    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.getChannel().asTextChannel().upsertPermissionOverride(event.getGuild().getPublicRole()).clear(Permission.MESSAGE_SEND).queue();
        event.reply("Channel successfully unlocked!").setEphemeral(true).queue();
    }
}
