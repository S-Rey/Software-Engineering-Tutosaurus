package ch.epfl.sweng.tutosaurus.helper;

import android.app.Activity;
import android.app.VoiceInteractor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

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

import ch.epfl.sweng.tutosaurus.R;


public class PictureHelper {

    static private StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://tutosaurus-16fce.appspot.com");
    static final long MAX_SIZE = 1024 * 1024; // One MB


    /**
     * Upload a picture (located at picPath) under sciper/ folder in the storage of Firebase.
     * @param picPath
     * @param sciper
     */
    static public void putImage(String picPath, String sciper) {
        Uri file = Uri.fromFile(new File(picPath));
        StorageReference riversRef = storageRef.child(sciper + "/"+file.getLastPathSegment());
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
     * Store the profile picture in Firebase storage in a folder which the name is the sciper
     * @param activity
     * @param sciper
     * @throws FileNotFoundException
     */
    static public void storeProfilePic (Activity activity, String sciper) throws FileNotFoundException {
        String pathPic =  activity.getFilesDir().getAbsolutePath() + File.separator + "pictures/profile.jpg";
        File file = new File(pathPic);
        if(file.exists()) {
            putImage(pathPic, sciper);
        } else {
            throw new FileNotFoundException("There is no picture : " + pathPic);
        }
    }


    /**
     * Write a picture (in JPEG format) to the internal storage at pictures/
     * @param activity
     * @param name
     */
    static public void savePicture (final Activity activity, String name, Bitmap pic) {
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
    static public Bitmap loadPicture(final Activity activity, String name) {
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


    /*
    public static String encodeToString(String pathToImage) {

        Bitmap bm = BitmapFactory.decodeFile(pathToImage);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        return encodedImage;
    }
    */
}
