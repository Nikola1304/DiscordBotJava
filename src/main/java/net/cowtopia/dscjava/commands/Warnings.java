package net.cowtopia.dscjava.commands;

import net.cowtopia.dscjava.Main;
import net.cowtopia.dscjava.libs.ICommand;
import net.cowtopia.dscjava.libs.ISLDPair;
import net.cowtopia.dscjava.libs.SqliteMan;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Warnings implements ICommand {
    @Override
    public String getName() {
        return "warnings";
    }

    @Override
    public String getDescription() {
        return "Lists all warnings user has";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER,"user","user you want warnings shown",true));
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

        SqliteMan app = new SqliteMan();
        ISLDPair[] sviWarnovi = app.allReasons(Main.greader.getDBName(),warnedUser.getIdLong());

        long warnedUserId = warnedUser.getIdLong();
        int num = app.countAllWarnings(Main.greader.getDBName(),warnedUserId);

        EmbedBuilder warnListEB = new EmbedBuilder();
        warnListEB.setAuthor(num + " Warnings for " + warnedUser.getUser().getName() + " (" + warnedUserId + ")");

        for(int i = 0; i < num; i++) {
            warnListEB.addField("Moderator: " + event.getJDA().getUserById(Long.toString(sviWarnovi[i].getLong())).getName()
                    , "`" + sviWarnovi[i].getIndex() + "`: " + sviWarnovi[i].getStr() + " - <t:" + sviWarnovi[i].getTime() + ":R>", false);
        }

        warnListEB.setColor(Color.RED);

        MessageEmbed warnListEmbed = warnListEB.build();
        event.replyEmbeds(warnListEmbed).queue();
        warnListEB.clear();
    }
}
