package ch.epfl.sweng.tutosaurus;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Settings");
        addPreferencesFromResource(R.xml.settings_preferences_layout);

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
