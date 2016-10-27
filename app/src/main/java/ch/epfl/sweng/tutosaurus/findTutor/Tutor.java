package ch.epfl.sweng.tutosaurus.findTutor;

/**
 * Created by albertochiappa on 21/10/16.
 */

public class Tutor {
    public int profilePicture;
    public String profileName;
    public int sciperNumber;

    public Tutor(){
        super();
    }
    public Tutor(int profilePicture, String profileName, int sciperNumber){
        super();
        this.profilePicture=profilePicture;
        this.profileName=profileName;
        this.sciperNumber=sciperNumber;
    }
}
