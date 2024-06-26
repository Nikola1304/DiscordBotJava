package net.cowtopia.dscjava.commands;

import net.cowtopia.dscjava.Main;
import net.cowtopia.dscjava.libs.ICommand;
import net.cowtopia.dscjava.libs.SqliteMan;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class Warn implements ICommand {
    @Override
    public String getName() {
        return "warn";
    }

    @Override
    public String getDescription() {
        return "Warns a user";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER,"user","user you want warned",true));
        options.add(new OptionData(OptionType.STRING,"reason","reason you warned that user",false));
        return options;
    }

    @Override
    public DefaultMemberPermissions getPermission() {
        return DefaultMemberPermissions.DISABLED;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping warnedUsr = event.getOption("user");
        Member warnedUser = warnedUsr.getAsMember();

        String reasonWarn;
        try {
            OptionMapping reasonWrn = event.getOption("reason");
            reasonWarn = reasonWrn.getAsString();
        }
        catch (NullPointerException e) {
            reasonWarn = "No reason provided";
        }

        SqliteMan app = new SqliteMan();
        app.insertNewReason(Main.greader.getDBName(),warnedUser.getIdLong(),reasonWarn,event.getMember().getIdLong());
        event.reply("Warn recorded successfully").queue();
    }
}
