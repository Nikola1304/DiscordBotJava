package net.cowtopia.dscjava.commands.moderation;

import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class Slowmode implements ICommand {
    @Override
    public String getName() {
        return "slowmode";
    }

    @Override
    public String getDescription() {
        return "Sets slowmode in current channel";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER,"amount","New slowmode time (seconds)",true)
                .setRequiredRange(0,21600));
        return options;
    }

    @Override
    public Boolean isAdminCommand() {
        return true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        int amount = (event.getOption("amount")).getAsInt();
        event.getChannel().asTextChannel().getManager().setSlowmode(amount).queue();
        event.reply(event.getMember().getUser().getAsMention() + " set the slowmode to " + amount + " seconds").queue();
    }
}
