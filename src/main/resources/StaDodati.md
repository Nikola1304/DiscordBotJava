Mod cmds
ban
kick
tempmute
mute (moze bude deo tempmuta a da se tempmute zove samo mute)
ne dodavati kancerogeni role manager nego to raditi manualno











// ban example iz chatdjipitija
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BanCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        
        // Check if the command is "!ban" and if there is a mentioned user
        if (args.length >= 2 && args[0].equalsIgnoreCase("!ban")) {
            // Get the mentioned users
            List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
            
            // Ensure at least one user is mentioned
            if (!mentionedMembers.isEmpty()) {
                // Ban each mentioned user
                for (Member member : mentionedMembers) {
                    event.getGuild().ban(member, 0).reason("Banned by bot.").queue(
                        // Success callback
                        success -> event.getChannel().sendMessage("Banned user: " + member.getEffectiveName()).queue(),
                        // Error callback
                        error -> event.getChannel().sendMessage("Failed to ban user: " + member.getEffectiveName()).queue()
                    );
                }
            } else {
                // No mentioned users
                event.getChannel().sendMessage("You need to mention the user(s) you want to ban.").queue();
            }
        }
    }
}

In this code:

    We listen for messages in guilds using onGuildMessageReceived.
    We split the message content into arguments to check for the ban command and mentioned users.
    If the command is "!ban" and at least one user is mentioned, we loop through the mentioned users and ban each one using event.getGuild().ban(member, 0).reason("Banned by bot.").queue().
    We provide success and error callbacks to handle the ban operation results.
    If no users are mentioned, we send a message indicating that the user(s) to be banned should be mentioned.

To use this command, register the BanCommand as an event listener with your JDA instance, similar to previous examples. Ensure that your bot has the necessary permissions to ban users in the server.












Guild guild=event.getGuild();
Member member=event.getMentionedMembers().get(0);//TODO check if exists
guild.ban(member,0,"ban command").queue();



# in code


String[] args = event.getMessage().getContentRaw().split("\\s+"); // gets message from mod and splits every whitespace.

if (args[0].equalsIgnoreCase(Main.prefix + "ban")) {
if(args.length==1){//no argument
//error message
}
else if(event.getMentionedMembers().isEmpty()){//no mentioned members, try to use argument as ID
event.getGuild().ban(args[1],0,"ban command").queue();
}else{//mentioned members

        event.getGuild().ban(event.getMentionedMembers().get(0),0,"ban command").queue();
    }

}







MEMBER LIST (BEZ BOTOVA)


/*
event.getGuild().loadMembers().onSuccess(members -> {
int users = 0;
int bots = 0;
for(Member member: members){
if (member.getUser().isBot()) bots++;
else users++;
}
}); // kod zla koji ne treba implementirati, ali nek stoji ovde za slucaj da mi zatreba
*/