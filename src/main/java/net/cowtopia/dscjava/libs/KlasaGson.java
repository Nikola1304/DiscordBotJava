package net.cowtopia.dscjava.libs;

public class KlasaGson
{
    private String bot_token;
    private long server_id;
    private String db_name;
    private long welcome_ch_id;
    private long leave_ch_id;
    private long members_vc_id;
    private long staff_ch_id;
    private long suggest_ch_id;
    private long tickets_cat;

    public KlasaGson(String bot_token, long server_id, String db_name, long welcome_ch_id, long leave_ch_id, long members_vc_id, long staff_ch_id, long suggest_ch_id, long tickets_cat) {
        this.bot_token = bot_token;
        this.server_id = server_id;
        this.db_name = db_name;
        this.welcome_ch_id = welcome_ch_id;
        this.leave_ch_id = leave_ch_id;
        this.members_vc_id = members_vc_id;
        this.staff_ch_id = staff_ch_id;
        this.suggest_ch_id = suggest_ch_id;
        this.tickets_cat = tickets_cat;
    }

    public KlasaGson(KlasaGson g) {
        this.bot_token = g.bot_token;
        this.server_id = g.server_id;
        this.db_name = g.db_name;
        this.welcome_ch_id = g.welcome_ch_id;
        this.leave_ch_id = g.leave_ch_id;
        this.members_vc_id = g.members_vc_id;
        this.staff_ch_id = g.staff_ch_id;
        this.suggest_ch_id = g.suggest_ch_id;
        this.tickets_cat = g.tickets_cat;
    }

    public String getToken() {
        return bot_token;
    }

    public long getServerId() {
        return server_id;
    }

    public String getDBName() {
        return db_name;
    }

    public long getWelcomeId() {
        return welcome_ch_id;
    }

    public long getLeaveId() {
        return leave_ch_id;
    }

    public long getMembersId() {
        return members_vc_id;
    }

    public long getStaffId() {
        return staff_ch_id;
    }

    public long getSuggestId() {
        return suggest_ch_id;
    }

    public long getTicketsCat() {
        return tickets_cat;
    }
}
