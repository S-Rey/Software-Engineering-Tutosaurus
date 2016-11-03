package ch.epfl.sweng.tutosaurus.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private String sciper;
    private String username;
    private String fullName;
    private String email;
    private String uid;
    private int profilePicture;

    private Map<String, Boolean> languages = new HashMap<>();

    private Map<String, Boolean> teaching = new HashMap<>();
    private Map<String, Boolean> studying = new HashMap<>();

    private Map<String, Double> ratings = new HashMap<>(); /* (course id -> globalRating) */
    private Map<String, Integer> totalHoursTaught = new HashMap<>(); /* (course id -> hours taught */

    private double globalRating;

    /**
     * Default constructor (for Firebase database)
     */
    public User(){

    }

    /**
     * Constructor for the User class.
     * @param sciper the sciper number of this user, used as an unique identifier
     */
    public User(String sciper) {
        this.sciper = sciper;
    }

    /**
     * Constructor for the User class.
     * @param sciper the sciper number of this user, used as an unique identifier
     * @param username the username of this user (normally a GASPAR username)
     */
    public User(String sciper, String username) {
        this.sciper = sciper;
        this.username = username;
    }

    /**
     * Set this user's username (for now, this will be their GASPAR username).
     * @param username the username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Set this user's full name.
     * @param fullName the full name of the user
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Sets this user's email address
     * @param email the email address of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets this user's email address
     * @param profilePicture the id of the profile picture
     */
    public void setPicture(int profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return this.uid;
    }

    /**
     * Sets this user's teacher globalRating, as a number between 0 and 1 included.
     * @param globalRating the user's globalRating between 0 and 1
     * @throws IllegalArgumentException if the globalRating is not comprised between 0 and 1
     */
    public void setGlobalRating(double globalRating){
        if(globalRating > 1.0 || globalRating < 0) {
            throw new IllegalArgumentException("The globalRating should be between 0 and 1");
        } else {
            this.globalRating = globalRating;
        }
    }

    /**
     * Returns this user's sciper number.
     * @return the sciper number of the user
     */
    public String getSciper() {
        return this.sciper;
    }

    /**
     * Returns this user's username (normally the GASPAR username).
     * @return the username of the user
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns this user's full fullName.
     * @return the full fullName of the user
     */
    public String getFullName() {
        return this.fullName;
    }

    /**
     * Returns this user's email address.
     * @return the email address of the user
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Returns this user's teacher globalRating.
     * @return the globalRating of the user
     */
    public double getGlobalRating() {
        return this.globalRating;
    }

     /**
     * Add a course to the list of courses that this user is prepared to teach.
     * @param courseId the course to add
     */
    public void addTeaching(String courseId) {
        teaching.put(courseId, true);
    }

    /**
     * Add a course to the list of courses that this user wants assistance with.
     * @param courseId the course to add.
     */
    public void addStudying(String courseId) {
        studying.put(courseId, true);
    }

    /**
     * Add a language to the list of this user's spoken languages.
     * @param language the language to add
     */
    public void addLanguage(String language) {
        this.languages.put(language, true);
    }

    /**
     * Returns a map containing this user's spoken languages, mapped to the boolean value true.
     * @return a map of this user's spoken languages
     */
    public Map<String, Boolean> getLanguages() {
        Map<String, Boolean> map = new HashMap<>();
        for(String l : languages.keySet()) {
            map.put(l, true);
        }
        return map;
    }

    /**
     * Returns a map containing the courses that this user has agreed to teach
     * @return a map of courses mapped to the true boolean value
     */
    public Map<String, Boolean> getTeaching() {
        Map<String, Boolean> ls = new HashMap<>();
        for(String c : teaching.keySet()) {
            ls.put(c, true);
        }
        return ls;
    }

    /**
     * Returns a map containing the courses that this user wants help with.
     * @return a map of courses mapped to the true boolean value
     */
    public Map<String, Boolean> getStudying() {
        Map<String, Boolean> ls = new HashMap<>();
        for(String c : studying.keySet()) {
            ls.put(c, true);
        }
        return ls;
    }

    /**
     * Increase the number of hours taught in a particular course.
     * @param courseId the unique id of the course
     * @param hours the number of hours by which to increase the number of hours taught
     */
    public void addHoursTaught(String courseId, int hours) {
        totalHoursTaught.put(courseId, totalHoursTaught.get(courseId) + hours);
    }

    /**
     * Set the globalRating for a particular course.
     * @param courseId the unique id of the §course
     * @param rating the globalRating for this course
     */
    public void setCourseRating(String courseId, double rating) {
        if(rating > 1.0 || rating < 0) {
            throw new IllegalArgumentException("The globalRating should be between 0 and 1");
        } else {
            ratings.put(courseId, rating);
        }
    }

    /**
     * Get the number of hours taught in a particular course
     * @param courseId the unique id of the course
     * @return the number of hours taught
     */
    public int getHoursTaught(int courseId) {
        return totalHoursTaught.get(courseId);
    }

    /**
     * Returns this user's email address.
     * @return the id of the profile picture
     */
    public int getPicture() {
        return this.profilePicture;
    }

    /**
     * Get the globalRating for a particular course
     * @param courseId the unique id of the course
     * @return the globalRating for this course
     */
    public double getCourseRating(int courseId) {
        return ratings.get(courseId);
    }


    /**
     * Returns the name automatically (to be used in the listview)
     * @return the full name
     */

    public String toString(){
        return this.fullName;
    }

    public boolean isTeacher(String courseId){
        for(String course : teaching.keySet()){
            if(course.equals(courseId)){
                return true;
            }
        }
        return false;
    }

}
