package ch.epfl.sweng.tutosaurus;

import android.app.Fragment;
import android.content.Intent;
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

public class FindTutorsFragment extends Fragment {
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.find_tutors_layout, container, false);
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


        // Display search by name listener
        setDisplayByNameListener((Button) myView.findViewById(R.id.byName));


        // Display search by subject listener
        setDisplayBySubjectListener((Button) myView.findViewById(R.id.bySubject));

        // Search by subject listener
        setSubjectListener(R.id.mathsButton, R.id.mathsText, "findMathsTutor");
        setSubjectListener(R.id.physicsButton, R.id.physicsText, "findPhysicsTutor");
        setSubjectListener(R.id.chemistryButton, R.id.chemistryText, "findChemistryTutor");
        setSubjectListener(R.id.computerButton, R.id.computerText, "findComputerTutor");


        // Show full list
        TextView showFullList = (TextView) myView.findViewById(R.id.showFullList);
        showFullList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FindTutorResult.class);
                Bundle extras = new Bundle();
                extras.putString("METHOD_TO_CALL","showFullList");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        return myView;
    }

    void setSubjectListener(int buttonId, int textId, final String methodName){

        View.OnClickListener listenerToSet =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),FindTutorResult.class);
            Bundle extras = new Bundle();
            extras.putString("METHOD_TO_CALL",methodName);
            intent.putExtras(extras);
            startActivity(intent);
            }
        };

        ImageButton searchButton = (ImageButton) myView.findViewById(buttonId);
        searchButton.setOnClickListener(listenerToSet);
        TextView subjectTextView = (TextView) myView.findViewById(textId);
        subjectTextView.setOnClickListener(listenerToSet);
    }


    void setDisplayByNameListener(final Button byNameButton) {
        byNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEverything();
                setDisplayBySubjectListener((Button) myView.findViewById(R.id.bySubject));
                LinearLayout nameLayout = (LinearLayout) getView().findViewById(R.id.nameLayout);
                nameLayout.setVisibility(View.VISIBLE);
                setShowFullListListener(byNameButton);
            }
        });
    }


    void setDisplayBySubjectListener(final Button bySubjectButton) {
        bySubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEverything();
                setDisplayByNameListener((Button) myView.findViewById(R.id.byName));
                LinearLayout subjectLayout = (LinearLayout) getView().findViewById(R.id.subjectLayout);
                subjectLayout.setVisibility(View.VISIBLE);
                setShowFullListListener(bySubjectButton);
            }
        });
    }


    void setShowFullListListener(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEverything();
                myView.findViewById(R.id.showFullList).setVisibility(View.VISIBLE);
                setDisplayByNameListener((Button) myView.findViewById(R.id.byName));
                setDisplayBySubjectListener((Button) myView.findViewById(R.id.bySubject));

            }
        });
    }


    void hideEverything(){
        myView.findViewById(R.id.subjectLayout).setVisibility(View.GONE);
        myView.findViewById(R.id.nameLayout).setVisibility(View.GONE);
        myView.findViewById(R.id.showFullList).setVisibility(View.GONE);
    }
}
