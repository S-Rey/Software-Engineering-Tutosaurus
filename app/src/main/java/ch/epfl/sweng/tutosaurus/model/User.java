package ch.epfl.sweng.tutosaurus.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    private final int sciper;
    private String username;
    private String fullName;
    private String email;
    private ArrayList<Meeting> meetings = new ArrayList<>();

    private ArrayList<Course> teaching = new ArrayList<>();
    private ArrayList<Course> studying = new ArrayList<>();

    private Map<Integer, Double> ratings = new HashMap<>(); /* (course id -> rating) */
    private Map<Integer, Integer> totalHoursTaught = new HashMap<>(); /* (course id -> hours taught */

    private double rating;

    /**
     * Constructor for the User class.
     * @param sciper the sciper number of this user, used as an unique identifier
     */
    public User(int sciper) {
        this.sciper = sciper;
    }

    /**
     * Constructor for the User class.
     * @param sciper the sciper number of this user, used as an unique identifier
     * @param username the username of this user (normally a GASPAR username)
     */
    public User(int sciper, String username) {
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
     * Sets this user's teacher rating, as a number between 0 and 1 included.
     * @param rating the user's rating between 0 and 1
     * @throws IllegalArgumentException if the rating is not comprised between 0 and 1
     */
    public void setRating(double rating){
        if(rating > 1.0 || rating < 0) {
            throw new IllegalArgumentException("The rating should be between 0 and 1");
        } else {
            this.rating = rating;
        }
    }

    /**
     * Returns this user's sciper number.
     * @return the sciper number of the user
     */
    public int getSciper() {
        return this.sciper;
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
     * Returns this user's teacher rating.
     * @return the rating of the user
     */
    public double getRating() {
        return this.rating;
    }

    /**
     * Add a meeting to this user's meeting list.
     * @param meeting the meeting to add to this user
     */
    public void addMeeting(Meeting meeting) {
        meetings.add(meeting);
    }

    /**
     * Add a course to the list of courses that this user is prepared to teach.
     * @param course the course to add
     */
    public void addTeachingCourse(Course course) {
        teaching.add(course);
    }

    /**
     * Add a course to the list of courses that this user wants assistance with.
     * @param course the course to add.
     */
    public void addStudyingCourse(Course course) {
        studying.add(course);
    }

    /**
     * Returns a list containing all of the user's planned meetings.
     * @return a list of meetings
     */
    public ArrayList<Meeting> getMeetings() {
        ArrayList<Meeting> ls = new ArrayList<>();
        for(Meeting m : this.meetings) {
            ls.add(m);
        }
        return ls;
    }

    /**
     * Returns a list containing the courses that this user has agreed to teach
     * @return a list of courses
     */
    public ArrayList<Course> getTeachingCourses() {
        ArrayList<Course> ls = new ArrayList<>();
        for(Course c : teaching) {
            ls.add(c);
        }
        return ls;
    }

    /**
     * Returns a list containing te courses that this user wants help with.
     * @return a list of courses
     */
    public ArrayList<Course> getStudyingCourses() {
        ArrayList<Course> ls = new ArrayList<>();
        for(Course c : studying) {
            ls.add(c);
        }
        return ls;
    }

    /**
     * Increase the number of hours taught in a particular course.
     * @param courseId the unique id of the course
     * @param hours the number of hours by which to increase the number of hours taught
     */
    public void addHoursTaught(int courseId, int hours) {
        totalHoursTaught.put(courseId, totalHoursTaught.get(courseId) + hours);
    }

    /**
     * Set the rating for a particular course.
     * @param courseId the unique id of the course
     * @param rating the rating for this course
     */
    public void setCourseRating(int courseId, double rating) {
        ratings.put(courseId, rating);
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
     * Get the rating for a particular course
     * @param courseId the unique id of the course
     * @return the rating for this course
     */
    public double getCourseRating(int courseId) {
        return ratings.get(courseId);
    }

}
