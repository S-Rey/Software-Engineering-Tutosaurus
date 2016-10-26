package ch.epfl.sweng.tutosaurus.model;

import android.location.Location;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class Meeting {

    private final int id;
    private Date date;
    private int duration;
    private Location location;
    private ArrayList<Integer> participants = new ArrayList<>();

    /**
     * Constructor for the Meeting class
     * @param id the unique id of this meeting
     */
    public Meeting(int id) {
        this.id = id;
    }

    /**
     * Constructor for the Meeting class
     * @param id the unique id of this meeting
     * @param date the date at which the meeting takes place
     */
    public Meeting(int id, Date date) {
        this.id = id;
        this.date = date;
    }

    /**
     * Constructor for the Meeting class
     * @param id the unique id of this meeting
     * @param date the date at which the meeting takes place
     * @param duration the duration of the meeting (hours)
     */
    public Meeting(int id, Date date, int duration) {
        this.id = id;
        this.date = date;
        this.duration = duration;
    }

    /**
     * Sets the date at which this meeting takes place.
     * @param date the date of this meeting
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Sets the date at which this meeting takes place.
     * @param duration the duration of this meeting
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Sets the location were this meeting takes place.
     * @param location the location of the meeting
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Add a participant to this meeting.
     * @param sciper the sciper number of the participant
     */
    public void addParticipant(int sciper) {
        this.participants.add(sciper);
    }

    /**
     * Returns the unique id of this meeting.
     * @return the unique id for the meeting
     */
    public int getId() {
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
     * Returns the duration of this meeting.
     * @return the duration of the meeting
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * Returns the location where this meeting takes place.
     * @return the location of the meeting
     */
    public Location getLocation() {
        return this.location;
    }

}
