package net.cowtopia.dscjava.commands.moderation;

import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class Purge implements ICommand {
    @Override
    public String getName() {
        return "purge";
    }

    @Override
    public String getDescription() {
        return "Deletes specified amount of messages";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER,"amount","Amount of messages you want deleted",true)
                .setRequiredRange(1,100));
        return options;
    }

    @Override
    public Boolean isAdminCommand() {
        return true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        int brojPoruka = (event.getOption("amount")).getAsInt();
        List<Message> delMsg = event.getChannel().getHistory().retrievePast(brojPoruka).complete();
        event.getGuildChannel().deleteMessages(delMsg).queue();
        event.reply("Successfully purged " + brojPoruka + " messages").queue();
    }
}
