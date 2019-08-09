package com.example.android.eserviceexchange.ServicePostsPackage;

import java.util.Date;

public class ServicePost extends com.example.android.eserviceexchange.ServicePostsPackage.ServicePostID {
    public String desc, user_id, folder_path;
    public int number_of_images;
    public Date timestamp;

    public ServicePost(){}

    public ServicePost(String desc, String user_id, String folder_path, int number_of_images, Date timestamp) {
        this.desc = desc;
        this.user_id = user_id;
        this.folder_path = folder_path;
        this.number_of_images = number_of_images;
        this.timestamp = timestamp;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFolder_path() {
        return folder_path;
    }

    public void setFolder_path(String folder_path) {
        this.folder_path = folder_path;
    }

    public int getNumber_of_images() {
        return number_of_images;
    }

    public void setNumber_of_images(int number_of_images) { this.number_of_images = number_of_images; }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
