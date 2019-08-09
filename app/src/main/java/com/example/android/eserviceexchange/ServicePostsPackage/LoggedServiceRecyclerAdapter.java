//trying to get it right part 2 T_T



package com.example.android.eserviceexchange.ServicePostsPackage;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.eserviceexchange.CommentsPackage.CommentsActivity;
import com.example.android.eserviceexchange.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LoggedServiceRecyclerAdapter extends RecyclerView.Adapter<LoggedServiceRecyclerAdapter.ViewHolder> {

    public List<ServicePost> service_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public LoggedServiceRecyclerAdapter(List<ServicePost> service_list){

        this.service_list = service_list;

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
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        final String blogPostId = service_list.get(position).ServicePostID;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();
        final String cmmntPublisherId = service_list.get(position).user_id;

        String desc_data = service_list.get(position).getDesc();
        holder.setDescText(desc_data);

        String user_id = service_list.get(position).getUser_id();
        //User Data will be retrieved here...
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
            long millisecond = service_list.get(position).getTimestamp().getTime();
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
            holder.setTime(dateString);
        } catch (Exception e) {

            Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        holder.postSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent commentIntent = new Intent(context, CommentsActivity.class);
                commentIntent.putExtra("blog_post_id", blogPostId);
                context.startActivity(commentIntent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return service_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView descView;
        private TextView blogDate;

        private TextView postUserName;
        private TextView postCity;

        private ImageView postSendBtn;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            postSendBtn = mView.findViewById(R.id.comment_icon);

        }

        public void setDescText(String descText){

            descView = mView.findViewById(R.id.desc_load);
            descView.setText(descText);

        }

        public void setTime(String date) {

            blogDate = mView.findViewById(R.id.date_load);
            blogDate.setText(date);

        }

        public void setUserData(String name, String city){

            postUserName = mView.findViewById(R.id.username_load);
            postUserName.setText(name);

            postCity = mView.findViewById(R.id.city_load);
            postCity.setText(city);
        }

    }

}

