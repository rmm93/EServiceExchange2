package com.example.android.eserviceexchange.MainActivitiesPackage;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.eserviceexchange.R;
import com.example.android.eserviceexchange.ServicePostsPackage.ServicePost;
import com.example.android.eserviceexchange.ServicePostsPackage.ServicePostRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private DocumentSnapshot lastVisible;
    private RecyclerView postListView;
    private List<ServicePost> postList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    ServicePostRecyclerAdapter postRecyclerAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_home, container, false);
        //create list of posts that retrieve the data from firebase
        postList = new ArrayList<>();
        postListView = mainView.findViewById(R.id.post_list);
        postRecyclerAdapter = new ServicePostRecyclerAdapter(postList);
        postListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postListView.setAdapter(postRecyclerAdapter);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            ServicePost servicePost = doc.getDocument().toObject(ServicePost.class);
                            postList.add(servicePost);
                            postRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });


        // Inflate the layout for this fragment
        return mainView;
    }

}
