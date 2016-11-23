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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.tutosaurus.adapter.ClassicCourseAdapter;
import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.FullCourseList;

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

        // Populate list of subjects with search methods
        ArrayList<Course> courses = FullCourseList.getInstance().getListOfCourses();
        ClassicCourseAdapter courseAdapter = new ClassicCourseAdapter(  getActivity().getBaseContext(),
                                                                        R.layout.listview_course_row,
                                                                        courses);

        ListView courseList= (ListView) myView.findViewById(R.id.courseList);
        courseList.setAdapter(courseAdapter);

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
                ListView subjectList = (ListView) getView().findViewById(R.id.courseList);
                subjectList.setVisibility(View.VISIBLE);
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
        myView.findViewById(R.id.courseList).setVisibility(View.GONE);
        myView.findViewById(R.id.nameLayout).setVisibility(View.GONE);
        myView.findViewById(R.id.showFullList).setVisibility(View.GONE);
    }
}
