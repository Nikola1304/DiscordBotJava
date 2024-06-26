package net.cowtopia.dscjava.commands;

import net.cowtopia.dscjava.libs.HttpUrl;
import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Cat implements ICommand {
    @Override
    public String getName() {
        return "cat";
    }

    @Override
    public String getDescription() {
        return "shows you an image of a cat!";
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
        //String url = "http://thecatapi.com/api/images/get?format=src&type=png";
        String url = "https://api.thecatapi.com/api/images/get?format=src&type=png";
        EmbedBuilder catBuilder = new EmbedBuilder()
                .setTitle("Here's a cat!")
                .setImage(HttpUrl.whereUrlRedirects(url));

        event.replyEmbeds(catBuilder.build()).queue();
    }
}
