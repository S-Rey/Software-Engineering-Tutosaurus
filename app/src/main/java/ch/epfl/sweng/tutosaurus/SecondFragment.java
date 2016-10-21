package ch.epfl.sweng.tutosaurus;

import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Vincent on 05/10/2016.
 */

public class SecondFragment extends Fragment {
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.second_layout, container, false);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Find Tutors");

        // Search by name listener
        Button searchByName = (Button) myView.findViewById(R.id.searchByName);
        searchByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FindTutorResult.class);
                EditText nameToSearch=(EditText) getView().findViewById(R.id.nameToSearch);
                String name=nameToSearch.getText().toString();
                Bundle extras = new Bundle();
                extras.putString("NAME_TO_SEARCH",name);
                extras.putString("METHOD_TO_CALL","findTutorByName");
                intent.putExtras(extras);
                startActivity(intent);
                }
        });

        // Search by sciper listener
        Button searchBySciper = (Button) myView.findViewById(R.id.searchBySciper);
        searchBySciper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FindTutorResult.class);
                EditText sciperToSearch=(EditText) getView().findViewById(R.id.sciperToSearch);
                String sciper=sciperToSearch.getText().toString();
                Bundle extras = new Bundle();
                extras.putString("SCIPER_TO_SEARCH",sciper);
                extras.putString("METHOD_TO_CALL","findTutorBySciper");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        // Display search by name listener
        Button displayByName = (Button) myView.findViewById(R.id.byName);
        displayByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout nameLayout=(LinearLayout) getView().findViewById(R.id.nameLayout);
                nameLayout.setVisibility(View.VISIBLE);
            }
        });

        // Display search by subject listener
        Button displayBySubject = (Button) myView.findViewById(R.id.bySubject);
        displayBySubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout subjectLayout=(LinearLayout) getView().findViewById(R.id.subjectLayout);
                subjectLayout.setVisibility(View.VISIBLE);
            }
        });

        // Display search by sciper listener
        Button displayBySciper = (Button) myView.findViewById(R.id.bySciper);
        displayBySciper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout nameLayout=(LinearLayout) getView().findViewById(R.id.sciperLayout);
                nameLayout.setVisibility(View.VISIBLE);
            }
        });

        // Search by subject listener

        ImageButton searchMathsButton = (ImageButton) myView.findViewById(R.id.mathsButton);
        searchMathsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FindTutorResult.class);
                Bundle extras = new Bundle();
                extras.putString("METHOD_TO_CALL","findMathsTutor");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        ImageButton searchPhysicsButton = (ImageButton) myView.findViewById(R.id.physicsButton);
        searchPhysicsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FindTutorResult.class);
                Bundle extras = new Bundle();
                extras.putString("METHOD_TO_CALL","findPhysicsTutor");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        ImageButton searchChemistryButton = (ImageButton) myView.findViewById(R.id.chemistryButton);
        searchChemistryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FindTutorResult.class);
                Bundle extras = new Bundle();
                extras.putString("METHOD_TO_CALL","findChemistryTutor");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        ImageButton searchComputerButton = (ImageButton) myView.findViewById(R.id.computerButton);
        searchComputerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FindTutorResult.class);
                Bundle extras = new Bundle();
                extras.putString("METHOD_TO_CALL","findComputerTutor");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        return myView;
    }

}
