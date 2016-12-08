package ch.epfl.sweng.tutosaurus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import ch.epfl.sweng.tutosaurus.helper.PictureHelper;
import ch.epfl.sweng.tutosaurus.model.Chat;
import ch.epfl.sweng.tutosaurus.service.MeetingService;
import de.hdodenhof.circleimageview.CircleImageView;

import static ch.epfl.sweng.tutosaurus.RegisterScreenActivity.PROFILE_INFOS;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeScreenActivity";

    public static final int GALLERY_REQUEST = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 2;

    private final int PROFILE_PICTURE_HEIGHT = 600;
    private final int PROFILE_PICTURE_WIDTH = 600;

    private ImageView pictureView;
    private CircleImageView circleView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
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

        Intent serviceIntent = new Intent(this, MeetingService.class);
        getApplicationContext().startService(serviceIntent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
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

        TextView nameView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.fullName);
        nameView.setText(first_name + " " + last_name);

        TextView addressView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.mailAddress);
        addressView.setText(email_address);

        TextView sciperView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.sciper);
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

            if(intent.getAction().equals("OPEN_TAB_MESSAGES")) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new MessagingFragment()).commit();
            }
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(TAG, "Restarted!");
        pictureView = (ImageView) findViewById(R.id.picture_view);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent.getAction() != null) {
            if (intent.getAction().equals("OPEN_TAB_PROFILE")) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new ProfileFragment()).commit();
            }
            if (intent.getAction().equals("OPEN_TAB_MEETINGS")) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new MeetingsFragment()).commit();
            }

            if(intent.getAction().equals("OPEN_TAB_MESSAGES")) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new MessagingFragment()).commit();
            }
        }
        Log.d(TAG, "Resumed!");
        pictureView = (ImageView) findViewById(R.id.picture_view);
    }

    public void sendMessageForCall(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: (+41)210000000"));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CALL_PHONE},   //request specific permission from user
                    10);
        } else {
            startActivity(intent);
        }
    }

    public void sendMessageForEmail(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:")).setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "android.studio@epfl.ch" });
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
            Intent logInIntent = new Intent(this, MainActivity.class);
            logInIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logInIntent);
            Intent serviceIntent = new Intent(this, MeetingService.class);
            stopService(serviceIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        } else if (id == R.id.nav_messaging_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new MessagingFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void changePassword(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        EditText newPasswordChoosed = (EditText) findViewById(R.id.chooseNewPass);
        EditText newPasswordConfirmed = (EditText) findViewById(R.id.confirmNewPass);

        if (newPasswordChoosed.getText().toString().equals("") || newPasswordConfirmed.getText().toString().equals("")) {
            Toast.makeText(this, "Please fill both boxes above", Toast.LENGTH_SHORT).show();
        } else if (!newPasswordChoosed.getText().toString().equals(newPasswordConfirmed.getText().toString())) {
            Toast.makeText(this, "Passwords must match", Toast.LENGTH_SHORT).show();
        } else {
            user.updatePassword(newPasswordChoosed.getText().toString());
            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
            closePassword();
        }
    }

    private void openPassword() {
        EditText changePassField = (EditText) findViewById(R.id.chooseNewPass);
        EditText changePassFieldConfirm = (EditText) findViewById(R.id.confirmNewPass);
        changePassField.setHint("Choose New Password");
        changePassField.setVisibility(View.VISIBLE);
        changePassFieldConfirm.setHint("Confirm New Password");
        changePassFieldConfirm.setVisibility(View.VISIBLE);

        Button sendNewPassInfo = (Button) findViewById(R.id.changeNewPass);
        sendNewPassInfo.setVisibility(View.VISIBLE);

        Button changePasswordButton = (Button) findViewById(R.id.changePassword);
        View.OnClickListener changePassClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePassword();
            }
        };
        changePasswordButton.setOnClickListener(changePassClick);
    }

    private void closePassword() {
        EditText changePassField = (EditText) findViewById(R.id.chooseNewPass);
        EditText changePassFieldConfirm = (EditText) findViewById(R.id.confirmNewPass);
        Button sendNewPassInfo = (Button) findViewById(R.id.changeNewPass);
        changePassField.setVisibility(View.GONE);
        changePassField.setText("");
        changePassFieldConfirm.setVisibility(View.GONE);
        changePassFieldConfirm.setText("");
        sendNewPassInfo.setVisibility(View.GONE);

        Button changePasswordButton = (Button) findViewById(R.id.changePassword);
        View.OnClickListener changePassClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPassword();
            }
        };
        changePasswordButton.setOnClickListener(changePassClick);
    }

    void setPassTabToOpen(View v){
        EditText changePassField = (EditText) v.findViewById(R.id.chooseNewPass);
        EditText changePassFieldConfirm = (EditText) v.findViewById(R.id.confirmNewPass);
        Button sendNewPassInfo = (Button) v.findViewById(R.id.changeNewPass);
        changePassField.setVisibility(View.GONE);
        changePassFieldConfirm.setVisibility(View.GONE);
        sendNewPassInfo.setVisibility(View.GONE);

        Button changePasswordButton = (Button) v.findViewById(R.id.changePassword);
        View.OnClickListener changePassClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPassword();
            }
        };
        changePasswordButton.setOnClickListener(changePassClick);
    }

    private void loadImageFromGallery(View view) {
        Intent imageGalleryIntent = new Intent(Intent.ACTION_PICK);
        File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String picturesDirectoryPath = picturesDirectory.getPath();
        Uri uriRepresentationPicturesDir = Uri.parse(picturesDirectoryPath);
        imageGalleryIntent.setDataAndType(uriRepresentationPicturesDir, "image/*");

        startActivityForResult(imageGalleryIntent, GALLERY_REQUEST);
    }

    private void dispatchTakePictureIntent(View v) {
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

    protected void dispatchChatIntent(Intent chatIntent) {
        if(chatIntent.getComponent().getClassName().equals(ChatActivity.class.getName())) {
            startActivity(chatIntent);
        } else {
            Log.d(TAG, "not a chat intent");
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
        FileInputStream in;
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
                    imageSelected = resizeBitmap(imageSelected);
                    pictureView = (ImageView) findViewById(R.id.picture_view);
                    pictureView.setImageBitmap(imageSelected);
                    pictureView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    saveToInternalStorage(imageSelected);
                    inputStream.close();

                    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    PictureHelper.storePicOnline(imageSelectedUri.getPath(), currentUser);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to load the image", Toast.LENGTH_SHORT).show();
                }

            }

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = resizeBitmap(imageBitmap);
            pictureView = (ImageView) findViewById(R.id.picture_view);
            pictureView.setImageBitmap(imageBitmap);
            pictureView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            saveToInternalStorage(imageBitmap);
        }
    }

    private Bitmap resizeBitmap(Bitmap img) {
        return ThumbnailUtils.extractThumbnail(img, PROFILE_PICTURE_WIDTH, PROFILE_PICTURE_HEIGHT);
    }

    public void showChangePictureDialog(final View view){
        AlertDialog.Builder changePictureDialog = new AlertDialog.Builder(this);
        //changePictureDialog.setTitle("New profile picture");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);

        arrayAdapter.add("Take picture with camera");
        arrayAdapter.add("Load picture from gallery");

        changePictureDialog.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        changePictureDialog.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        if(strName.equals("Take picture with camera")){
                            dispatchTakePictureIntent(view);
                        }
                        else if(strName.equals("Load picture from gallery")){
                            loadImageFromGallery(view);
                        }
                    }
                });
        changePictureDialog.show();
    }
}
