package ch.epfl.sweng.tutosaurus;

import android.widget.ProgressBar;

/**
 * Created by albertochiappa on 17/10/16.
 */

public class ProfileMockup {
    private int sciperNumber;
    private String name;
    private String surname;
    private float studentRating;
    private float professorRating;

    public ProfileMockup(){

    }

    public ProfileMockup(int sciperNumber, String name, String surname, float studentRating, float professorRating){
        this.sciperNumber=sciperNumber;
        this.name=name;
        this.surname=surname;
        this.studentRating=studentRating;
        this.professorRating=professorRating;
    }
    public void setSciperNumber(int sciperNumber){
        this.sciperNumber=sciperNumber;
    }
    public int getSciperNumber(){
        return this.sciperNumber;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }
    public void setSurname(String surname){
        this.surname=surname;
    }
    public String getSurname(){
        return this.surname;
    }
    public void setStudentRating(float studentRating){
        this.studentRating=studentRating;
    }
    public float getStudentRating(){
        return this.studentRating;
    }
    public void setProfessorRating(float professorRating){
        this.professorRating=professorRating;
    }
    public float getProfessorRating(){
        return this.professorRating;
    }
}
