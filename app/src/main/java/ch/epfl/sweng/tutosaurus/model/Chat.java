package ch.epfl.sweng.tutosaurus.model;

/**
 * Created by ubervison on 11/5/16.
 */

public class Chat implements Identifiable {

    private String otherUserUid;
    private String otherFullName;

    public Chat(){

    }

    public String getOtherUserUid() {
        return otherUserUid;
    }

    public Chat(String otherUserUid) {
        this.otherUserUid = otherUserUid;
    }

    public Chat(String otherUserUid, String otherFullName) {
        this.otherFullName = otherFullName;
        this.otherUserUid = otherUserUid;
    }

    public void setOtherUserUid(String otherUserUid) {
        this.otherUserUid = otherUserUid;
    }

    public String getOtherFullName() {
        return otherFullName;
    }

    public void setOtherFullName(String otherFullName) {
        this.otherFullName = otherFullName;
    }

    public String getUid() {
        return this.otherUserUid;
    }
}
