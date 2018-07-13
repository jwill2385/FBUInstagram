package me.jwill2385.instagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;

import me.jwill2385.instagram.model.Post;


public class PostDetailsFragment extends Fragment {

    public Post post;
    // initiate variables
    public ImageView ivProfile;
    public ImageView ivPostPicture;
    public ImageView ivLike;
    public ImageView ivComment;
    public ImageView ivChat;
    public ImageView ivSave;
    public TextView tvUsername;
    public  TextView tvCaption;
    public  TextView tvTimeStamp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //reference id of each view from item_Post.xml and then cast to type
        ivProfile = (ImageView) view.findViewById(R.id.ivProfile);
        ivPostPicture = (ImageView) view.findViewById(R.id.ivPostPicture);
        ivLike = (ImageView) view.findViewById(R.id.ivLike);
        ivComment = (ImageView) view.findViewById(R.id.ivComment);
        ivChat = (ImageView) view.findViewById(R.id.ivChat);
        ivSave = (ImageView) view.findViewById(R.id.ivSave);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvCaption = (TextView) view.findViewById(R.id.tvCaption);
        tvTimeStamp = (TextView) view.findViewById(R.id.tvTimeStamp);

        ParseUser user = post.getUser();
        ParseFile profileImage = user.getParseFile("image");
        ParseFile picture = post.getImage();
        Glide.with(getContext()).load(profileImage.getUrl()).into(ivProfile);
        Glide.with(getContext()).load(picture.getUrl()).into(ivPostPicture);

        tvUsername.setText(user.getUsername());
        tvCaption.setText(post.getDescription());
        tvTimeStamp.setText(post.getCreatedAt().toString());



    }


}
