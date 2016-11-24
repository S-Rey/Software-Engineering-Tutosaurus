package ch.epfl.sweng.tutosaurus.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.LinkedHashMap;
import java.util.Map;

import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;

public class MeetingService extends Service {

    public static final String TAG = "MeetingService";

    private String currentEmail;
    private int numNewRequests = 0;
    private Map<String, String> requests = new LinkedHashMap<>();
    private NotificationManager mNotificationManager;
    private MeetingEventListener mListener;
    private DatabaseReference meetingReqRef;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor prefEditor;
    private boolean shouldNotify;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        // if the service crashed, we should not notify
        shouldNotify = !sharedPref.getBoolean("serviceCrashed", false);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null) {
            Log.d(TAG, "user must be logged in, stopping service");
            stopSelf();
        } else {
            currentEmail = currentUser.getEmail();
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            meetingReqRef = DatabaseHelper.getInstance().getReference().child(DatabaseHelper.MEETING_REQUEST_PATH).child(currentUser.getUid());
            mListener = new MeetingEventListener();
            meetingReqRef.addChildEventListener(mListener);
            Log.d(TAG, "Service started on path: " + meetingReqRef.toString());
        }
        prefEditor = sharedPref.edit();
        // we assume the service crashed as long as onDestroy is not called
        prefEditor.putBoolean("serviceCrashed", true);
        prefEditor.apply();
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        meetingReqRef.removeEventListener(mListener);
        mNotificationManager.cancel(5555);
        // if the service was terminated normally, it did not crash
        prefEditor.putBoolean("serviceCrashed", false);
        prefEditor.apply();
        Log.d(TAG, "service stopped");
    }

    private void notifyNewRequest() {
        if(shouldNotify) {
            NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(this);
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setSummaryText(currentEmail);
            inboxStyle.setBigContentTitle(requests.size() + " new meeting requests");
            for (Map.Entry<String, String> req : requests.entrySet()) {
                inboxStyle.addLine(req.getValue());
            }
            notBuilder.setContentTitle(requests.size() + " new meeting requests")
                    .setSmallIcon(R.drawable.philosoraptor)
                    .setNumber(numNewRequests++)
                    .setStyle(inboxStyle)
                    .setContentText(currentEmail)
                    .setNumber(requests.size());

            synchronized (mNotificationManager) {
                mNotificationManager.notify(5555, notBuilder.build());
            }
        } else {
            Log.d(TAG, "Service just crashed, no need to notify");
            shouldNotify = true;
        }
    }

    private void notifyRequestAccepted() {

    }

    private void notifyRequestRejected() {

    }

    private class MeetingEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            String details = (String) dataSnapshot.child("meeting").child("description").getValue();
            requests.put(key, details);
            notifyNewRequest();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Log.d(TAG, "Meeting changed: " + s);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            requests.remove(key);
            notifyNewRequest();
            Log.d(TAG, "meeting removed");
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            Log.d(TAG, "Meeting moved: " + s);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
