package me.jwill2385.instagram;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class PostFragment extends Fragment {

    private final String imagePath = "/storage/emulated/0/Download/lightning.jpg";
    private EditText etDescription;
    private Button btnCreate;
    private ImageView ivCamera;

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;



    public static final String TAG = PostFragment.class.getSimpleName();
    private OnItemSelectedListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etDescription = (EditText) view.findViewById(R.id.etDescription);
        btnCreate = (Button) view.findViewById(R.id.btnCreate);
        ivCamera = (ImageView) view.findViewById(R.id.ivCamera);
        Log.d(TAG, "Home activity has started");

        // auto launches the camera
        listener.onLaunchCamera(view);


        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onLaunchCamera(view);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = etDescription.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();

                final ParseFile parseFile = new ParseFile(photoFile);

                parseFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            listener.createPost(description, parseFile, user);
                        } else {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });


    }

    //how fragment communicates with activity
    public interface  OnItemSelectedListener{

        //void loadTopPosts();
        void onLaunchCamera(View view);
        void createPost(String description, ParseFile imageFile, ParseUser user);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){
            listener = (OnItemSelectedListener) context;
        }else{
            throw new ClassCastException(context.toString()
                    + " must implement PostFragment.OnItemSelectedListener");
        }
    }

}
