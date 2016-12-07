package ch.epfl.sweng.tutosaurus.tequila;

import org.junit.Test;

import ch.epfl.sweng.tutosaurus.Tequila.Profile;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProfileTest {
    private final String firstName = "Donald Muriel";
    private final String lastName = "Trump";
    private final String gaspar = "Maga";
    private final String email = "DonaldTrump@gov.org";
    private final String sciper = "091116";
    private final Profile profile = new Profile(sciper, gaspar, email, firstName, lastName);
    @Test
    public void printProfileCorrectly(){
        assertThat("Donald Muriel Trump\nsciper: 091116\ngaspar: Maga\nemail: DonaldTrump@gov.org", is(profile.toString()));
    }
}
