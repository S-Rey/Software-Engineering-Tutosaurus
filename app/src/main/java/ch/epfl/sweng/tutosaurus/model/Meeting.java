package ch.epfl.sweng.tutosaurus.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Meeting {

    private  String id;
    private Date date;
    private String nameLocation;
    private double latitudeLocation;
    private double longitudeLocation;
    private int duration;
    private String description;
    private List<String> participants = new ArrayList<>();
    private Course course;


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
     * Constructor for the Meeting class
     * @param date the date at which the meeting takes place
     * @param duration the duration of this meeting (in minutes)
     * @param course the subject of this meeting
     */
    public Meeting(Date date, int duration, Course course) {
        this.date = date;
        this.duration = duration;
        this.course = course;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Sets this meeting's unique id.
     * @param id the unique id of this meeting
     */
    public void setId(String id){
        this.id = id;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return this.course;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    /**
     * Sets the nameLocation were this meeting takes place.
     * @param nameLocation the nameLocation of the meeting
     */
    public void setNameLocation(String nameLocation) {
        this.nameLocation = nameLocation;
    }

    /**
     * Add a participant to this meeting.
     * @param key the Firebase key of the participant
     */
    public void addParticipant(String key) {
        this.participants.add(key);
    }

    public void addDescription(String description) { this.description = description; }


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
     * Returns the nameLocation where this meeting takes place.
     * @return the nameLocation of the meeting
     */
    public String getNameLocation() {
        return this.nameLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitudeLocation() {
        return latitudeLocation;
    }

    public void setLatitudeLocation(double latitudeLocation) {
        this.latitudeLocation = latitudeLocation;
    }

    public double getLongitudeLocation() {
        return longitudeLocation;
    }

    public void setLongitudeLocation(double longitudeLocation) {
        this.longitudeLocation = longitudeLocation;
    }

}
