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
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    // magija koja mi omogucava da ne moram da manualno pusham svaku klasu
    public CommandManager() {
        // Use Reflections to find all classes implementing MyInterface
        Reflections reflections = new Reflections("net.cowtopia.dscjava.commands");
        Set<Class<? extends ICommand>> classes = reflections.getSubTypesOf(ICommand.class);

        // Instantiate each class and add to the list
        for (Class<? extends ICommand> clazz : classes) {
            try {
                ICommand instance = clazz.getDeclaredConstructor().newInstance();
                add(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void add(ICommand command) {
        commands.add(command);
        HelpManager.fetch_commands(command.getName(), command.getDescription(), command.getCmdType());
    }
}

// Global and Guild Commands
// Global Commands - can be used anywhere: any guild that your bot is in and also in DMs
// njih dodajem tako sto umesto guild. dole napisem bot. direktno
// ali njima se necu igrati
// Guild Commands - can only be used in a specific guild (I need this)
