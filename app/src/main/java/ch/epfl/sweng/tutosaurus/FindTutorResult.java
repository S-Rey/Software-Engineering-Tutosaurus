package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sweng.tutosaurus.model.User;

public class FindTutorResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tutor_result);

        User[] profiles = createProfiles();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String methodToCall= extras.getString("METHOD_TO_CALL");

        if(methodToCall.equals("findTutorByName")){
            findTutorByName(extras.getString("NAME_TO_SEARCH"),profiles);
        }
        //((TextView) findViewById(R.id.sciperNumber)).setText(sciperNumber);

        /*Toast toast=Toast.makeText(this, "Profiles created", Toast.LENGTH_SHORT);
        toast.show();
        ((TextView) findViewById(R.id.sciperNumberOne)).setText(String.valueOf(profiles[0].getSciper()));
        ((TextView) findViewById(R.id.nameOne)).setText(String.valueOf(profiles[0].getFullName()));

        ((TextView) findViewById(R.id.sciperNumberTwo)).setText(String.valueOf(profiles[1].getSciper()));
        ((TextView) findViewById(R.id.nameTwo)).setText(String.valueOf(profiles[1].getFullName()));

        ((TextView) findViewById(R.id.sciperNumberThree)).setText(String.valueOf(profiles[2].getSciper()));
        ((TextView) findViewById(R.id.nameThree)).setText(String.valueOf(profiles[2].getFullName()));

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

    private User[] createProfiles(){
        User profileOne=new User(273516);
        profileOne.setFullName("Alberto Chiappa");
        profileOne.setEmail("alberto.chiappa@epfl.ch");

        User profileTwo=new User(223415);
        profileTwo.setFullName("Albert Einstein");
        profileTwo.setEmail("albert.einstein@epfl.ch");

        User profileThree=new User(124821);
        profileThree.setFullName("Kurt Godel");
        profileThree.setEmail("kurt.godel@epfl.ch");

        return new User[] {profileOne,profileTwo,profileThree};
    }

    private void findTutorByName(String name, User[] profiles){
        int count = 0;
        for(User profile : profiles){
            if(profile.getFullName().equals(name)){
                ((TextView) findViewById(R.id.nameOne)).setText(name);
                count++;
            }
        }
        if(count==0){
            ((TextView) findViewById(R.id.nameOne)).setText("Non ho trovato un tubo");

        }
    }
    /*private void fillDatabase(MockupDatabaseHandler dbHandler){
        ProfileMockup alberto=new ProfileMockup(273516,"Alberto Silvio","Chiappa",3.5f,4.0f);
        dbHandler.addProfile(alberto);
    }*/
}
