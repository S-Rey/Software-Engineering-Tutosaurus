package ch.epfl.sweng.tutosaurus.model;

import org.junit.Test;

import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.FullCourseList;

import static org.junit.Assert.assertEquals;

/**
 * Created by albertochiappa on 27/11/16.
 */

public class CourseTest {
    @Test
    public void testConstructors() {
        Course firstCourse = new Course("mathematics");
        firstCourse.setName("Mathematics");
        firstCourse.setDescription("I'm good at maths");
        assertEquals(firstCourse.getId(), "mathematics");
        assertEquals(firstCourse.getName(), "Mathematics");
        assertEquals(firstCourse.getDescription(), "I'm good at maths");

        Course secondCourse = new Course("physics", "Physics");
        secondCourse.setDescription("I'm good at physics");
        assertEquals(secondCourse.getId(), "physics");
        assertEquals(secondCourse.getName(), "Physics");
        assertEquals(secondCourse.getDescription(), "I'm good at physics");

        Course thirdCourse = new Course("chemistry", "Chemistry", R.drawable.flask);
        thirdCourse.setDescription("I'm good at chemistry");
        assertEquals(thirdCourse.getId(), "chemistry");
        assertEquals(thirdCourse.getName(), "Chemistry");
        assertEquals(thirdCourse.getDescription(), "I'm good at chemistry");
        assertEquals(thirdCourse.getPictureId(), R.drawable.flask);
    }
}