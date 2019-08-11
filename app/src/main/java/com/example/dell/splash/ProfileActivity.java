package com.example.dell.splash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE =101 ;
    ImageView propic;
    TextView verify;
    EditText name;
    Button save;
    ProgressBar progressBar;
    Uri uriProfileImage;
    String profileImageUrl;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        propic=(ImageView)findViewById(R.id.imageView);
        name=(EditText)findViewById(R.id.editText5);
        verify=(TextView)findViewById(R.id.verification);
        save=(Button)findViewById(R.id.button7);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        mAuth=FirebaseAuth.getInstance();

        propic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showImagechooser();
            }
        });

        loadUserInformation();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   saveUserInformation();
                   finish();

                   Intent intent=new Intent(ProfileActivity.this,MainPage.class);
                   startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(this,Login.class));
        }
    }

    private void loadUserInformation(){
        final FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null)
        {
            if(user.getPhotoUrl()!=null)
            {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(propic);
            }
            if(user.getDisplayName()!=null)
            {
                name.setText(user.getDisplayName());
            }
            if(user.isEmailVerified())
            {
                verify.setText("Email Verified");
            }
            else
            {
                verify.setText("Email Not Verified(Click to verify)");
                verify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(),"Verification Email Sent",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        }

    }

    private void saveUserInformation() {
        String displayName=name.getText().toString();
        if(displayName.isEmpty())
        {
            name.setError("Name required");
            name.requestFocus();
            return;
        }
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null && profileImageUrl!=null)
        {
            UserProfileChangeRequest profile=new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ProfileActivity.this,"Profile Updated",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            uriProfileImage=data.getData();
            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);
                propic.setImageBitmap(bitmap);
                uploadImageTofireBaseStorage();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageTofireBaseStorage() {
        final StorageReference profileImageRef= FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if(uriProfileImage!=null)
        {
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                      progressBar.setVisibility(View.GONE);
                                      profileImageUrl=uri.toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator=getMenuInflater();
        inflator.inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuLogout:
                                  FirebaseAuth.getInstance().signOut();
                                  finish();
                                  startActivity(new Intent(this,HomeActivity.class));
                                  break;
        }
        return true;
    }
*/
    private void showImagechooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"Select Profile Image"),CHOOSE_IMAGE);
    }
}
