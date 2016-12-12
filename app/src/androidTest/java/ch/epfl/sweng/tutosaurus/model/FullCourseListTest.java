package ch.epfl.sweng.tutosaurus.model;

import org.junit.Test;

import ch.epfl.sweng.tutosaurus.model.FullCourseList;
import static org.junit.Assert.*;

public final class FullCourseListTest {

    @Test
    public void isSingular() {
        FullCourseList firstList = FullCourseList.getInstance();
        FullCourseList secondList = FullCourseList.getInstance();
        assertEquals(firstList, secondList );
    }

    @Test
    public void returnsCorrectCourse(){
        FullCourseList courseList = FullCourseList.getInstance();
        Course returnedCourse = courseList.getCourse("mathematics");
        assertEquals("Mathematics", returnedCourse.getName());
    }

}
