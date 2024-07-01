package net.cowtopia.dscjava.listeners;

import net.cowtopia.dscjava.libs.GSonConfig;
import net.cowtopia.dscjava.libs.HelpManager;
import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    private List<ICommand> commands = new ArrayList<>();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        //for(Guild guild : event.getJDA().getGuilds()) {

            GSonConfig json_config = GSonConfig.get();
            Guild guild = event.getJDA().getGuildById(json_config.getServerId());
            for(ICommand command : commands) {
                if(command.getOptions() == null) {
                    if(command.isAdminCommand()) {
                        guild.upsertCommand(command.getName(), command.getDescription()).setDefaultPermissions(DefaultMemberPermissions.DISABLED).queue();
                    }
                    else {
                        guild.upsertCommand(command.getName(), command.getDescription()).queue();
                    }
                } else {
                    if(command.isAdminCommand()) {
                        guild.upsertCommand(command.getName(), command.getDescription()).addOptions(command.getOptions()).setDefaultPermissions(DefaultMemberPermissions.DISABLED).queue();
                    }
                    else {
                        guild.upsertCommand(command.getName(), command.getDescription()).addOptions(command.getOptions()).queue();
                    }
                }
            }
        //}
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for(ICommand command : commands) {
            if(command.getName().equals(event.getName())) {
                command.execute(event);
                return;
            }
        }
    }

    public void add(ICommand command) {
        commands.add(command);
        HelpManager.fetch_commands(command.getName(), command.getDescription(), command.getCmdType());
    }
}
