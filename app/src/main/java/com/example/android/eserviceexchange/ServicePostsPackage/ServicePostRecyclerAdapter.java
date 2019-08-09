package com.example.android.eserviceexchange.ServicePostsPackage;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.eserviceexchange.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ServicePostRecyclerAdapter extends RecyclerView.Adapter<ServicePostRecyclerAdapter.ViewHolder> {

    public List<ServicePost> postList;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public ServicePostRecyclerAdapter(List<ServicePost> postList){
        this.postList = postList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_item, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        final String PostId = postList.get(position).ServicePostID;
        final String cmmntPublisherId = postList.get(position).user_id;

        String desc_data = postList.get(position).getDesc();
        holder.setDescText(desc_data);

        String image_path = postList.get(position).getFolder_path();
        List<Uri> imgArrayUri= new ArrayList<>(postList.get(position).number_of_images);
        holder.setPostImages(image_path,imgArrayUri);

        String user_id = postList.get(position).getUser_id();
        //User Data will be retrieved here..

        firebaseFirestore.collection("UsersAccount").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    String userName = task.getResult().getString("Username");
                    String city= task.getResult().getString("City");

                    holder.setUserData(userName,city);

                } else {

                    String errorMSG= task.getException().getMessage();
                    Toast.makeText(context,"Error: "+ errorMSG,Toast.LENGTH_LONG).show();
                }

            }
        });

        try {
            long millisecond = postList.get(position).getTimestamp().getTime();
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
            holder.setTime(dateString);
        } catch (Exception e) {

            Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView descView;
        //private ImageView blogImageView;   will be replaced by array
        private TextView postDate;
        private TextView postUserName;
        private TextView postCity;



        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDescText(String descText){

            descView = mView.findViewById(R.id.desc_load);
            descView.setText(descText);

        }

        public void setPostImages(String downloadUri, List<Uri> listUri){

            LinearLayout imagesLoad= (LinearLayout) mView.findViewById(R.id.images_load);
            LayoutInflater layoutInflater= (LayoutInflater) LayoutInflater.from(context);

            for (int i = 0; i < listUri.size(); i++){
                View view = layoutInflater.inflate(R.layout.gallery_image_item,imagesLoad,false);
                ImageView postImgs= view.findViewById(R.id.post_image_load);
                //uri goes here
                RequestOptions placeholderRequest = new RequestOptions();
                placeholderRequest.placeholder(R.drawable.ic_launcher_background);

                Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(listUri.get(i)).into(postImgs);
                imagesLoad.addView(view);
            }
        }

        public void setTime(String date) {

            postDate = mView.findViewById(R.id.date_load);
            postDate.setText(date);

        }

        public void setUserData(String name, String city){

            postUserName = mView.findViewById(R.id.username_load);
            postUserName.setText(name);

            postCity = mView.findViewById(R.id.city_load);
            postCity.setText(city);
        }
    }
}
