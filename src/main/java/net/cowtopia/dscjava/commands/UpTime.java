package net.cowtopia.dscjava.commands;

import net.cowtopia.dscjava.libs.ICommand;
import net.cowtopia.dscjava.libs.TimeToString;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.lang.management.ManagementFactory;
import java.util.List;

public class UpTime implements ICommand {
    @Override
    public String getName() {
        return "uptime";
    }

    @Override
    public String getDescription() {
        return "Get the bot uptime.";
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
        final long duration = ManagementFactory.getRuntimeMXBean().getUptime();
        TimeToString time = new TimeToString(duration);

        event.reply("My uptime is " + time.toString() + ".").queue();
    }
}
