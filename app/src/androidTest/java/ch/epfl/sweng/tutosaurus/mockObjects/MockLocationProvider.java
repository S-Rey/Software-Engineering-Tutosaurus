package ch.epfl.sweng.tutosaurus.mockObjects;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;

/**
 * Created by santo on 26/11/16.
 */

public class MockLocationProvider {

    private String providerName;
    private Context context;


    public MockLocationProvider(String name, Context context) {
        this.providerName = name;
        this.context = context;

        LocationManager lm = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);
        lm.addTestProvider(providerName, false, false, false, false, false,
                true, true, 0, 5);
        lm.setTestProviderEnabled(providerName, true);
    }


    public void pushLocation(double lat, double lon) {
        LocationManager lm = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);

        Location mockLocation = new Location(providerName);
        mockLocation.setLatitude(lat);
        mockLocation.setLongitude(lon);
        mockLocation.setAltitude(0);
        mockLocation.setTime(System.currentTimeMillis());
        mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        mockLocation.setAccuracy(1);
        lm.setTestProviderLocation(providerName, mockLocation);
    }


    public void shutdown() {
        LocationManager lm = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);
        lm.removeTestProvider(providerName);
    }
}