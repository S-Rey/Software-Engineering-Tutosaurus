package ch.epfl.sweng.tutosaurus;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.Query;

import ch.epfl.sweng.tutosaurus.adapter.CustomAdapter;
import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Meeting;

public class MeetingsFragment extends Fragment {

    View myView;
    private CustomAdapter adapter;
    private String currentUser = "236905";
    DatabaseHelper dbh = DatabaseHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.meetings_layout, container, false);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Meetings");

        //Button syncCalendar = (Button) myView.findViewById(R.id.syncCalendar);

        ListView meetingList = (ListView) myView.findViewById(R.id.meetingList);
        Query ref = dbh.getMeetingsRefForUser(currentUser);
        ref = ref.orderByChild("date");
        adapter = new CustomAdapter(getActivity(), Meeting.class, R.layout.listview_meetings_row, ref);
        meetingList.setAdapter(adapter);

        return myView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }

}
