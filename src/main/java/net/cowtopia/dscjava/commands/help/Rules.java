package net.cowtopia.dscjava.commands.help;

import net.cowtopia.dscjava.libs.CmdType;
import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Rules implements ICommand {
    @Override
    public String getName() {
        return "rules";
    }

    @Override
    public String getDescription() {
        return "Shows you server rules";
    }

    @Override
    public List<OptionData> getOptions() {
        //return List.of();
        return null;
    }

    @Override
    public Boolean isAdminCommand() {
        return false;
    }

    @Override
    public CmdType getCmdType() {
        return CmdType.Help;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        event.replyEmbeds(get()).queue();
    }

    private static MessageEmbed INSTANCE;
    private static final String ruleFile = "rules.txt";


    private static MessageEmbed get() {
        if(INSTANCE == null) {
            try {
                INSTANCE = buildRulesEmbed(ruleFile);
            } catch (IOException e) {
                System.out.println("Neki exception kad kreiras rules embed, ne znam kako do njega dolazi");
            }
        }
        return INSTANCE;
    }

    public static MessageEmbed buildRulesEmbed(String rulesFile) throws IOException
    {
        EmbedBuilder rulesEmbedBuild = new EmbedBuilder();
        rulesEmbedBuild.setTitle("Server rules");
        rulesEmbedBuild.setColor(Color.CYAN);

        BufferedReader reader = new BufferedReader(new FileReader(rulesFile));

        String testNesto = reader.readLine();
        String kontent = "1. " + testNesto;

        if(testNesto != null) {
            String line = reader.readLine();

            int i = 2;
            while (line != null) {
                kontent = kontent + "\n" + i + ". " + line;
                line = reader.readLine();
                i++;
            }
        }
        else {
            kontent = "Please insert rules in rules.txt";
        }

        rulesEmbedBuild.setDescription(kontent);
        return rulesEmbedBuild.build();
    }
}
