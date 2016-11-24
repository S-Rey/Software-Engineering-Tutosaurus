package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import com.google.firebase.database.Query;

import ch.epfl.sweng.tutosaurus.findTutor.FirebaseTutorAdapter;
import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.User;

public class FindTutorResult extends AppCompatActivity {

    private String tutorName;
    private String tutorSciper;
    private String courseId;

    DatabaseHelper dbh = DatabaseHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tutor_result);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String methodToCall = extras.getString("METHOD_TO_CALL");
        ListView tutorList = (ListView) findViewById(R.id.tutorList);
        Query ref = dbh.getUserRef();

        if (methodToCall.equals("findTutorByName")) {
            tutorName=extras.getString("NAME_TO_SEARCH");
            ref = ref.orderByChild("fullName").equalTo(tutorName);
        }
        else if (methodToCall.equals("findTutorBySciper")) {
            tutorSciper=extras.getString("SCIPER_TO_SEARCH");
            ref = ref.orderByChild("sciper").equalTo(tutorSciper);
        }
        else if (methodToCall.equals("findTutorByCourse")) {
            courseId=extras.getString("COURSE_ID");
            ref = findTutorBySubject(courseId, ref);
        }

        FirebaseTutorAdapter adapter = new FirebaseTutorAdapter(this, User.class, R.layout.listview_tutor_row, ref);
        tutorList.setAdapter(adapter);
    }

    private Query findTutorBySubject(String subject, Query userRef){
        Query resultTutorRef=userRef.orderByChild("teaching/" + subject).equalTo(true);
        return resultTutorRef;
    }

}

