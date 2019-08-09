package com.example.android.eserviceexchange.ServicePostsPackage;

import com.google.firebase.firestore.Exclude;

import androidx.annotation.NonNull;

public class ServicePostID {

    @Exclude
    public String ServicePostID;

    /**
     * @param id
     * @param <T>
     * @return
     */
    public <T extends ServicePostID> T withId(@NonNull final String id) {
        this.ServicePostID = id;
        return (T) this;
    }
}
