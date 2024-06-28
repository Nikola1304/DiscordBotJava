package net.cowtopia.dscjava.commands.help;

import net.cowtopia.dscjava.libs.GSonConfig;
import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Members implements ICommand {
    @Override
    public String getName() {
        return "members";
    }

    @Override
    public String getDescription() {
        return "Shows you amount of members in server";
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
        Guild guild = event.getGuild();
        User author = event.getMember().getUser();
        GSonConfig json_config = GSonConfig.get();

        int mc = guild.getMemberCount();
        event.reply("There are " + mc + " members in this server").queue();
        if(author.getId().equals(guild.getOwner().getUser().getId())) {
            guild.getVoiceChannelById(json_config.getMembersId()).getManager().setName("Members: " + mc).queue();
        }
    }
}
