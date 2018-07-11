package me.jwill2385.instagram;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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

    private SwipeRefreshLayout swipeContainer;

    InstaAdapter instaAdapter;
    ArrayList<Post> myposts;
    RecyclerView rvPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_activty);

        rvPost = (RecyclerView) findViewById(R.id.rvPost);
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // initiate arraylist (data source)
        myposts = new ArrayList<>();
        // construct the adapter from this data source
        instaAdapter = new InstaAdapter(myposts);
        // recyclerView setup (layout manager, use adapter)
        rvPost.setLayoutManager(new LinearLayoutManager(this));
        // set the adapter
        rvPost.setAdapter(instaAdapter);
        loadTopPost();

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


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

    public void fetchTimelineAsync(int page) {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();
        postQuery.newestFirst().withUser();

        postQuery.findInBackground(new FindCallback<Post>(){


            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null){
                    // clear out adapter and array
                    myposts.clear();
                    instaAdapter.clear();
                    //now add all the post back
                    myposts.addAll(objects);

                    for(int i = 0; i < objects.size(); i++){
                        Log.d("RefreshingtopPost", "Post[" + i + "]");
                    }
                }else{
                    e.printStackTrace();
                }
            }

        });
        // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);




    }



}
