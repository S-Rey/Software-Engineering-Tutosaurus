package ch.epfl.sweng.tutosaurus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.epfl.sweng.tutosaurus.model.User;

/**
 * Created by ubervison on 20.10.16.
 */

@RunWith(JUnit4.class)
public class UserTest {

    @Test(expected = IllegalArgumentException.class)
    public void ratingCanOnlyBeBetweenZeroAndOne() {
        User user = new User("000000");
        user.setGlobalRating(4.2);
        user.setGlobalRating(-0.8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void courseRatingCanOnlyBeBetweenZeroAndOne() {
        User user = new User("000000");
        user.setCourseRating("0", 4.2);
    }
}
