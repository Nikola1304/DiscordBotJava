package net.cowtopia.dscjava.commands;

import net.cowtopia.dscjava.Main;
import net.cowtopia.dscjava.libs.ICommand;
import net.cowtopia.dscjava.libs.SqliteMan;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class DelWarn implements ICommand {
    @Override
    public String getName() {
        return "delwarn";
    }

    @Override
    public String getDescription() {
        return "Delets warning";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER,"warningid", "ID of warning you want removed",true)
                .setRequiredRange(1,999999999));
        return options;
    }

    @Override
    public DefaultMemberPermissions getPermission() {
        return DefaultMemberPermissions.DISABLED;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping wrnIndex = event.getOption("warningid");
        int warnIndex = wrnIndex.getAsInt();

        SqliteMan app = new SqliteMan();
        app.deleteReason(Main.greader.getDBName(),warnIndex);
        event.reply("Warn deleted sucessfully").queue();
    }
}
