package ch.epfl.sweng.tutosaurus.tequila;

import org.junit.Test;

import ch.epfl.sweng.tutosaurus.Tequila.Profile;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Stephane on 11/17/2016.
 */


public class ProfileTest {
    final String firstName = "Donald Muriel";
    final String lastName = "Trump";
    final String gaspar = "Maga";
    final String email = "DonaldTrump@gov.org";
    final String sciper = "091116";
    final Profile profile = new Profile(sciper, gaspar, email, firstName, lastName);
    @Test
    public void printProfileCorrectly(){
        assertThat("Donald Muriel Trump\nsciper: 091116\ngaspar: Maga\nemail: DonaldTrump@gov.org", is(profile.toString()));
    }
}
