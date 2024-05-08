package net.cowtopia.dscjava.listeners;

import net.cowtopia.dscjava.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WelcomeLeaveListeners extends ListenerAdapter
{
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event)
    {
        Guild guild = event.getGuild();
        TextChannel welcomechannel = guild.getTextChannelById(Main.greader.getWelcomeId());

        // naravno sve ovo treba poboljsati ali baza je tu
        // dodati lepu porukicu, updatovanje channela sa member listama, potencijalno davanje nekih rolova

        //guild.getDefaultChannel().sendMessage("Welcome " + event.getUser().getAsMention() + " to the server!").queue();
        welcomechannel.sendMessage("Welcome " + event.getUser().getAsMention() + " to the server!").queue();


        // ovo nije najefikasnije na velikim serverima ali ako ikada postanem toliko veliki resavanje ovog problema bice jedan od manjih problema
        // takodje discord API ogranicava promene na channelima na dve promene u 10 minuta

        //guild.getVoiceChannelById(1211789463475327067L).getManager().setName("Total Users: " + guild.getMembers().size()).queue();
        // ubijam API ovime, ostavicu ga disabled za sada (27.02.2024.)
        //guild.getVoiceChannelById(membersvcid).getManager().setName("Members: " + guild.getMemberCount()).queue();
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        Guild guild = event.getGuild();
        TextChannel leavechannel = guild.getTextChannelById(Main.greader.getLeaveId());
        // bruhh sta je ovo
        //List<TextChannel> leavechlist = guild.getTextChannelsByName("welcome",true);


        // Isto kao i za welcome
        leavechannel.sendMessage(event.getUser().getAsMention() + " has left the server.").queue();
        //guild.getVoiceChannelById(membersvcid).getManager().setName("Members: " + guild.getMemberCount()).queue();
    }
}
