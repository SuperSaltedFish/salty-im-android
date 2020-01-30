package me.zhixingye.im.bean;

/**
 * Created by zhixingye on 2020年01月29日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ContactTag {
    private String tag;
    private int membersCount;

    public ContactTag() {
    }

    public ContactTag(String tag, int membersCount) {
        this.tag = tag;
        this.membersCount = membersCount;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }
}
