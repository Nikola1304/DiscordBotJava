package net.cowtopia.dscjava.commands.help;

import net.cowtopia.dscjava.libs.CmdType;
import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.List;

public class Help implements ICommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Displays a list of available commands.";
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

    private static MessageEmbed INSTANCE;

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        // event.deferReply().setEphemeral(true).queue();
        // event.getHook().sendMessage("Nema tebi pomoci decko").queue();

        // event.reply("Nema tebi pomoci decko").queue();

        if(INSTANCE == null) {
            INSTANCE = new EmbedBuilder()
                    .setTitle("Help commands")
                    .setDescription("Click on buttons to get help commands")
                    .setColor(Color.yellow).build();
        }

        event.replyEmbeds(INSTANCE).addActionRow(
                Button.primary("help-button", "Help commands"),
                Button.primary("help-fun","Fun commands"),
                Button.primary("help-mod","Moderation commands")
        ).queue();
    }
}
