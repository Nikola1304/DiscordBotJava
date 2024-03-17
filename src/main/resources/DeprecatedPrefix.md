        if(content.startsWith(prefix)) {
            String necmd = content.substring(1);
            String cmd = necmd.replaceAll("\\s.*", "").toLowerCase();

            //channel.sendMessage(cmd).queue();

            // pretvara celu poruku u niz stringova, onda mogu da biram sta mi treba :)
            // bug: ako stavim 2 razmaka napravi gluposti
            String[] niz_reci = necmd.split("\\s");

            // radi proveru da li je korisnik uneo jos neki podatak osim same komande
            boolean contentPostoji = true;
            String msgContentRaw = "Placeholder";
            if(niz_reci.length > 1) msgContentRaw = content.substring(cmd.length()+2);
            else contentPostoji = false;

            if(cmd.equals("help")) {
                channel.sendMessage("Nema tebi pomoci decko").queue();
            }
            else if (cmd.equals("invite")) {
                message.reply("https://discord.gg/zrEUQENmRr").queue();
            }
            else if (cmd.equals("eksperiment")) {
                // nemam pojma sta ovo radi
                channel.sendMessage(author.retrieveProfile().toString()).queue();
                channel.sendMessage("Kolicina podatka data botu: " + Integer.toString(niz_reci.length)).queue();
            }
            else if (cmd.equals("ping")) {

                // ukrao iz chat djipitija
                long startTime = System.currentTimeMillis(); // Record the current time
                channel.sendMessage("Pinging...").queue(response -> {
                    long endTime = System.currentTimeMillis(); // Record the time after receiving the message
                    // Calculate the bot's ping
                    long ping = endTime - startTime;
                    // Reply with the ping
                    response.editMessageFormat("Ping: %dms", ping).queue();
                });
            }
            else if (cmd.equals("purge")) {
                if(!message.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `MESSAGE_MANAGE` permission").queue();
                    return;
                }
                if(!contentPostoji) {
                    channel.sendMessage("Please specify an amount of messages you want deleted!").queue();
                    return;
                }

                int brojPoruka;
                try {
                    brojPoruka = Integer.valueOf(niz_reci[1]);
                    if(brojPoruka < 0) brojPoruka *= -1; // magic number
                    brojPoruka += 1; // da obrise i moju purge poruku
                    if(brojPoruka > 100) brojPoruka = 100;
                }
                catch (NumberFormatException e) {
                    channel.sendMessage("Input you provided is not a number!").queue();
                    return;
                }

                List<Message> delMsg = channel.getHistory().retrievePast(brojPoruka).complete();
                event.getGuildChannel().deleteMessages(delMsg).queue();
                channel.sendMessage("Successfully purged " + (brojPoruka - 1) + " messages").queue();

            }
            else if(cmd.equals("ban")) {
                if(!message.getMember().hasPermission(Permission.BAN_MEMBERS)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `BAN_MEMBERS` permission").queue();
                    return;
                }

                // prebacio sam ovo u funkciju jer ce mi trebati za kick (jezivo znam)
                String reasonBan = bankickjezivafunkcija(niz_reci);

                if(!mentionedPeople.isEmpty()) {
                    //User banUser = mentionedPeople.get(0).getUser();
                    //banUser.openPrivateChannel().flatMap(chenl -> chenl.sendMessage("DMovan si")).queue();

                    guild.ban(mentionedPeople.get(0),0, TimeUnit.MINUTES).reason(reasonBan).queue();
                            //.queueAfter(500, TimeUnit.MILLISECONDS); // timeunit odredjuje koliko ce da ide nazad i obrise poruke
                    channel.sendMessage(mentionedPeople.get(0).getAsMention() + " has been banned!").queue();
                }
                else {
                    channel.sendMessage("Specify who you want banned").queue();
                }
            }
            else if (cmd.equals("kick")) {
                if(!message.getMember().hasPermission(Permission.KICK_MEMBERS)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `KICK_MEMBERS` permission").queue();
                    return;
                }

                String reasonKick = bankickjezivafunkcija(niz_reci);

                if(!mentionedPeople.isEmpty()) {
                    guild.kick(mentionedPeople.get(0)).reason(reasonKick).queue();
                    channel.sendMessage(mentionedPeople.get(0).getAsMention() + " has been kicked!").queue();
                }
                else {
                    channel.sendMessage("Specify who you want kicked").queue();
                }
            }
            else if (cmd.equals("mute")) {
                if(!message.getMember().hasPermission(Permission.MODERATE_MEMBERS)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `MODERATE_MEMBERS` permission").queue();
                    return;
                }

                TimeUnit vremenskaJedinica = TimeUnit.MINUTES;
                int duzinaMuta = 5;
                int jedinicaMuta = 2;
                if (niz_reci.length >= 4)
                {
                    try
                    {
                        duzinaMuta = Integer.valueOf(niz_reci[2]);
                        jedinicaMuta = Integer.valueOf(niz_reci[3]);

                    } catch (NumberFormatException e)
                    {
                        // ako opet zaboravim da napisem .queue() mislim da ce se lose stvari desiti
                        channel.sendMessage("Input you provided is not a number!").queue();
                        return;
                    }
                }
                else if (niz_reci.length == 3) {
                    try
                    {
                        duzinaMuta = Integer.valueOf(niz_reci[2]);

                    }
                    catch (NumberFormatException e)
                    {
                        channel.sendMessage("Input you provided is not a number!").queue();
                        return;
                    }
                }

                if(duzinaMuta < 0) duzinaMuta *= -1;

                if (jedinicaMuta == 1) {
                    vremenskaJedinica = TimeUnit.SECONDS;
                    if (duzinaMuta > 10800) duzinaMuta = 10800;
                }
                else if (jedinicaMuta == 3)
                {
                    vremenskaJedinica = TimeUnit.HOURS;
                    if (duzinaMuta > 169) duzinaMuta = 169;
                }
                else if (jedinicaMuta == 4)
                {
                    vremenskaJedinica = TimeUnit.DAYS;
                    if (duzinaMuta > 28) duzinaMuta = 28;
                }
                else
                {
                    vremenskaJedinica = TimeUnit.MINUTES;
                    if (duzinaMuta > 1400) duzinaMuta = 1400;
                }


                if(!mentionedPeople.isEmpty()) {
                    try {
                        guild.timeoutFor(mentionedPeople.get(0), duzinaMuta, vremenskaJedinica).queue();
                        channel.sendMessage(mentionedPeople.get(0).getUser().getName() + " has been muted!").queue();
                    }
                    catch (HierarchyException e) {
                        channel.sendMessage("Sorry, I am not allowed to do that").queue();
                    }
                }
                else {
                    channel.sendMessage("Specify who you want muted").queue();
                }
            }
            else if (cmd.equals("unmute")) {
                if(!message.getMember().hasPermission(Permission.MODERATE_MEMBERS)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `MODERATE_MEMBERS` permission").queue();
                    return;
                }

                if(!mentionedPeople.isEmpty()) {
                    // ne proverava da li korisnik ima timeout, ali nije vazno
                    // ako ne moze da uradi to (fizicki, nema permission, member je jaci od njega), samo ce da baci error, fixaj to kasnije
                    guild.removeTimeout(mentionedPeople.get(0)).queue();
                    channel.sendMessage(mentionedPeople.get(0).getUser().getName() + " has been unmuted!").queue();
                }
                else {
                    channel.sendMessage("Specify who you want unmuted").queue();
                }
            }
            else if (cmd.equals("unban")) {
                if (!message.getMember().hasPermission(Permission.BAN_MEMBERS)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `BAN_MEMBERS` permission").queue();
                    return;
                }

                if(niz_reci.length == 1) {
                    channel.sendMessage("Specify who you want unbanned").queue();
                    return;
                }

                // fix this mess

                long userID;
                try {
                    userID = Long.valueOf(niz_reci[1]);
                }
                catch (NumberFormatException e ) {
                    channel.sendMessage("Input you provided is not a number!").queue();
                    return;
                }
                try {
                    User target = event.getJDA().getUserById(userID);
                    guild.unban(target);
                    channel.sendMessage("User with the id " + userID + " has been unbanned!").queue();
                } catch (IllegalArgumentException e) {
                    // ako opet zaboravim da napisem .queue() mislim da ce se lose stvari desiti
                    channel.sendMessage("Input you provided is not an ID!").queue();
                    return;
                }
                //channel.sendMessage((CharSequence) event.getGuild().retrieveBanList()).queue();
            }
            else if (cmd.equals("ticket")) {

                String chStart = "ticket-";
                EnumSet<Permission> permsTicket = EnumSet.of(Permission.VIEW_CHANNEL,Permission.MESSAGE_SEND);

                if(channel.getName().startsWith(chStart)) {
                    if(contentPostoji) {
                    //if(niz_reci.length >= 2) {
                        if (niz_reci[1].equals("delete")) {
                            channel.delete().queue();
                        }
                        else if (niz_reci[1].equals("lock")) {

                            String imeTicketOwnera = channel.getName().substring(7);
                            Long idTicketOwnera = Long.parseLong(imeTicketOwnera);
                            //User userTicketOwner = event.getJDA().getUserById(idTicketOwnera);
                            Member memberTicketOwner = guild.getMemberById(idTicketOwnera);

                            // task za buduceg mene: skontaj kako da clearas sve da bude default bez da to unosis manualno
                            // (da se user fizicki izbrise iz channel cuda, a ne samo da mu povuce perms)
                            guildChannelEvil.upsertPermissionOverride(memberTicketOwner).clear(permsTicket).queue();



                            // Role role = guild.getRoleById(817427547678703668L);
                            //PermissionOverride override = guildChannelEvil.getPermissionOverride(role); // Get an existing override for a role
                            //if (override.equals(null)) {
                            //    override = guildChannelEvil.createPermissionOverride(role);//.complete(); // Create a new override if it doesn't exist
                            //}
                            //override.getManager().grant(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND).queue();

                            // List<PermissionOverride> overrides = guildChannelEvil.upsertPermissionOverride()

                            // Remove all existing permission overrides
                            //for (PermissionOverride override : overrides) {
                            //    override.delete().queue();
                            //}

                            //guildChannelEvil.upsertPermissionOverride(role).deny(Permission.VIEW_CHANNEL).queue();

                            // Member member = guild.getMemberById("member_id_here");
                            // guild.addRoleToMember(member, role).queue();
                            // guildChannelEvil.getMemberPermissionOverrides(Permission.MESSAGE_SEND)
                        }
                        else {
                            channel.sendMessage("Provide me with something valid").queue();
                        }
                    }
                    else {
                        channel.sendMessage("Please provide me with some arguments").queue();
                    }
                }
                else {
                    guild.createTextChannel(chStart + author.getId()).setTopic("This ticket was created by " + author.getName() + " with reason: " + msgContentRaw).queue(ticketch -> {
                        ticketch.upsertPermissionOverride(authorMember).grant(permsTicket).queue();
                        ticketch.upsertPermissionOverride(everyoneRole).deny(permsTicket).queue();
                    });
                }
            }
            else if (cmd.equals("slowmode")) {
                if(!message.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `MANAGE_CHANNEL` permission").queue();
                    return;
                }
                if(!contentPostoji) {
                    message.reply("Please provide me with slowmode you want set.").queue();
                    return;
                }
                try {
                    // int slowmodeKojiUserZeli = Math.Abs(Integer.valueOf(niz_reci[1]));
                    short slowmodeKojiUserZeli = Short.valueOf(niz_reci[1]); // ne postoji razlog zasto sam ga ogranicio na short
                    if(slowmodeKojiUserZeli < 0) slowmodeKojiUserZeli *= -1;
                    if(slowmodeKojiUserZeli > 21600) slowmodeKojiUserZeli = 21600; // hard limit za slowmode

                    guildChannelEvil.getManager().setSlowmode(slowmodeKojiUserZeli).queue();
                    channel.sendMessage(mentionUser + " set the slowmode to " + slowmodeKojiUserZeli + " seconds").queue();

                } catch (NumberFormatException e) {
                    // ako opet zaboravim da napisem .queue() mislim da ce se lose stvari desiti
                    channel.sendMessage("Input you provided is not a number!").queue();
                    return;
                }
            }
            else if (cmd.equals("slowmode?")) {
                // iskreno ne volim ovo Integer.toString() u javi ali sta ces
                // i == je broken pa moras da koristis .equals()
                //String getSlowChannel = Integer.toString(guildChannelEvil.getSlowmode());

                channel.sendMessage("The current slowmode in this channel is `" + guildChannelEvil.getSlowmode() + "` seconds.").queue();
                // e i da ako citas ovo volim te
                // volim te i ako ne citas ali razumes poentu
                // <3
            }
            else if (cmd.equals("suggest")) {
                if(!contentPostoji) {
                    message.reply("Please provide a valid suggestion").queue();
                }
                else
                {
                    // https://charbase.com/1f44d-unicode-thumbs-up-sign
                    message.addReaction(Emoji.fromUnicode("\ud83d\udc4d")).queue();
                    EmbedBuilder sugEmbedBuild = new EmbedBuilder();
                    sugEmbedBuild.setTitle(author.getName() + " suggests");
                    sugEmbedBuild.setDescription(msgContentRaw);
                    sugEmbedBuild.setColor(5724148);
                    sugEmbedBuild.setFooter(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(LocalDateTime.now()),authorMember.getUser().getAvatarUrl());
                    MessageEmbed sugEmbed = sugEmbedBuild.build();

                    guild.getTextChannelById(suggestchid).sendMessageEmbeds(sugEmbed).queue(suggestmsg -> {
                        suggestmsg.addReaction(Emoji.fromUnicode("\u2705")).queue();
                        suggestmsg.addReaction(Emoji.fromUnicode("\u274c")).queue();
                    });
                    sugEmbedBuild.clear();
                }
            }
            else if (cmd.equals("members")) {
                // problem: broji i botove, mogu da resim problem jednom FOR petljom ali bas mi se ne svidja to resenje
                channel.sendMessage("There are " + guild.getMemberCount() + " members in this server").queue();
            }
            else if (cmd.equals("lock")) {
                if(!message.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `MANAGE_CHANNEL` permission").queue();
                    return;
                }
                guildChannelEvil.upsertPermissionOverride(everyoneRole).deny(Permission.MESSAGE_SEND).queue();
            }
            else if (cmd.equals("unlock")) {
                if(!message.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
                    channel.sendMessage("You don't have right permissions to execute this command. You need `MANAGE_CHANNEL` permission").queue();
                    return;
                }
                guildChannelEvil.upsertPermissionOverride(everyoneRole).clear(Permission.MESSAGE_SEND).queue();
            }
            else if (cmd.equals("avatar")) {
                Member avatarMember;
                if(mentionedPeople.isEmpty()) {
                    avatarMember = authorMember;
                }
                else {
                    avatarMember = mentionedPeople.get(0);
                }
                EmbedBuilder avatarEmbed = new EmbedBuilder();
                avatarEmbed.setTitle(avatarMember.getUser().getName() + "'s avatar");
                // podesi velicinu avatara
                avatarEmbed.setImage(avatarMember.getUser().getAvatarUrl());
                // https://www.tabnine.com/code/java/methods/net.dv8tion.jda.core.entities.User/getEffectiveAvatarUrl
                avatarEmbed.setFooter("Requested by " + author.getName());
                avatarEmbed.setColor(Color.BLUE);
                channel.sendMessageEmbeds(avatarEmbed.build()).queue();
                avatarEmbed.clear();
            }
            else if (cmd.equals("embed")) {
                EmbedBuilder embedBuilder = new EmbedBuilder();

                // Set the title of the embed
                embedBuilder.setTitle("Example Embed");

                // Set the description of the embed
                embedBuilder.setDescription("This is a simple example of an embed message.");

                embedBuilder.addField("Fraza 1)", "Stuff", false);
                embedBuilder.addField("Fraza 2)", "Stuff", false);

                // Set the color of the embed
                embedBuilder.setColor(Color.GREEN);

                embedBuilder.setFooter("Bot created by person", guild.getOwner().getUser().getAvatarUrl());

                // Build the embed message
                MessageEmbed embed = embedBuilder.build();

                // Send the embed message to the channel
                channel.sendMessageEmbeds(embed).queue();
                //channel.sendMessage("message").setEmbeds(embedBuilder.build()).queue();
                embedBuilder.clear();

            }
            else if (cmd.equals("license")) {
                // nemam pojma da li je ovo vazno da uradim ali eto nek bude tu
                channel.sendMessage("https://github.com/Nikola1304/DiscordBotJava/blob/main/LICENSE").queue();
            }
            else if (cmd.equals("say")) {
                // moj ID, jer drugi nisu dostojni ove komande
                if(author.getId().equals("716962243400564786")) {
                    if(niz_reci.length > 1) {
                        message.delete().queue();
                        channel.sendMessage(msgContentRaw).queue();
                    }
                    else {
                        channel.sendMessage("Tell me what to say").queue();
                    }
                } else System.out.println(author.getName() + " pokusava da koristi ovu komandu");
            }
