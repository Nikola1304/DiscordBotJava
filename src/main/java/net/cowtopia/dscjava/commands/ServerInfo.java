package net.cowtopia.dscjava.commands;

import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ServerInfo implements ICommand {
    @Override
    public String getName() {
        return "serverinfo";
    }

    @Override
    public String getDescription() {
        return "Shows server info";
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

        Guild guild = event.getGuild();

        EmbedBuilder sinfoEmb = new EmbedBuilder()
                .setTitle(guild.getName())
                .setThumbnail(guild.getIconUrl())
                .setColor(Color.BLUE)

                .addField("Owner", guild.getOwner().getUser().getName(), true)
                .addField("Members", Integer.toString(guild.getMemberCount()), true)
                .addField("Roles", Integer.toString(guild.getRoles().size()), true)
                .addField("Category Channels", Integer.toString(guild.getCategories().size()), true)
                .addField("Text Channels", Integer.toString(guild.getTextChannels().size()), true)
                .addField("Voice Channels", Integer.toString(guild.getVoiceChannels().size()), true)

                .setFooter("ID: " + guild.getId() + " | Server created: " + DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(guild.getTimeCreated()));

        event.replyEmbeds(sinfoEmb.build()).addActionRow(
                Button.primary("view-roles-button", "View Roles"),
                Button.primary("view-emojis-button", "View Emojis")
        ).queue();
    }
}
