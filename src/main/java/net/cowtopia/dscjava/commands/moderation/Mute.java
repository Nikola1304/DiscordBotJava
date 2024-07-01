package net.cowtopia.dscjava.commands.moderation;

import net.cowtopia.dscjava.Main;
import net.cowtopia.dscjava.libs.CmdType;
import net.cowtopia.dscjava.libs.GSonConfig;
import net.cowtopia.dscjava.libs.ICommand;
import net.cowtopia.dscjava.libs.MuteDuration;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class Mute implements ICommand {

    private static final Command.Choice choiceSeconds = new Command.Choice("Seconds", 1);
    private static final Command.Choice choiceMinutes = new Command.Choice("Minutes", 2);
    private static final Command.Choice choiceHours = new Command.Choice("Hours", 3);
    private static final Command.Choice choiceDays = new Command.Choice("Days", 4);

    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getDescription() {
        return "Mutes user";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER,"user","user you want muted",true));
        options.add(new OptionData(OptionType.INTEGER,"amount","amount of time user will be muted",true)
                .setRequiredRange(1,2419200));
        options.add(new OptionData(OptionType.INTEGER, "type","Type of time measurement",true)
                .addChoices(choiceSeconds,choiceMinutes,choiceHours,choiceDays));
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

        OptionMapping muteUsr = event.getOption("user");
        OptionMapping amountMute = event.getOption("amount");
        OptionMapping typeMute = event.getOption("type");
        //OptionMapping reasonMute = event.getOption("reason");

        Member muteUser = muteUsr.getAsMember();
        int duzinaMuta = amountMute.getAsInt();
        int jedinicaMuta = typeMute.getAsInt();

        //String reason = "No reason provided";
        //if(reasonMute != null) {
        //    reason = reasonMute.getAsString();
        //}

        MuteDuration muteDuration = new MuteDuration(duzinaMuta, jedinicaMuta);

        try {
            event.getGuild().timeoutFor(muteUser, muteDuration.getDuration(), muteDuration.getDurationUnit()).queue();
            event.reply(muteUser.getUser().getName() + " has been muted").queue();
        }
        catch (HierarchyException e) {
            event.reply(GSonConfig.get().getHierarchyExMsg()).queue();
        }
    }
}
