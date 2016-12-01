package ch.epfl.sweng.tutosaurus;

import org.junit.Test;

import ch.epfl.sweng.tutosaurus.model.FullCourseList;
import static org.junit.Assert.*;
/**
 * Created by albertochiappa on 27/11/16.
 */

public final class FullCourseListTest {
    @Test
    public void isSingular() {
        FullCourseList firstList = FullCourseList.getInstance();
        FullCourseList secondList = FullCourseList.getInstance();
        assertEquals(firstList, secondList );
    }

}
