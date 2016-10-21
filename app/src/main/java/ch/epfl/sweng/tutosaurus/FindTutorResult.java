package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ch.epfl.sweng.tutosaurus.findTutor.Tutor;
import ch.epfl.sweng.tutosaurus.findTutor.TutorAdapter;
import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.User;

public class FindTutorResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tutor_result);

        User[] profiles = createProfiles();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String methodToCall = extras.getString("METHOD_TO_CALL");


        if (methodToCall.equals("findTutorByName")) {
            fillListView(findTutorByName(extras.getString("NAME_TO_SEARCH"), profiles));
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

    private User[] createProfiles() {

        Course maths= new Course(0,"Maths");
        Course physics= new Course(1,"Physics");
        Course chemistry= new Course(2,"Chemistry");
        Course computer= new Course(3,"Computer Science");

        User profileOne = new User(273516);
        profileOne.setFullName("Alberto Chiappa");
        profileOne.setEmail("alberto.chiappa@epfl.ch");
        profileOne.addTeachingCourse(maths);
        profileOne.addTeachingCourse(computer);
        profileOne.setPicture(R.drawable.foto_mia);

        User profileTwo = new User(223415);
        profileTwo.setFullName("Albert Einstein");
        profileTwo.setEmail("albert.einstein@epfl.ch");
        profileTwo.addTeachingCourse(physics);
        profileTwo.addTeachingCourse(maths);
        profileTwo.setPicture(R.drawable.einstein);

        User profileThree = new User(124821);
        profileThree.setFullName("Kurt Godel");
        profileThree.setEmail("kurt.godel@epfl.ch");
        profileThree.addTeachingCourse(maths);
        profileThree.setPicture(R.drawable.godel);

        User profileFour = new User(100000);
        profileFour.setFullName("Maurizio Grasselli");
        profileFour.setEmail("maurizio.grasselli@epfl.ch");
        profileFour.addTeachingCourse(maths);
        profileFour.setPicture(R.drawable.grasselli);

        User profileFive = new User(223615);
        profileFive.setFullName("Linus Torvald");
        profileFive.setEmail("linus.torval@epfl.ch");
        profileFive.addTeachingCourse(computer);
        profileFive.setPicture(R.drawable.torvald);

        User profileSix = new User(443213);
        profileSix.setFullName("Carlo Rubbia");
        profileSix.setEmail("carlo.rubbia@epfl.ch");
        profileSix.addTeachingCourse(chemistry);
        profileSix.addTeachingCourse(physics);
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
                tutorToAdd=new Tutor(profile.getPicture(),profile.getFullName());
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
            if (profile.getSciper()==sciperNumber) {
                tutorToAdd=new Tutor(profile.getPicture(),profile.getFullName());
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
        int id=convertNameToId(subject);
        int count = 0;
        Tutor tutorToAdd;
        ArrayList<Tutor> teachers = new ArrayList<>(0);
        for (User profile : profiles) {
            for (Course taughtCourse : profile.getTeachingCourses()) {
                if (taughtCourse.getId() == id) {
                    tutorToAdd=new Tutor(profile.getPicture(),profile.getFullName());
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
    private void fillListView(ArrayList<Tutor> tutorNames){
        TutorAdapter arrayAdapter = new TutorAdapter(
                this,
                R.layout.listview_tutor_row,
                tutorNames);
        ListView tutorList=(ListView) findViewById(R.id.tutorList);
        tutorList.setAdapter(arrayAdapter);
    }
    private int convertNameToId(String subject){
        int id = -1;

        if (subject.equals("Maths")) {
            id = 0;
        }
        else if(subject.equals("Physics")){
            id=1;
        }
        else if(subject.equals("Chemistry")){
            id=2;
        }
        else if(subject.equals("Computer Science")||subject.equals("Computer")){
            id=3;
        }
        return id;
    }
}

