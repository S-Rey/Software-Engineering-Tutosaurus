package ch.epfl.sweng.tutosaurus.model;

public class Course {

    private String name;
    private String id;

    /**
     * Constructor with no arguments for Course (required for firebase deserialization)
     */
    public Course() {

    }

    /**
     * Constructor for the Course class.
     * @param id the unique id of this course
     */
    public Course(String id) {
        this.id = id;
    }

    /**
     * Constructor for the Course class.
     * @param id the unique id of this course
     * @param name the name of this course
     */
    public Course(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Sets this course's name.
     *
     * @param name the name of the course
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns this course's unique id.
     * @return the unique id for this course
     */
    public String getId(){
        return this.id;
    }

    /**
     * Returns this course's name.
     *
     * @return the name of the course
     */
    public String getName() {
        return this.name;
    }

}
