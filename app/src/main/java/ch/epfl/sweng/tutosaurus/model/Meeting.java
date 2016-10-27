package ch.epfl.sweng.tutosaurus.model;

import android.location.Location;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Meeting {

    private  String id;
    private Date date;
    private String location;
    private int duration;
    private ArrayList<String> participants = new ArrayList<>();


    /**
     * Empty constructor for Meeting (required for firebase deserialization)
     */
    public Meeting () {

    }

    /**
     * Constructor for the Meeting class
     * @param date the date at which the meeting takes place
     */
    public Meeting(Date date) {
        this.date = date;
    }

    /**
     * Constructor for the Meeting class
     * @param date the date at which the meeting takes place
     * @param duration the duration of this meeting (in minutes)
     */
    public Meeting(Date date, int duration) {
        this.date = date;
        this.duration = duration;
    }

    /**
     * Sets this meeting's unique id.
     * @param id the unique id of this meeting
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Sets the date at which this meeting takes place.
     * @param date the date of this meeting
     */
    public void setDateD(Date date) {
        this.date = date;
    }

    /**
     * Sets the date at which this meeting takes place.
     * @param date the date of this meeting
     */
    public void setDate(long date) {
        this.date = new Date(date);
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    /**
     * Sets the location were this meeting takes place.
     * @param location the location of the meeting
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Add a participant to this meeting.
     * @param sciper the sciper number of the participant
     */
    public void addParticipant(String sciper) {
        this.participants.add(sciper);
    }


    public List<String> getParticipants(){
        return this.participants;
    }

    /**
     * Returns the unique id of this meeting.
     * @return the unique id for the meeting
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns the date at which this meeting takes place.
     * @return the date of the meeting
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Returns the location where this meeting takes place.
     * @return the location of the meeting
     */
    public String getLocation() {
        return this.location;
    }
}
