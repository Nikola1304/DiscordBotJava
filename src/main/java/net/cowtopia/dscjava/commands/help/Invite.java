package net.cowtopia.dscjava.commands.help;

import net.cowtopia.dscjava.libs.CmdType;
import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Invite implements ICommand {

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getDescription() {
        return "Sends you permanent server invite link";
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
        event.reply("https://discord.gg/zrEUQENmRr").queue();
    }
}
