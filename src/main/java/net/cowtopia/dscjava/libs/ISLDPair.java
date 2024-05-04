package net.cowtopia.dscjava.libs;

public class ISLDPair
{
    private int index;
    private String str;
    private long lng;
    private long time;

    public ISLDPair(int index, String str, long lng, long time) {
        this.index = index;
        this.str = str;
        this.lng = lng;
        this.time = time;
    }

    public ISLDPair() {
        this.index = -1;
        this.str = "trashvalue";
        this.lng = 69420L;
        this.time = 1000202400L;
    }

    public ISLDPair(ISLDPair p) {
        this.index = p.index;
        this.str = p.str;
        this.lng = p.lng;
        this.time = p.time;
    }

    public int getIndex() {
        return index;
    }

    public String getStr() {
        return str;
    }

    public long getLong() {
        return lng;
    }

    public long getTime() {
        return time;
    }
}
