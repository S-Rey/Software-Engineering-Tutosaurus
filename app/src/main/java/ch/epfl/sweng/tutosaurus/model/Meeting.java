package ch.epfl.sweng.tutosaurus.model;

import android.location.Location;
import java.util.Date;

public class Meeting {

    private final int id;
    private Date date;
    private Location location;

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
     * Sets the date at which this meeting takes place.
     * @param date the date of this meeting
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Sets the location were this meeting takes place.
     * @param location the location of the meeting
     */
    public void setLocation(Location location) {
        this.location = location;
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
     * Returns the location where this meeting takes place.
     * @return the location of the meeting
     */
    public Location getLocation() {
        return this.location;
    }
}
