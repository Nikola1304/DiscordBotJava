package net.cowtopia.dscjava.listeners;

import net.cowtopia.dscjava.Main;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class DiscordBot extends ListenerAdapter
{
    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        MessageChannel channel = event.getChannel();
        Message message = event.getMessage();


        if(channel.getName().equals("counting")) {
            channel.getHistory().retrievePast(1)
                    .map(messages -> messages.get(0))
                    .queue(msg -> { // success callback
                        if(message.equals(msg)) msg.delete().queue();
                    });
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();

        User author = event.getAuthor();
        Guild guild = event.getGuild();

        Member authorMember = event.getMember();
        String mentionUser = author.getAsMention();
        // lista svih membera koji su mentionovani u poruci sa koje mozemo da skinemo recimo prvog kada radimo ban, kick, mute itd
        List<Member> mentionedPeople = message.getMentions().getMembers();

        // ja ne kontam razliku izmedju ovih channela pa sam samo hakovao ovo jer ne znam sta bih drugo
        TextChannel textChannel = guild.getTextChannelById(channel.getId());


        //GuildChannel guildChannel = guild.getGuildChannelById(channel.getId());
        GuildChannel guildChannel = event.getGuildChannel();

        Role everyoneRole = guild.getPublicRole();


        if(author.isBot()) return;
        // this exists to prevent user to acidentally ban this bot
        if(content.contains(event.getJDA().getSelfUser().getAsMention())) {
            return;
        }

        /*
        channel.sendMessage("author: " + author +
                "\nmember: " + authorMember + "" +
                "\nmember1: " + guild.getMemberById(author.getId())).queue();
         */


        // problem: attachment moze da bude nesto drugo osim slike (zip, pdf, bla bla bla)
        // mozda problem: mozes da napises tekst kad posaljes sliku
        if(channel.getName().equals("images")) {
            if(message.getAttachments().isEmpty()) {
                message.delete().queue();
            }
            return;
            //if(channel.getId().equals("910103074490699826")) {
        } else if(channel.getName().equals("counting")) {
            channel.getHistory().retrievePast(2)
                    .map(messages -> messages.get(1)) // ovo pretpostavlja da u channelu postoji makar 2 poruke (prethodna i tvoja)
                    .queue(msg -> { // success callback
                        if(msg.getAuthor().getId().equals(author.getId())) {
                            message.delete().queue();
                        }
                        // bug - ako korisnik edita poruku, bice obrisana jer moze da zbuni counting bota, ali to utice da counting streak bude cudan (1 2 4 5 6 8 9 10 11 15 16 17...)
                        // bug2 - mora da postoji makar jedan broj (aka poruka) u channelu da bi radilo
                        else {
                            try {
                                if(Integer.parseInt(message.getContentRaw()) != (Integer.parseInt(msg.getContentRaw()) + 1)) message.delete().queue();
                            }
                            catch (NumberFormatException e) {
                                message.delete().queue();
                            }

                            // System.out.println("The second most recent message is: " + msg.getContentDisplay());
                        }

                    });
            return;
        }

        String[] BAD_WORDS = {"weener", "poop", "frick", "darn", "ios"};

        for(String badWord : BAD_WORDS) {
            if(event.getMessage().getContentRaw().contains(badWord)) {

                channel.sendMessage("You said a bad word! I am telling on you.").queue();

                TextChannel staffChannel = event.getJDA().getTextChannelById(Main.staffchid);
                if(staffChannel != null) {

                    Button removeButton = Button.danger("remove-message-button", "Remove Message");
                    Button ignoreAlert = Button.secondary("ignore-alert-button", "Ignore Alert");


                    MessageCreateBuilder reportMessage = new MessageCreateBuilder()
                            .addContent(author.getEffectiveName() + " said a bad word. Click the button below to remove it. Message ID: " + event.getMessageId())
                            .addActionRow(removeButton, ignoreAlert);

                    staffChannel.sendMessage(reportMessage.build()).queue();
                }
            }
        }

        if (content.startsWith("!say")) {
            // moj ID, jer drugi nisu dostojni ove komande
            if(author.getId().equals("716962243400564786")) {
                try {
                    channel.sendMessage(content.substring(5)).queue();
                    message.delete().queue();
                }
                catch (StringIndexOutOfBoundsException e) {
                    channel.sendMessage("Tell me what to say").queue();
                }
            } else System.out.println(author.getName() + " pokusava da koristi ovu komandu");
            return;
        }

        if(content.equals("ping")) {
            channel.sendMessage("Pong!").queue();
        }
        else if(content.equals("kys")){
            channel.sendMessage("No, u").queue();
        }
    }

    public void onModalInteraction(@NotNull ModalInteractionEvent event) {

        Guild guild = event.getGuild();
        String name = event.getModalId();

        if(name.equals("sup-modal")) {
            String supname = event.getValue("sup-name").getAsString();
            String message = event.getValue("sup-message").getAsString();


            Optional<Member> memberOptional = guild.getMembersByName(supname,true).stream().findFirst();

            if(memberOptional.isPresent()) {
                event.reply("Sup, " + memberOptional.get().getAsMention() + "! Message: " + message).queue();
            } else {
                event.reply("Sup, " + supname + ". Message: " + message).queue();
            }
        }
        else if(event.getModalId().equals("multiply-modal")) {
            String num1str = event.getValue("operand1").getAsString();
            String num2str = event.getValue("operand2").getAsString();

            try {
                int num1 = Integer.parseInt(num1str);
                int num2 = Integer.parseInt(num2str);

                int product = num1 * num2;
                event.reply("The product is: " + product).queue();

            } catch (NumberFormatException e) {
                event.reply("One of the numbers was not a number").setEphemeral(true).queue();
            }

        }
    }

    // potencijalno maknuti deo koda ispod ovog komentara
    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent event) {
        String channelName = event.getChannel().getName();

        TextChannel channel = event.getGuild().getTextChannelById(1218562604830560266L);

        if(channel != null) {
            channel.sendMessage("The channel: \"" + channelName + "\" was deleted").queue();
        }
    }

    /*
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Guild guild = event.getJDA().getGuildById(817404696687673454L);
        TextChannel channel = guild.getTextChannelById(generalchid);
        channel.sendMessage("Podigao sam se iz mrtvih").queue();
    }*/
}
