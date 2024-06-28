package net.cowtopia.dscjava.commands.fun;

import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Ping implements ICommand {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "server ping";
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
    public void execute(SlashCommandInteractionEvent event) {
        long startTime = System.currentTimeMillis();
        event.reply("Pinging...").queue(response -> {
            long endTime = System.currentTimeMillis();
            long ping = endTime - startTime;
            response.editOriginal("Ping: " + ping + "ms").queue();
        });
    }
}

