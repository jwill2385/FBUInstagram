package me.jwill2385.instagram;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;


public class ProfileFragment extends Fragment {
     private ImageView ivPicture;
     private Button btnLogout;
     private OnLogoutSelectedListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivPicture = view.findViewById(R.id.ivPicture);
        btnLogout = (Button) view.findViewById(R.id.btnLogout);

        final ParseFile avatarFile = ParseUser.getCurrentUser().getParseFile("image");
        Glide.with(this)
                    .load(avatarFile.getUrl())
                    .into(ivPicture);
//        if (avatarFile != null) {
//            Glide.with(this)
//                    .load(avatarFile.getUrl())
//                    .into(ivPicture);
//        } else {
//            // set a default avatar
//            ivPicture.setImageResource(R.drawable.instagram_user_filled_24);
//        }


        ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo – grab photo from device or have user take pic.
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ParseUser user = ParseUser.getCurrentUser();
                listener.logout(user);
            }
        });

    }

    public interface OnLogoutSelectedListener{
        void logout(ParseUser user);

    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLogoutSelectedListener) {
            listener = (OnLogoutSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implementProfileFragment.OnLogoutSelectedListener");
        }
    }
}
