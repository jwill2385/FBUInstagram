package me.jwill2385.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;

import me.jwill2385.instagram.model.Post;

public class MainActivity extends AppCompatActivity implements PostFragment.OnItemSelectedListener, ProfileFragment.OnLogoutSelectedListener,
        HomeFragment.MainActivityListener {


    public static final String TAG = PostFragment.class.getSimpleName();
    BottomNavigationView bottomNavigationView;
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;
    PostFragment fragment2;
    PostDetailsFragment detailsFragment;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //gives user profile image if they don't have one
        final ParseFile avatarFile = ParseUser.getCurrentUser().getParseFile("image");
        if(avatarFile == null) {
            // set user avatar here if they don't have one
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.instagram_user_filled_24);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Compress image to lower quality scale 1 - 100
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();
            // Create the ParseFile
            ParseFile file = new ParseFile("instagram_user_filled_24.png", image);
            // Upload the image into Parse Cloud
            ParseUser user = ParseUser.getCurrentUser();
            user.put("image", file);

            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                }
            });
        }


        // define your fragments here
        final HomeFragment fragment1 = new HomeFragment();
        fragment2 = new PostFragment();
        final Fragment fragment3 = new ProfileFragment();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        // handle navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ic_home:
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.flContainer, fragment1).commit();
                                return true;
                            case R.id.ic_newPost:
                                FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                                fragment2 = new PostFragment();
                                fragmentTransaction2.replace(R.id.flContainer, fragment2).commit();
                                return true;
                            case R.id.ic_profile:
                                 FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
                                 fragmentTransaction3.replace(R.id.flContainer, fragment3).commit();
                                return true;
                        }
                        return false;
                    }
                });

    }


    @Override
    public void createPost(String description, ParseFile imageFile, ParseUser user) {
        Log.d(TAG, "We are in createPost()");
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Create post Success");
                } else {
                    e.printStackTrace();
                }
            }
        });

        // now send user to timeline screen (Home Fragment)
        bottomNavigationView.setSelectedItemId(R.id.ic_home);
    }

    @Override
    public void onLaunchCamera(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);
        sendPhotoFile();
        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // must update the photofragments picturefile with the one we just took
    private void sendPhotoFile(){
        fragment2.photoFile = photoFile;
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // Load the taken image into a preview
                ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
                ivPreview.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void logout(ParseUser user) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        if(currentUser == null)
        {
            Log.i("Logout","Logout successful");
        }
        Intent a = new Intent(this, LoginActivity.class);
        startActivity(a);
        finish();
    }

    @Override
    public void sendPostToMainActivity(Post post) {
       detailsFragment = new PostDetailsFragment();
        detailsFragment.post = post;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, detailsFragment).commit();
    }
}
