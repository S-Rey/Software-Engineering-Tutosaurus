package ch.epfl.sweng.tutosaurus;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class FourthFragment extends PreferenceFragment {

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Settings");
        addPreferencesFromResource(R.xml.settings_preferences);
    }


    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.checkbox_location:
                if (checked) {

                }
                break;
            /**case R.id.checkbox_notifications:
             if (checked)

             else

             break;
             case R.id.checkbox_calendar:
             if (checked)

             else**/
        }
    }

}
