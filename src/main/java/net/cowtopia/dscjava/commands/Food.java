package net.cowtopia.dscjava.commands;

import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class Food implements ICommand {
    @Override
    public String getName() {
        return "food";
    }

    @Override
    public String getDescription() {
        return "Otkriva ti tajne univerzuma";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "foodname", "ime tvoje omiljene hrane",true));
        return options;

    }

    @Override
    public DefaultMemberPermissions getPermission() {
        return DefaultMemberPermissions.ENABLED;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping option = event.getOption("foodname");

        if(option.equals(null)) {
            event.reply("for some reason I cant explain, a food name was not provided").queue();
            return;
        }

        String favouriteFood = option.getAsString();
        event.reply("Your favourite food is " + favouriteFood).queue();
    }
}
