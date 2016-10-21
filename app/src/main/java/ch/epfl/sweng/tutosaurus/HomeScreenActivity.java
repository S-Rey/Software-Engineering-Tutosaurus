package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int GALLERY_REQUEST = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 2;
    private ImageView pictureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        pictureView = (ImageView) findViewById(R.id.picture_view);
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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
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
        } else if (id == R.id.nav_myAppointResults_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new MyAppointResultsFragment()).commit();
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
            if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            else {
                Toast.makeText(HomeScreenActivity.this, "Camera is busy", Toast.LENGTH_SHORT).show();
            }
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
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to load the image", Toast.LENGTH_SHORT).show();
                }

            }
        }
        else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ((ImageView) findViewById(R.id.picture_view)).setImageBitmap(imageBitmap);
        }
    }
}
