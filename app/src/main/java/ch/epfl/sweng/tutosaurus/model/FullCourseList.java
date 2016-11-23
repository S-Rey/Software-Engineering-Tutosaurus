package ch.epfl.sweng.tutosaurus.model;

import java.util.ArrayList;
import java.util.Iterator;

import ch.epfl.sweng.tutosaurus.R;

/**
 * Created by albertochiappa on 23/11/16.
 */
public class FullCourseList {
    private static FullCourseList ourInstance = new FullCourseList();
    private static ArrayList<Course> listOfCourses;

    public static FullCourseList getInstance() {
        return ourInstance;
    }

    private FullCourseList() {
        listOfCourses.add(new Course("Maths", "Mathematics", R.drawable.school));
        listOfCourses.add(new Course("Physics", "Physics", R.drawable.molecule));
        listOfCourses.add(new Course("Chemistry", "Chemistry", R.drawable.flask));
        listOfCourses.add(new Course("Computer", "Computer Science", R.drawable.computer));
        listOfCourses.add(new Course("Quantum", "Quantum Mechanics", R.drawable.music));
        listOfCourses.add(new Course("Dynamics", "Dynamics", R.drawable.shapes));
    }

    public ArrayList<Course> getListOfCourses(){
        return listOfCourses;
    }
}
