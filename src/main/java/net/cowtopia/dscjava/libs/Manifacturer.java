package net.cowtopia.dscjava.libs;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Manifacturer
{
    // ne zelim da konstantno citam iz text fajla, tako da se objekat generise na pocetku i reusa
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
