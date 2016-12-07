package ch.epfl.sweng.tutosaurus.Tequila;

/**
 * Created by Stephane on 10/24/2016.
 */

/**
 * Tequila user profile information.
 *
 * @author Solal Pirelli
 */
public final class Profile {
    /**
     * This is the user ID, it is guaranteed to be unique.
     */
    public final String sciper;
    /**
     * This is probably unique, but you shouldn't depend on it.
     */
    public final String gaspar;
    /**
     * Don't spam your users! Use this carefully.
     */
    public final String email;
    /**
     * Do not assume anything about what exactly this contains.
     * Some people have one name, some have multiple, some have honorary prefixes, ...
     */
    public final String firstNames;
    /**
     * Same remark as `firstNames`.
     */
    public final String lastNames;

    /**
     * Create User Profile. Profiles are always fetched by Tequila and thus assumed to be automatically correct.
     * @param sciper the sciper associated with this profile
     * @param gaspar the gaspar associated with this profile
     * @param email the email associated with this profile
     * @param firstNames the first names of this profile's user
     * @param lastNames the last names of this profile's user
     */
    public Profile(String sciper, String gaspar, String email, String firstNames, String lastNames) {
        this.sciper = sciper;
        this.gaspar = gaspar;
        this.email = email;
        this.firstNames = firstNames;
        this.lastNames = lastNames;
    }

    @Override
    public String toString() {
        return firstNames + " " + lastNames
                + "\nsciper: " + sciper
                + "\ngaspar: " + gaspar
                + "\nemail: " + email;
    }
}