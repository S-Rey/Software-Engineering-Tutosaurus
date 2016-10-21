package ch.epfl.sweng.tutosaurus;

import android.app.Fragment;
import android.app.ListFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Vincent on 05/10/2016.
 */

public class HelpFragment extends PreferenceFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Help");
        addPreferencesFromResource(R.xml.help_preferences_layout);

        // remove dividers
        View rootView = getView();
        ListView list = (ListView) rootView.findViewById(android.R.id.list);
        list.setDivider(new ColorDrawable(this.getResources().getColor(R.color.colorPrimaryDark)));
        list.setDividerHeight(2);
        list.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        list.setVerticalScrollBarEnabled(false);
    }

}
