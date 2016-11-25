package ch.epfl.sweng.tutosaurus.model;

import java.util.ArrayList;

import ch.epfl.sweng.tutosaurus.R;

/**
 * Created by albertochiappa on 23/11/16.
 */
public class FullCourseList {
    private static FullCourseList ourInstance = new FullCourseList();
    private ArrayList<Course> listOfCourses;

    public static FullCourseList getInstance() {
        return ourInstance;
    }

    private FullCourseList() {
        ArrayList<Course> listOfCourses = new ArrayList<>(0);
        listOfCourses.add(new Course("mathematics", "Mathematics", R.drawable.school));
        listOfCourses.add(new Course("physics", "Physics", R.drawable.molecule));
        listOfCourses.add(new Course("chemistry", "Chemistry", R.drawable.flask));
        listOfCourses.add(new Course("computer_science", "Computer Science", R.drawable.computer));
        //listOfCourses.add(new Course("quantum_mechanics", "Quantum Mechanics", R.drawable.music));
        //listOfCourses.add(new Course("dynamics", "Dynamics", R.drawable.shapes));
        this.listOfCourses = listOfCourses;
    }

    public ArrayList<Course> getListOfCourses(){
        return listOfCourses;
    }
}
