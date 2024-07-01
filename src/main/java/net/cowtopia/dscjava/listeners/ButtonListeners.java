package net.cowtopia.dscjava.listeners;

import net.cowtopia.dscjava.libs.CmdType;
import net.cowtopia.dscjava.libs.HelpManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class ButtonListeners extends ListenerAdapter
{
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        String name = event.getButton().getId();
        Guild guild = event.getGuild();
        Message message = event.getMessage();

        List<Role> allRoles = guild.getRoles();
        List<RichCustomEmoji> allEmojis = guild.getEmojis();

        if(name.equals("view-roles-button")) {
            EmbedBuilder rolelistEmbed = new EmbedBuilder()
                    .setTitle("Roles [" + Integer.toString(guild.getRoles().size()) + "]")
                    .setColor(Color.BLUE);

            String allRolesList = "Roles: \n";

            for(int i = 0; i < allRoles.size(); i++) {
                allRolesList += allRoles.get(i).getAsMention() + "\n";
            }

            rolelistEmbed.setDescription(allRolesList);

            event.replyEmbeds(rolelistEmbed.build()).setEphemeral(true).queue();
        }
        else if(name.equals("view-emojis-button")) {
            EmbedBuilder emojilistEmbed = new EmbedBuilder()
                    .setTitle(guild.getEmojis().size() + " Total")
                    .setColor(Color.BLUE);

            String allEmojisList = "Emojis: ";

            // bug: ne uzme ih sve, verovatno zbog ogranicenja velicine stringa
            for(int i = 0; i < allRoles.size(); i++) {

                allEmojisList += allEmojis.get(i).getAsMention();
            }

            emojilistEmbed.setDescription(allEmojisList);

            event.replyEmbeds(emojilistEmbed.build()).setEphemeral(true).queue();
        }
        else if(name.equals("remove-message-button")) {
            String[] badWordContent = event.getMessage().getContentRaw().split(" ");
            String badMessageId = badWordContent[badWordContent.length - 1];

            guild.getTextChannelById(951799037902876682L).deleteMessageById(badMessageId).queue();

            event.reply("Message " + badMessageId + " deleted.").queue();
        } else if(name.equals("ignore-alert-button")) {

            message.delete().queue();

            event.reply("Alert deleted").setEphemeral(true).queue();

        }
        else if(name.equals("help-button")) {

            event.replyEmbeds(HelpManager.get(CmdType.Help)).setEphemeral(true).queue();
        }
        else if(name.equals("help-fun")) {

            event.replyEmbeds(HelpManager.get(CmdType.Fun)).setEphemeral(true).queue();
        }
        else if(name.equals("help-mod")) {
            event.replyEmbeds(HelpManager.get(CmdType.Moderation)).setEphemeral(true).queue();
        }
    }
}
