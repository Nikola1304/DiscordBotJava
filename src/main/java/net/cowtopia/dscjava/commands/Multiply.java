package net.cowtopia.dscjava.commands;

import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.List;

public class Multiply implements ICommand {
    @Override
    public String getName() {
        return "multiply";
    }

    @Override
    public String getDescription() {
        return "Multiplies two numbers";
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
        TextInput operand1 = TextInput.create("operand1","Operand 1", TextInputStyle.SHORT)
                .setPlaceholder("Enter a number")
                .setMinLength(1)
                .setMaxLength(11)
                .setRequired(true)
                .build();

        TextInput operand2 = TextInput.create("operand2","Operand 2",TextInputStyle.SHORT)
                .setPlaceholder("Enter a number")
                .setMinLength(1)
                .setMaxLength(11)
                .setRequired(true)
                .build();

        Modal modal = Modal.create("multiply-modal","Multiply")
                .addActionRows(ActionRow.of(operand1),ActionRow.of(operand2))
                .build();

        event.replyModal(modal).queue();
    }
}
