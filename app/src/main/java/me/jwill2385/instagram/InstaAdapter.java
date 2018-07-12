package me.jwill2385.instagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

import me.jwill2385.instagram.model.Post;

public class InstaAdapter extends RecyclerView.Adapter<InstaAdapter.ViewHolder> {

    Context context;
    private List<Post> mPost;  // this list will store all my post

    //pass in post to adapter
    public InstaAdapter(List<Post> posts){
        mPost = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View instaView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(instaView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
// get the data according to position
        Post post = mPost.get(position);
        //  populate the views according to this data
        holder.tvUsername.setText(post.getUser().getUsername());
        holder.tvCaption.setText(post.getDescription());
        holder.tvTimeStamp.setText(getRelativeTime(post.getCreatedAt()));

        Glide.with(context).load(ParseUser.getCurrentUser().getParseFile("image").getUrl()).into(holder.ivProfile);
        Glide.with(context).load(post.getImage().getUrl()).into(holder.ivPostPicture);

    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // perform findViewById lookups

            //reference id of each view from item_Post.xml and then cast to type
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
            ivPostPicture = (ImageView) itemView.findViewById(R.id.ivPostPicture);
            ivLike = (ImageView) itemView.findViewById(R.id.ivLike);
            ivComment = (ImageView) itemView.findViewById(R.id.ivComment);
            ivChat = (ImageView) itemView.findViewById(R.id.ivChat);
            ivSave = (ImageView) itemView.findViewById(R.id.ivSave);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
        }
    }


    // Clean all elements of the recycler
    public void clear() {
        mPost.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPost.addAll(list);
        notifyDataSetChanged();
    }

    public static String getRelativeTime(Date date) {
        //String Format = "yyyy-mm-dd'T'HH:mm:ss.sss'Z'";
        String relativeDate;
        long dateMillis = date.getTime();
        relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        return relativeDate;
    }



}
