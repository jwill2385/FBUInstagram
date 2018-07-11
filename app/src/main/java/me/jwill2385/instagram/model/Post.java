package me.jwill2385.instagram.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("post")
public class Post extends ParseObject {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";

    // setters and getters
    public String getDescription(){

        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        //assigns key_description with description value
        put(KEY_DESCRIPTION, description);
    }

    // returns a parsefile of the image
    public ParseFile getImage(){

        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
    put(KEY_IMAGE, image);
    }

    // returns a parseUser of the user
    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public static class Query extends ParseQuery<Post>{


        public Query() {
            super(Post.class);
        }

        public Query newestFirst(){

            orderByDescending("createdAt");
            return this;
        }
        public Query getTop(){
        setLimit(20);
        return this;
        }

        public Query withUser(){

            include("user");
            return this;
        }
    }


}
