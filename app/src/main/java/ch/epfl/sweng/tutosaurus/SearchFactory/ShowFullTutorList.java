package ch.epfl.sweng.tutosaurus.SearchFactory;

import com.google.firebase.database.Query;

import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;

/**
 * Created by albertochiappa on 09/12/16.
 */

public class ShowFullTutorList implements SearchCriterion {

    private DatabaseHelper dbh;

    @Override
    public Query performSearch(String extraInfo) {
        dbh = DatabaseHelper.getInstance();
        Query ref = dbh.getUserRef();
        ref = ref.orderByChild("fullName");
        return ref;
    }
}
