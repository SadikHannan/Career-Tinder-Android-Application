package com.softwaregiants.careertinder.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.preferences.PreferenceManager;
import com.softwaregiants.careertinder.utilities.Constants;
import com.softwaregiants.careertinder.utilities.UtilityMethods;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ImagePickerActivity extends BaseActivity {

    private static final String IMAGE_DIRECTORY = "/career_tinder";
    protected int GALLERY = 1;
    protected int CAMERA = 2;
    final String EMPTY_STRING = "";
    ImageView imageUser;
    Button updateImageButton;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String fileName;
    String userType;
    Boolean imageSelected = false;
    Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userType = PreferenceManager.getInstance(getApplicationContext())
                .getString(Constants.PK_USER_TYPE,"");
        fileName = PreferenceManager.getInstance(getApplicationContext())
                .getString(Constants.PK_AUTH_CODE,"");
        if (userType.equals(Constants.USER_TYPE_EMPLOYER)) {
            fileName += UtilityMethods.getEpochTime();
        }
    }

    protected void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.getDeniedPermissionResponses() != null && !report.getDeniedPermissionResponses().isEmpty()) {
                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings
                                openSettingsDialog();
                            } else {
                                openReRequestDialog();
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void openReRequestDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Permission denied :(");
        alertDialog.setMessage("Sorry, but you must allow the use of these permissions to create your profile.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestMultiplePermissions();
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
    }

    private void openSettingsDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Permission permanently denied :(");
        alertDialog.setMessage("Unfortunately, you have permanently denied one of the permissions without which we cannot proceed any further. You must go into Application settings and grant these permissions to continue.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                finish();
            }
        });
        alertDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    protected void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    View.OnClickListener updateImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showPictureDialog();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imageSelected = true;
            if (requestCode == GALLERY) {
                if (data != null && data.getData() != null) {
                    Uri contentURI = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        imageUser.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                        imageUser.setImageDrawable( UtilityMethods.getDrawable(mContext,R.drawable.placeholder) );
                        imageSelected = false;
                        Toast.makeText(mContext, "Failed to select image!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    imageUser.setImageDrawable( UtilityMethods.getDrawable(mContext,R.drawable.placeholder) );
                    imageSelected = false;
                    Toast.makeText(mContext, "Failed to select image!", Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == CAMERA) {
                bitmap = (Bitmap) data.getExtras().get("data");
                imageUser.setImageBitmap(bitmap);
            }
        }
    }

    private void deleteExistingFile() {
        StorageReference storageRef = storage.getReference(fileName);
        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext, "Deleted existing image.", Toast.LENGTH_SHORT).show();
//                uploadImageFile();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Deleted existing image failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    protected void uploadImageFile(Bitmap bitmap, OnSuccessListener osl, OnFailureListener ofl) {
        StorageReference storageRef = storage.getReference(fileName);

        // Get the data from an ImageView as bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        Toast.makeText(mContext, "Uploading image.", Toast.LENGTH_SHORT).show();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(ofl).addOnSuccessListener(osl);
    }

    protected void downloadImageFile(String fileName) {
        StorageReference storageRef = storage.getReference(fileName);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit()
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder).into(imageUser);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
