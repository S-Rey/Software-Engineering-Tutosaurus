package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.Query;

import java.util.ArrayList;

import ch.epfl.sweng.tutosaurus.findTutor.FirebaseTutorAdapter;
import ch.epfl.sweng.tutosaurus.findTutor.TutorAdapter;
import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.Tutor;
import ch.epfl.sweng.tutosaurus.model.User;

public class FindTutorResult extends AppCompatActivity {

    private String tutorName;
    DatabaseHelper dbh = DatabaseHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tutor_result);

        User[] profiles = createProfiles();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String methodToCall = extras.getString("METHOD_TO_CALL");


        if (methodToCall.equals("findTutorByName")) {
            tutorName=extras.getString("NAME_TO_SEARCH");

            ListView tutorList = (ListView) findViewById(R.id.tutorList);
            Query ref = dbh.getUserRef();
            ref = ref.orderByChild("fullName");
            ref=ref.equalTo(tutorName);
            FirebaseTutorAdapter adapter = new FirebaseTutorAdapter(this, User.class, R.layout.listview_tutor_row, ref);
            tutorList.setAdapter(adapter);
            //fillListView(findTutorByName(extras.getString("NAME_TO_SEARCH"), profiles));
        }
        else if (methodToCall.equals("findTutorBySciper")) {
            fillListView(findTutorBySciper(extras.getString("SCIPER_TO_SEARCH"), profiles));
        }
        else if (methodToCall.equals("findMathsTutor")) {
            fillListView(findTutorBySubject("Maths", profiles));
        }
        else if (methodToCall.equals("findPhysicsTutor")) {
            fillListView(findTutorBySubject("Physics", profiles));
        }
        else if (methodToCall.equals("findChemistryTutor")) {
            fillListView(findTutorBySubject("Chemistry", profiles));
        }
        else if (methodToCall.equals("findComputerTutor")) {
            fillListView(findTutorBySubject("Computer", profiles));
        }
        else if (methodToCall.equals("showFullList")){
            fillListView(showFullList(profiles));
        }
        /*
        // Create example database
        MockupDatabaseHandler dbHandler = new MockupDatabaseHandler(this);
        //ProfileMockup alberto=new ProfileMockup(273516,"Alberto Silvio","Chiappa",3.5f,4.0f);
        //dbHandler.addProfile(alberto);
        //fillDatabase(dbHandler);

        // Writes the name of id 273516
        TextView name=(TextView) findViewById(R.id.name);
        Toast toast=Toast.makeText(this, "Looking for the profile", Toast.LENGTH_SHORT);
        toast.show();
        //String nameToDisplay=dbHandler.getProfile(273516).getName();
        //name.setText(dbHandler.getProfile(273516).getName());
        //name1.setText("Alberto");
        */

    }

    public User[] createProfiles() {

        Course maths= new Course("0","Maths");
        Course physics= new Course("1","Physics");
        Course chemistry= new Course("2","Chemistry");
        Course computer= new Course("3","Computer Science");

        User profileOne = new User("273516");
        profileOne.setFullName("Alberto Chiappa");
        profileOne.setEmail("alberto.chiappa@epfl.ch");
        profileOne.addTeaching(maths.getId());
        profileOne.addTeaching(computer.getId());
        profileOne.setPicture(R.drawable.foto_mia);

        User profileTwo = new User("223415");
        profileTwo.setFullName("Albert Einstein");
        profileTwo.setEmail("albert.einstein@epfl.ch");
        profileTwo.addTeaching(physics.getId());
        profileTwo.addTeaching(maths.getId());
        profileTwo.setPicture(R.drawable.einstein);

        User profileThree = new User("124821");
        profileThree.setFullName("Kurt Godel");
        profileThree.setEmail("kurt.godel@epfl.ch");
        profileThree.addTeaching(maths.getId());
        profileThree.setPicture(R.drawable.godel);

        User profileFour = new User("100000");
        profileFour.setFullName("Maurizio Grasselli");
        profileFour.setEmail("maurizio.grasselli@epfl.ch");
        profileFour.addTeaching(maths.getId());
        profileFour.setPicture(R.drawable.grasselli);

        User profileFive = new User("223615");
        profileFive.setFullName("Linus Torvalds");
        profileFive.setEmail("linus.torval@epfl.ch");
        profileFive.addTeaching(computer.getId());
        profileFive.setPicture(R.drawable.torvalds);

        User profileSix = new User("443213");
        profileSix.setFullName("Carlo Rubbia");
        profileSix.setEmail("carlo.rubbia@epfl.ch");
        profileSix.addTeaching(chemistry.getId());
        profileSix.addTeaching(physics.getId());
        profileSix.setPicture(R.drawable.rubbia);


        return new User[]{profileOne, profileTwo, profileThree, profileFour, profileFive,profileSix};
    }

    // TODO: make a search algorithm that is more flexible
    private ArrayList<Tutor> findTutorByName(String name, User[] profiles) {
        int count = 0;
        Tutor tutorToAdd;
        ArrayList<Tutor> teachers = new ArrayList<>(0);
        for (User profile : profiles) {
            if (profile.getFullName().equals(name)) {
                tutorToAdd=new Tutor(profile.getPicture(),profile.getFullName(),profile.getSciper());
                teachers.add(tutorToAdd);
                count++;
            }
        }

        for (User profile : profiles) {
            if (profile.getFullName().toLowerCase().contains(name.toLowerCase())) {
                tutorToAdd=new Tutor(profile.getPicture(),profile.getFullName(),profile.getSciper());
                teachers.add(tutorToAdd);
                count++;
            }
        }

        if (count == 0) {
            TextView message=(TextView) findViewById(R.id.message);
            message.setText("The research produced no results");
            message.setVisibility(View.VISIBLE);
        }
        return teachers;
    }

    private ArrayList<Tutor> findTutorBySciper(String sciper, User[] profiles) {
        int count = 0;
        int sciperNumber=Integer.parseInt(sciper);
        Tutor tutorToAdd;
        ArrayList<Tutor> teachers = new ArrayList<>(0);
        for (User profile : profiles) {
            if (profile.getSciper().equals(sciperNumber)) {
                tutorToAdd=new Tutor(profile.getPicture(),profile.getFullName(), profile.getSciper());
                teachers.add(tutorToAdd);
                count++;
            }
        }
        if (count == 0) {
            TextView message=(TextView) findViewById(R.id.message);
            message.setText("The research produced no results");
            message.setVisibility(View.VISIBLE);
        }
        return teachers;
    }

    private ArrayList<Tutor> findTutorBySubject(String subject, User[] profiles) {
        String id=convertNameToId(subject);
        int count = 0;
        Tutor tutorToAdd;
        ArrayList<Tutor> teachers = new ArrayList<>(0);
        for (User profile : profiles) {
            for (String taughtCourseId : profile.getTeaching().keySet()) {
                if (taughtCourseId.equals(id)) {
                    tutorToAdd=new Tutor(profile.getPicture(),profile.getFullName(), profile.getSciper());
                    teachers.add(tutorToAdd);
                    count++;
                }

            }
        }
        if (count == 0) {
            TextView message=(TextView) findViewById(R.id.message);
            message.setText("The research produced no results");
            message.setVisibility(View.VISIBLE);
        }

        return teachers;
    }

    private ArrayList<Tutor> showFullList(User[] profiles) {
        int count = 0;
        Tutor tutorToAdd;
        ArrayList<Tutor> teachers = new ArrayList<>(0);
        for (User profile : profiles) {
            tutorToAdd=new Tutor(profile.getPicture(),profile.getFullName(),profile.getSciper());
            teachers.add(tutorToAdd);
            count++;
        }

        if (count == 0) {
            TextView message=(TextView) findViewById(R.id.message);
            message.setText("The research produced no results");
            message.setVisibility(View.VISIBLE);
        }
        return teachers;
    }
    private void fillListView(ArrayList<Tutor> tutorNames){
        TutorAdapter arrayAdapter = new TutorAdapter(
                this,
                R.layout.listview_tutor_row,
                tutorNames);
        ListView tutorList=(ListView) findViewById(R.id.tutorList);
        tutorList.setAdapter(arrayAdapter);
    }
    private String convertNameToId(String subject){
        String id = "-1";

        if (subject.equals("Maths")) {
            id = "0";
        }
        else if(subject.equals("Physics")){
            id="1";
        }
        else if(subject.equals("Chemistry")){
            id="2";
        }
        else if(subject.equals("Computer Science")||subject.equals("Computer")){
            id="3";
        }
        return id;
    }
}

