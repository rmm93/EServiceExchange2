package com.example.android.eserviceexchange.MainActivitiesPackage;


import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.eserviceexchange.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NewPostActivity extends AppCompatActivity {
    private Toolbar newPostToolbar;
    //for choosing images from the gallery
    private List<Uri> imgsUri;
    private FloatingActionButton selectPhotosbtn;
    int PICK_IMAGE_MULTIPLE = 1;
    private String imageEncoded;
    private List<String> imagesEncodedList;
    private GridView gvGallery;
    private GalleryAdapter galleryAdapter;
    private ArrayList<Uri> mArrayUri;
    //for uploading the info
    private EditText postDescrpt;
    private Button uploadPost;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private String loggedUserId;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        firebaseAuth = FirebaseAuth.getInstance();
        loggedUserId= firebaseAuth.getCurrentUser().getUid();
        storageReference= FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        postDescrpt= (EditText) findViewById(R.id.servicedescription);
        uploadPost= (Button) findViewById(R.id.uploadbutton);
        selectPhotosbtn = findViewById(R.id.selectPhotosBtn);
        gvGallery = (GridView)findViewById(R.id.gv);
        imagesEncodedList = new ArrayList<String>();
        imgsUri= new ArrayList<Uri>();
        newPostToolbar = findViewById(R.id.newposttoolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setTitle("Add New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        selectPhotosbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });

        uploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String describe= postDescrpt.getText().toString();
                String randomNameimg, randomNameFolder;
                StorageReference filepath, folderPath=null;
                if(!TextUtils.isEmpty(describe)){
                    uploadPost.setEnabled(false);
                    Map<String, Object> postMap = new HashMap<>();
                    postMap.put("desc", describe);
                    postMap.put("user_id", loggedUserId);
                    postMap.put("timestamp", FieldValue.serverTimestamp());
                    postMap.put("number_of_images", imgsUri.size());
                    if(imgsUri.size()>0){
                        randomNameFolder = UUID.randomUUID().toString();
                        for(int i=0;i<imgsUri.size();i++) {
                            randomNameimg = UUID.randomUUID().toString();
                            folderPath= storageReference.child("post_images"+randomNameFolder);
                            filepath = storageReference.child("post_images"+randomNameFolder).child(loggedUserId + "_" + randomNameimg + ".jpg");
                            filepath.putFile(imgsUri.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if(task.isSuccessful()){
                                        String downUri= task.getResult().getMetadata().getReference().getDownloadUrl().toString();
                                        Toast.makeText(NewPostActivity.this, "Image stored successfully", Toast.LENGTH_LONG).show();
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(NewPostActivity.this, "Storage Error: " + error, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        postMap.put("folder_path", folderPath.toString());
                    }


                    firestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {

                            if(task.isSuccessful()){

                                Toast.makeText(NewPostActivity.this, "Post was added", Toast.LENGTH_LONG).show();
                                Intent mainIntent = new Intent(NewPostActivity.this, MainActivity_for_Logged_Users.class);
                                startActivity(mainIntent);
                                finish();

                            } else {
                                uploadPost.setEnabled(true);
                                String error = task.getException().getMessage();
                                Toast.makeText(NewPostActivity.this, "DB Error: " + error, Toast.LENGTH_LONG).show();

                            }

                            //newPostProgress.setVisibility(View.INVISIBLE);

                        }
                    });
                }else {Toast.makeText(NewPostActivity.this,"Please fill all required fields",Toast.LENGTH_LONG).show();}
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();

                    mArrayUri = new ArrayList<Uri>();
                    mArrayUri.add(mImageUri);
                    imgsUri.add(mImageUri);
                    galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            imgsUri.add(uri);
                            cursor.close();
                            galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                            gvGallery.setAdapter(galleryAdapter);
                            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                    .getLayoutParams();
                            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

