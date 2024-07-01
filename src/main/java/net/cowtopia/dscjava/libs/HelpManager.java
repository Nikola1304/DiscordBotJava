package net.cowtopia.dscjava.libs;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.ArrayList;

public class HelpManager {

    private static MessageEmbed FunEmbed;
    private static MessageEmbed HelpEmbed;
    private static MessageEmbed ModerationEmbed;

    private static ArrayList<String> _fun_names = new ArrayList<>();
    private static ArrayList<String> _fun_descriptions = new ArrayList<>();

    private static ArrayList<String> _help_names = new ArrayList<>();
    private static ArrayList<String> _help_descriptions = new ArrayList<>();

    private static ArrayList<String> _moderation_names = new ArrayList<>();
    private static ArrayList<String> _moderation_descriptions = new ArrayList<>();


    public static MessageEmbed get(CmdType t) {

        // opet, sve moze da se spoji preko modifikovanog fabric patterna, ali me mrzi
        if(t == CmdType.Fun) {
            if(FunEmbed == null) {
                FunEmbed = build(t, _fun_names, _fun_descriptions);
            }
            return FunEmbed;
        }
        else if(t == CmdType.Help) {
            if(HelpEmbed == null) {
                HelpEmbed = build(t, _help_names, _help_descriptions);
            }
            return HelpEmbed;
        }
        else { // if(t == CmdType.Moderation) {
            if(ModerationEmbed == null) {
                ModerationEmbed = build(t, _moderation_names, _moderation_descriptions);
            }
            return ModerationEmbed;
        }
    }

    private static MessageEmbed build(CmdType t, ArrayList<String> names, ArrayList<String> descriptions) {

        EmbedBuilder helpEmbedBuild = new EmbedBuilder();
        helpEmbedBuild.setTitle("Commands");
        helpEmbedBuild.setColor(Color.CYAN);
        for(int i = 0; i < names.size(); i++) {
            helpEmbedBuild.addField("/" + names.get(i), descriptions.get(i), false);
            if(i == 24) break;
        }
        return helpEmbedBuild.build();
    }


    public static void fetch_commands(String name, String description, CmdType type) {

        // ovo moze pametnije preko modifikovanog fabric patterna, ali me mrzi
        if(type == CmdType.Fun) {
            _fun_names.add(name);
            _fun_descriptions.add(description);
        }
        else if(type == CmdType.Help) {
            _help_names.add(name);
            _help_descriptions.add(description);
        }
        else if(type == CmdType.Moderation) {
            _moderation_names.add(name);
            _moderation_descriptions.add(description);
        }
    }
}
