package ch.epfl.sweng.tutosaurus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static ch.epfl.sweng.tutosaurus.RegisterScreenActivity.PROFILE_INFOS;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeScreenActivity";

    public static final int GALLERY_REQUEST = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 2;
    private ImageView pictureView;
    private CircleImageView circleView;
    private TextView nameView;
    private TextView addressView;
    private TextView sciperView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        pictureView = (ImageView) findViewById(R.id.picture_view);

        circleView = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.circleView);
        linkProfilePictureToNavView(circleView);

        SharedPreferences settings = getSharedPreferences(PROFILE_INFOS, Context.MODE_PRIVATE);
        String first_name = settings.getString("firstName", "");
        String last_name = settings.getString("lastName", "");
        String email_address = settings.getString("email", "");
        String sciper = settings.getString("sciper", "");

        nameView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.fullName);
        nameView.setText(first_name + " " + last_name);

        addressView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.mailAddress);
        addressView.setText(email_address);

        sciperView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.sciper);
        sciperView.setText(sciper);

        if (intent.getAction() != null) {
            if (intent.getAction().equals("OPEN_TAB_PROFILE")) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new ProfileFragment()).commit();
            }
            if (intent.getAction().equals("OPEN_TAB_MEETINGS")) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new MeetingsFragment()).commit();
            }
        }
    }

    public void meetingsNotification(View view) {
        SharedPreferences notif = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean areNotifEnable = notif.getBoolean("checkbox_preference_notification", true);

        if (areNotifEnable) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.philosoraptor)
                    .setContentTitle("Meeting Notification")
                    .setContentText("Click Here To Test The Notification")
                    .setAutoCancel(true)
                    .setColor(getResources().getColor(R.color.colorPrimaryDark));

            Intent resultIntent = new Intent(this, HomeScreenActivity.class);
            resultIntent.setAction("OPEN_TAB_MEETINGS");

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(HomeScreenActivity.class);
            stackBuilder.addNextIntent(resultIntent);

            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(9999, mBuilder.build());
        }
    }

    public void sendMessageForCall(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: (+41)210000000"));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CALL_PHONE},   //request specific permission from user
                    10);
            return;
        } else {
            startActivity(intent);
        }
    }

    public void sendMessageForEmail(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:")).setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, "android.studio@epfl.ch");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logOutButton) {
            mAuth.signOut();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.app.FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_profile_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ProfileFragment()).commit();
        } else if (id == R.id.nav_findTutors_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new FindTutorsFragment()).commit();
        } else if (id == R.id.nav_beATutor_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new BeATutorFragment()).commit();
        } else if (id == R.id.nav_settings_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
        } else if (id == R.id.nav_help_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HelpFragment()).commit();
        } else if (id == R.id.nav_about_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AboutFragment()).commit();
        } else if (id == R.id.nav_meetings_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new MeetingsFragment()).commit();
        } else if (id == R.id.nav_db_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new DatabaseFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void loadImageFromGallery(View view) {
        Intent imageGalleryIntent = new Intent(Intent.ACTION_PICK);
        File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String picturesDirectoryPath = picturesDirectory.getPath();
        Uri uriRepresentationPicturesDir = Uri.parse(picturesDirectoryPath);
        imageGalleryIntent.setDataAndType(uriRepresentationPicturesDir, "image/*");

        startActivityForResult(imageGalleryIntent, GALLERY_REQUEST);
    }

    public void dispatchTakePictureIntent(View v) {
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            Toast toast = Toast.makeText(HomeScreenActivity.this, "No camera", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Toast.makeText(HomeScreenActivity.this, "Camera is busy", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveToInternalStorage(Bitmap bitmapImage) {
        FileOutputStream fos = null;
        try {
            fos = getApplicationContext().openFileOutput("user_profile_pic.bmp", Context.MODE_PRIVATE);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        circleView = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.circleView);
        linkProfilePictureToNavView(circleView);
    }

    private void linkProfilePictureToNavView(CircleImageView item) {
        FileInputStream in = null;
        try {
            in = getApplicationContext().openFileInput("user_profile_pic.bmp");
            Bitmap b = BitmapFactory.decodeStream(in);
            item.setImageBitmap(b);
        }
        catch (FileNotFoundException e) {
            item.setImageResource(R.drawable.dino_logo);
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST) {

            if (resultCode == RESULT_OK) {

                Uri imageSelectedUri = data.getData();
                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(imageSelectedUri);
                    Bitmap imageSelected = BitmapFactory.decodeStream(inputStream);
                    pictureView = (ImageView) findViewById(R.id.picture_view);
                    pictureView.setImageBitmap(imageSelected);
                    saveToInternalStorage(imageSelected);
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to load the image", Toast.LENGTH_SHORT).show();
                }

            }

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ((ImageView) findViewById(R.id.picture_view)).setImageBitmap(imageBitmap);
            saveToInternalStorage(imageBitmap);
        }
    }

}
