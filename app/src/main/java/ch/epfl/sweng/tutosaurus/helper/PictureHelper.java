package ch.epfl.sweng.tutosaurus.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class PictureHelper {

    static private StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://tutosaurus-16fce.appspot.com");
    static final long MAX_SIZE = 1024 * 1024; // One MB






    /**
     * Store the profile picture in the local folder storage in a folder which the name is the sciper
     * @param activity
     * @param sciper
     * @throws FileNotFoundException
     */
    static public void storeProfilePicOnline (Activity activity, String sciper) throws FileNotFoundException {
        String pathPic =  activity.getFilesDir().getAbsolutePath() + File.separator + "pictures/profile.jpg";
        File file = new File(pathPic);
        if(file.exists()) {
            storePicOnline(pathPic, sciper);
        } else {
            throw new FileNotFoundException("There is no picture : " + pathPic);
        }
    }

    /**
     * Upload a picture (located at picPath) under sciper/ folder in the storage of Firebase.
     * Called by storePicOnline
     * @param picPath
     * @param key
     */
    static public void storePicOnline(String picPath, String key) {
        Uri file = Uri.fromFile(new File(picPath));
        StorageReference riversRef = storageRef.child("profilePictures/"+key+".jpg"); //file.getLastPathSegment()
        UploadTask uploadTask = riversRef.putFile(file);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }


    /**
     *  Store a picture store at localPicPath to the onlinePicPath (don't forget the extension jpg
     *  or png)
     * @param localPicPath
     * @param onlinePicPath
     */
    static public void storePictureOnline(String localPicPath, String onlinePicPath) {
        Uri file = Uri.fromFile(new File(localPicPath));
        StorageReference storeRef = storageRef.child(onlinePicPath);
        UploadTask uploadTask = storeRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }


    /**
     * Write a picture (in JPEG format) to the internal storage at pictures/
     * @param activity
     * @param name
     */
    static public void storePicLocal (final Activity activity, String name, Bitmap pic) {
        String dirPath = activity.getFilesDir().getAbsolutePath() + File.separator + "pictures";
        File projDir = new File(dirPath);

        if(!projDir.exists()) {
            projDir.mkdirs();
        }

        if (pic != null) {
            File file = new File(dirPath + "/" + name + ".jpg");
            try {
                file.createNewFile();
                FileOutputStream fileOutput = new FileOutputStream(file);
                ByteArrayOutputStream byteArrOutputStream = new ByteArrayOutputStream();
                pic.compress(Bitmap.CompressFormat.JPEG, 100, byteArrOutputStream);
                fileOutput.write(byteArrOutputStream.toByteArray());
                fileOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * Load the picture specified by name and return it in case of success
     * otherwise return null
     * @param activity
     * @param name
     * @return bitmap
     */
    @Nullable
    static public Bitmap loadPictureLocal(final Activity activity, String name) {
        String filePath = activity.getFilesDir().getAbsolutePath() + File.separator +
                "pictures/" + name + ".jpg";
        try {
            File file = new File(filePath);
            FileInputStream fileInStream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(fileInStream);
            fileInStream.close();
            return  bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
