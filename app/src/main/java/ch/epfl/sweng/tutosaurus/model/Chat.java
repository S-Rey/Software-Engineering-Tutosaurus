package ch.epfl.sweng.tutosaurus.model;

/**
 * Created by ubervison on 11/5/16.
 */

public class Chat implements Identifiable {

    private String fullName;
    private String uid;

    public Chat() {

    }

    public Chat(String uid){
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}