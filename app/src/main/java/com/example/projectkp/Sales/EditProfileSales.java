package com.example.projectkp.Sales;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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
import com.bumptech.glide.request.RequestOptions;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditProfileSales extends AppCompatActivity {

    FirebaseAuth editAuth = FirebaseAuth.getInstance();
    FirebaseUser editUser = editAuth.getCurrentUser();
    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    DatabaseReference reference = rootNode.getReference("users");

    Toolbar toolbar;
    ProgressBar saveDataProgress;
    TextInputLayout editFullname, editPhone, editEmail;
    TextView usernameUser;
    ImageView userPhoto, updatePhoto;
    Uri userPhotoUri;
    Boolean updateImgButton = false;
    String myUsername, myFullname, myPhone, myEmail, myPassword;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String FullName = "fullname";
    public static final String UserName = "username";
    public static final String Phone = "phone";
    public static final String Email = "email";
    public static final String Password = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_sales);

        storeId();
        getUserDataPreferences();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserDetailSales.class));
                finish();
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
                startActivity(new Intent(getApplicationContext(), UserDetailSales.class));
                finish();
            }
        });

        findViewById(R.id.save_profile_sales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataProgress.setVisibility(View.VISIBLE);
                if (updateImgButton) {
                    storePhotoDatabase(userPhotoUri, editUser);
                }
                storeUserData();
            }
        });
    }

    private void storeId() {
        toolbar = findViewById(R.id.edit_profile_toolbar_sales);
        saveDataProgress = findViewById(R.id.save_profile_sales_prog);
        userPhoto = findViewById(R.id.edit_user_photo_sales);
        updatePhoto = findViewById(R.id.edit_change_img_sales);
        usernameUser = findViewById(R.id.edit_username_sales);
        editFullname = findViewById(R.id.edit_fullname_sales);
        editPhone = findViewById(R.id.edit_phone_sales);
        editEmail = findViewById(R.id.edit_email_sales);
    }

    private void getUserDataPreferences() {

        Glide.with(this)
                .applyDefaultRequestOptions(
                        new RequestOptions()
                                .placeholder(R.drawable.ic_baseline_account_circle_120)
                                .error(R.drawable.ic_baseline_account_circle_120))
                .load(editUser.getPhotoUrl())
                .centerCrop()
                .into(userPhoto);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(FullName)) {
            myFullname = sharedPreferences.getString(FullName, "");
            editFullname.getEditText().setHint(sharedPreferences.getString(FullName, ""));
        }
        if (sharedPreferences.contains(UserName)) {
            myUsername = sharedPreferences.getString(UserName, "");
            usernameUser.setText(sharedPreferences.getString(UserName, ""));
        }
        if (sharedPreferences.contains(Phone)) {
            myPhone = sharedPreferences.getString(Phone, "");
            editPhone.getEditText().setHint(sharedPreferences.getString(Phone, ""));
        }
        if (sharedPreferences.contains(Email)) {
            myEmail = sharedPreferences.getString(Email, "");
            editEmail.getEditText().setHint(sharedPreferences.getString(Email, ""));
        }
        if (sharedPreferences.contains(Password)) {
            myPassword = sharedPreferences.getString(Email, "");
        }
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(EditProfileSales.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileSales.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(getApplicationContext(), R.string.acc_permission, Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(EditProfileSales.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
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
            userPhoto.setImageURI(userPhotoUri);
        }
    }

    private void storePhotoDatabase(Uri userPhotoUri, FirebaseUser currentUser) {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("sales_photo").child(myUsername);
        StorageReference imgPath = storageReference.child(userPhotoUri.getLastPathSegment());

        imgPath.putFile(userPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                                            Toast.makeText(EditProfileSales.this, R.string.update_photo_error, Toast.LENGTH_SHORT).show();
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
        });
    }

    private void storeUserData() {

        String fullname = editFullname.getEditText().getText().toString();
        String phone = editPhone.getEditText().getText().toString();
        String email = editEmail.getEditText().getText().toString();

        saveDataProgress.setVisibility(View.GONE);
        if (email.isEmpty()) {
            if (validateData()) {
                return;
            }

            editEmail.getEditText().setText(myEmail);
            reference.child(myUsername).child("fullname").setValue(fullname);
            reference.child(myUsername).child("phone").setValue(phone);
            reference.child(myUsername).child("email").setValue(email);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(FullName, fullname);
            editor.putString(Phone, phone);
            editor.putString(Email, email);
            editor.apply();

            Toast.makeText(getApplicationContext(), R.string.edit_success, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), UserDetailSales.class));
            finish();

        } else {
            if (validateData()) {
                return;
            }
            updateEmail(fullname, email, phone);
        }
    }

    private void updateEmail(String fullname, String email, String phone) {

        AuthCredential credential = EmailAuthProvider.getCredential(myEmail, myPassword);
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

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(FullName, fullname);
                            editor.putString(Phone, phone);
                            editor.putString(Email, email);
                            editor.apply();

                            Toast.makeText(getApplicationContext(), R.string.edit_success, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), UserDetailSales.class));
                            finish();
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

    private Boolean validateData() {
        String checkFullname = editFullname.getEditText().getText().toString();
        String checkPhone = editPhone.getEditText().getText().toString();
        String checkEmail = editEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (checkFullname.isEmpty()) {
            editFullname.getEditText().setText(myFullname);
        } else if (checkPhone.isEmpty()) {
            editPhone.getEditText().setText(myPhone);
        } else if (!checkEmail.isEmpty()) {
            if (!checkEmail.matches(emailPattern)) {
                editEmail.setError(getString(R.string.invalid_email));
                return false;
            } else {
                editEmail.setError(null);
                editEmail.setErrorEnabled(false);
            }
        }
        return true;
    }
}