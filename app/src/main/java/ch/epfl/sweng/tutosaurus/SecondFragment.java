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

        // Display search by name listener
        Button displayByName = (Button) myView.findViewById(R.id.byName);
        displayByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout nameLayout=(LinearLayout) getView().findViewById(R.id.nameLayout);
                nameLayout.setVisibility(View.VISIBLE);
            }
        });

        return myView;
    }

}
