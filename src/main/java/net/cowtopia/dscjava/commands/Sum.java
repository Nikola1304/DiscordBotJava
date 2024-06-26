package net.cowtopia.dscjava.commands;

import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class Sum implements ICommand {
    @Override
    public String getName() {
        return "sum";
    }

    @Override
    public String getDescription() {
        return "add two numbers together";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER,"operand1","the first number",true)
                .setRequiredRange(1,Integer.MAX_VALUE));
        options.add(new OptionData(OptionType.INTEGER,"operand2","the second number",true)
                .setRequiredRange(1,Integer.MAX_VALUE));
        return options;
    }

    @Override
    public DefaultMemberPermissions getPermission() {
        return DefaultMemberPermissions.ENABLED;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping operand1 = event.getOption("operand1");
        OptionMapping operand2 = event.getOption("operand2");

        if(operand1 == null || operand2 == null) {
            event.reply("No numbers were provided!").queue();
            return;
        }

        int sum = operand1.getAsInt() + operand2.getAsInt();
        event.reply("The sum is: " + sum).queue();
    }
}
