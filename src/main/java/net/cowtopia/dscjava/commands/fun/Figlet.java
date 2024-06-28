package net.cowtopia.dscjava.commands.fun;

import com.github.lalyos.jfiglet.FigletFont;
import net.cowtopia.dscjava.libs.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Figlet implements ICommand {
    @Override
    public String getName() {
        return "figlet";
    }

    @Override
    public String getDescription() {
        return "Creates ascii art from text";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING,"text","text you want converted to ascii art",true));
        return options;
    }

    @Override
    public Boolean isAdminCommand() {
        return false;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String txt = (event.getOption("text")).getAsString();
        try
        {
            String asciiArt = FigletFont.convertOneLine(txt);
            event.reply("```"+asciiArt+"```").queue();
        } catch (IOException e)
        {
            event.reply("IOException, idk what that is").queue();
            //throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            event.reply("Im sorry, I cant do that").queue();
        }
    }
}
