package ch.epfl.sweng.tutosaurus.model;

/**
 * Created by albertochiappa on 21/10/16.
 */

public class Tutor {
    public int profilePicture;
    public String profileName;
    public String sciperNumber;

    public Tutor(){
        super();
    }
    public Tutor(int profilePicture, String profileName, String sciperNumber){
        super();
        this.profilePicture=profilePicture;
        this.profileName=profileName;
        this.sciperNumber=sciperNumber;
    }
}
