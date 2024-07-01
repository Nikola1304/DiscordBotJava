package net.cowtopia.dscjava.commands.moderation;

import net.cowtopia.dscjava.libs.CmdType;
import net.cowtopia.dscjava.libs.GSonConfig;
import net.cowtopia.dscjava.libs.ICommand;
import net.cowtopia.dscjava.libs.SqliteMan;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class DelWarnings implements ICommand {

    private static final Command.Choice choiceUser = new Command.Choice("User", 5);
    private static final Command.Choice choiceMod = new Command.Choice("Moderator", 6);

    @Override
    public String getName() {
        return "delwarnings";
    }

    @Override
    public String getDescription() {
        return "Delets all warnings made by mod or assigned to a certain user";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER,"usermod","Choose which warnings you want deleted",true)
                .addChoices(choiceUser, choiceMod));
        options.add(new OptionData(OptionType.USER,"user","user with warns or mod who slapped those warns",true));
        return options;
    }

    @Override
    public Boolean isAdminCommand() {
        return true;
    }

    @Override
    public CmdType getCmdType() {
        return CmdType.Moderation;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping usrmod = event.getOption("usermod");
        OptionMapping usr = event.getOption("user");

        GSonConfig json_config = GSonConfig.get();

        int usermod = usrmod.getAsInt();
        long userid = usr.getAsMember().getIdLong();

        SqliteMan app = new SqliteMan();

        if(usermod == 5) { // user
            app.deleteMultipleReasons(json_config.getDBName(),"user_id",userid);
            event.reply("Warnings deleted successfully").queue();
        }
        else if (usermod == 6) { // mod
            app.deleteMultipleReasons(json_config.getDBName(),"author_id",userid);
            event.reply("Warnings deleted successfully").queue();
        }
        else {
            event.reply("Invalid argument").queue();
        }
    }
}
