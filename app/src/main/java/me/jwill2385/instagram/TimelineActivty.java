package me.jwill2385.instagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import me.jwill2385.instagram.model.Post;

public class TimelineActivty extends AppCompatActivity {

    InstaAdapter instaAdapter;
    ArrayList<Post> myposts;
    RecyclerView rvPost;
    // REQUEST_CODE can be any value we like, used to determine the result type later
    private final int REQUEST_CODE = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_activty);

        rvPost = (RecyclerView) findViewById(R.id.rvPost);

        // initiate arraylist (data source)
        myposts = new ArrayList<>();
        // construct the adapter from this data source
        instaAdapter = new InstaAdapter(myposts);
        // recyclerView setup (layout manager, use adapter)
        rvPost.setLayoutManager(new LinearLayoutManager(this));
        // set the adapter
        rvPost.setAdapter(instaAdapter);

        loadTopPost();


//        // Specify which class to query
//        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
//// Specify the object id
//        query.getInBackground("aFuEsvjoHt", new GetCallback<Post>() {
//            public void done(Post item, ParseException e) {
//                if (e == null) {
//                    // Access data using the `get` methods for the object
//                    String body = item.getDescription();
//                    // Access special values that are built-in to each object
//                    String objectId = item.getObjectId();
//                    Date updatedAt = item.getUpdatedAt();
//                    Date createdAt = item.getCreatedAt();
//                    // Do whatever you want with the data...
//                    Toast.makeText(Post, body, Toast.LENGTH_SHORT).show();
//                } else {
//                    // something went wrong
//                }
//            }
//        });
   }

   private  void loadTopPost(){

        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();
        postQuery.newestFirst().withUser();

        postQuery.findInBackground(new FindCallback<Post>(){


            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null){
                    myposts.addAll(objects);
                    instaAdapter.notifyDataSetChanged();

                    for(int i = 0; i < objects.size(); i++){
                        Log.d("LoadTopPost", "Post[" + i + "]");
                    }
                }else{
                    e.printStackTrace();
                }
            }
        });

   }


}
