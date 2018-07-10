package me.jwill2385.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.jwill2385.instagram.model.Post;

public class ParseApp extends Application {


    @Override
   public void onCreate(){
        super.onCreate(); // runs on create of Application class

        ParseObject.registerSubclass(Post.class);
        //this will set up server to make network call
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("fakeig")
                .clientKey("apples-2")
                .server("http://jwill2385-fbu-instagram.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);
    }
}
