package me.jwill2385.instagram;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import me.jwill2385.instagram.model.Post;

public class HomeFragment extends Fragment{

    private SwipeRefreshLayout swipeContainer;

    InstaAdapter instaAdapter;
    ArrayList<Post> myposts;
    RecyclerView rvPost;
    private MainActivityListener listener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        rvPost = (RecyclerView) view.findViewById(R.id.rvPost);
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // initiate arraylist (data source)
        myposts = new ArrayList<>();
        // construct the adapter from this data source
        instaAdapter = new InstaAdapter(myposts);
        // recyclerView setup (layout manager, use adapter)
        rvPost.setLayoutManager(new LinearLayoutManager(view.getContext()));
        // set the adapter
        rvPost.setAdapter(instaAdapter);
        loadTopPost();

        instaAdapter.setNewListener(new InstaAdapter.AdapterListener() {
            @Override
            public void sendPostToHomeFragment(Post post) {
                listener.sendPostToMainActivity(post);
            }
        });
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

    public interface MainActivityListener{
        void sendPostToMainActivity(Post post);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivityListener){
            listener = (MainActivityListener) context;
        }else{
            throw new ClassCastException(context.toString()
            + "must implement MainActivityListener");
        }
    }
}
