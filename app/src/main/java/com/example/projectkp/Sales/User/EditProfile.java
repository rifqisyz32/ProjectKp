package com.example.projectkp.Sales.User;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.projectkp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class EditProfile extends AppCompatActivity {

    FirebaseAuth editAuth = FirebaseAuth.getInstance();
    FirebaseUser editUser = editAuth.getCurrentUser();
    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    DatabaseReference reference = rootNode.getReference("users");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("sales_photo");
    StorageTask imgTask;

    Toolbar toolbar;
    ProgressBar saveDataProgress;
    TextInputLayout editFullname, editPhone, editEmail;
    TextView usernameSales;
    ImageView userPhoto, updatePhoto;
    Uri userPhotoUri;
    Boolean updateImgButton = false;
    String myUsername, myPassword, databaseEmail, databaseFullname, databasePhone;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String sharedUsername = "username";
    public static final String sharedPassword = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_sales);

        storeId();
        getUserData();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        updatePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImgButton = true;
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }
            }
        });

        findViewById(R.id.cancel_edit_sales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.save_profile_sales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataProgress.setVisibility(View.VISIBLE);
                if (updateImgButton) {
                    if (imgTask != null && imgTask.isInProgress()) {
                        Toast.makeText(getApplicationContext(), R.string.wait, Toast.LENGTH_SHORT).show();
                    } else {
                        storePhotoDatabase(userPhotoUri, editUser);
                    }
                }
                storeUserData();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), UserDetail.class));
        finish();
    }

    private void storeId() {
        toolbar = findViewById(R.id.edit_profile_toolbar_sales);
        saveDataProgress = findViewById(R.id.save_profile_sales_prog);
        userPhoto = findViewById(R.id.edit_user_photo_sales);
        updatePhoto = findViewById(R.id.edit_change_img_sales);
        usernameSales = findViewById(R.id.edit_username_sales);
        editFullname = findViewById(R.id.edit_fullname_sales);
        editPhone = findViewById(R.id.edit_phone_sales);
        editEmail = findViewById(R.id.edit_email_sales);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    private void getUserData() {

        if (editUser.getPhotoUrl() != null) {
            Glide.with(this).load(editUser.getPhotoUrl()).centerCrop().into(userPhoto);
        }

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (sharedPreferences.contains(sharedUsername)) {
            myUsername = sharedPreferences.getString(sharedUsername, "");
            usernameSales.setText(sharedPreferences.getString(sharedUsername, ""));
        }

        if (sharedPreferences.contains(sharedPassword)) {
            myPassword = sharedPreferences.getString(sharedPassword, "");
        }

        Query checkUser = reference.orderByChild("username").equalTo(myUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    databaseFullname = snapshot.child(myUsername).child("fullname").getValue(String.class);
                    databasePhone = snapshot.child(myUsername).child("phone").getValue(String.class);
                    databaseEmail = snapshot.child(myUsername).child("email").getValue(String.class);

                    editFullname.getEditText().setText(databaseFullname);
                    editFullname.getEditText().setHint(R.string.fullname);

                    editPhone.getEditText().setText(databasePhone);
                    editPhone.getEditText().setHint(R.string.phone);

                    editEmail.getEditText().setText(databaseEmail);
                    editEmail.getEditText().setHint(R.string.email);

                } else {
                    Toast.makeText(getApplicationContext(), R.string.no_user, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(EditProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(getApplicationContext(), R.string.acc_permission, Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        } else
            openGallery();
    }

    private void openGallery() {
        Intent gallerySales = new Intent((Intent.ACTION_GET_CONTENT));
        gallerySales.setType("image/*");
        startActivityForResult(gallerySales, REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {
            userPhotoUri = data.getData();
            Glide.with(this).load(userPhotoUri).centerCrop().into(userPhoto);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver fileExtension = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(fileExtension.getType(uri));
    }

    private void storePhotoDatabase(Uri userPhotoUri, FirebaseUser currentUser) {

        if (userPhotoUri != null) {
            StorageReference imgReference = storageReference.child(myUsername).child("user_photo");

            imgTask = imgReference.putFile(userPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(myUsername)
                                    .setPhotoUri(uri)
                                    .build();

                            currentUser.updateProfile(profileUpdate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(EditProfile.this, R.string.update_photo_error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), R.string.no_photo, Toast.LENGTH_SHORT).show();
        }
    }

    private void storeUserData() {

        String fullname = editFullname.getEditText().getText().toString();
        String phone = editPhone.getEditText().getText().toString();
        String email = editEmail.getEditText().getText().toString();

        if (phone.isEmpty()) {
            editPhone.getEditText().setText(databasePhone);
        }
        if (fullname.isEmpty()) {
            editFullname.getEditText().setText(databaseFullname);
        }

        fullname = editFullname.getEditText().getText().toString();
        phone = editPhone.getEditText().getText().toString();

        if (email.isEmpty()) {

            editEmail.getEditText().setText(databaseEmail);
            email = editEmail.getEditText().getText().toString();

            reference.child(myUsername).child("fullname").setValue(fullname);
            reference.child(myUsername).child("phone").setValue(phone);
            reference.child(myUsername).child("email").setValue(email);

            saveDataProgress.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), R.string.edit_success, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), UserDetail.class));
            finish();

        } else {
            if (!validateEmail()) {
                return;
            }
            updateEmail(fullname, email, phone);
        }
    }

    private void updateEmail(String fullname, String email, String phone) {

        AuthCredential credential = EmailAuthProvider.getCredential(databaseEmail, myPassword);
        editUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                editUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            reference.child(myUsername).child("fullname").setValue(fullname);
                            reference.child(myUsername).child("phone").setValue(phone);
                            reference.child(myUsername).child("email").setValue(email);

                            saveDataProgress.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), R.string.edit_success, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), UserDetail.class));
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                saveDataProgress.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validateEmail() {
        String val = editEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!val.matches(emailPattern)) {
            editEmail.setError(getString(R.string.invalid_email));
            return false;
        } else {
            editEmail.setError(null);
            editEmail.setErrorEnabled(false);
            return true;
        }
    }
}