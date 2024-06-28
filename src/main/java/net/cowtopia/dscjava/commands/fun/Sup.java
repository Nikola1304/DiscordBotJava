package net.cowtopia.dscjava.commands.fun;

import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.List;

public class Sup implements ICommand {
    @Override
    public String getName() {
        return "sup";
    }

    @Override
    public String getDescription() {
        return "Says sup to someone";
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
        TextInput nameModal = TextInput.create("sup-name","Name", TextInputStyle.SHORT)
                .setMinLength(1)
                .setRequired(true)
                .setPlaceholder("The recipient")
                .build();

        TextInput messageModal = TextInput.create("sup-message","Message", TextInputStyle.PARAGRAPH)
                .setMinLength(10)
                .setMaxLength(100)
                .setRequired(true)
                .setValue("You smell like a wet sock")
                .build();

        Modal modal = Modal.create("sup-modal","Say Wassup")
                .addActionRows(ActionRow.of(nameModal), ActionRow.of(messageModal))
                .build();

        event.replyModal(modal).queue();
    }
}
