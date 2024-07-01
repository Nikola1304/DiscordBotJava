package net.cowtopia.dscjava.commands.help;

import net.cowtopia.dscjava.libs.CmdType;
import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class License implements ICommand {
    @Override
    public String getName() {
        return "license";
    }

    @Override
    public String getDescription() {
        return "Shows you code license (maybe useful)";
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
    public CmdType getCmdType() {
        return CmdType.Help;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("https://github.com/Nikola1304/DiscordBotJava/blob/main/LICENSE").queue();
    }
}
