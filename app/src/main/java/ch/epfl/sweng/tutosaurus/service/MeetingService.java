package ch.epfl.sweng.tutosaurus.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

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

    private int numNewRequests = 0;
    private Map<String, String> requests = new LinkedHashMap<>();
    private NotificationManager mNotificationManager;

    DatabaseReference meetingReqRef;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String user = intent.getStringExtra("uid");
        meetingReqRef = DatabaseHelper.getInstance().getReference().child(DatabaseHelper.MEETING_REQUEST_PATH).child(user);
        meetingReqRef.addChildEventListener(new MeetingEventListener());
        Log.d(TAG, "Service started on path: " + meetingReqRef.toString());
        return START_STICKY;
    }

    private void notifyNewRequest() {
        NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(this);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setSummaryText("New meeting requests");
        for(Map.Entry<String, String> req : requests.entrySet()) {
            inboxStyle.addLine(req.getValue());
        }
        notBuilder.setContentTitle("Tutosaurus")
                .setSmallIcon(R.drawable.philosoraptor)
                .setNumber(numNewRequests ++)
                .setStyle(inboxStyle)
                .setNumber(requests.size());

        synchronized (mNotificationManager) {
            mNotificationManager.notify(5555, notBuilder.build());
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
