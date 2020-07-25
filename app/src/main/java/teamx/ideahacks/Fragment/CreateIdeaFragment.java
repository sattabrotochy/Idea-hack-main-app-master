package teamx.ideahacks.Fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import teamx.ideahacks.MainActivity;
import teamx.ideahacks.Models.PostModel;
import teamx.ideahacks.Models.UserModel;
import teamx.ideahacks.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateIdeaFragment extends Fragment {


    public CreateIdeaFragment() {
        // Required empty public constructor
    }


    ImageView imageView;

    Button selectB, uploadB;
    EditText titleE, desE;


    private static final String TAG = "CreateIdeaFragment";
    private int STORAGE_PERMISSION_CODE = 1;

    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storageReference;
    private Uri muri;
    private StorageTask mUploadTask;
    private ProgressDialog progressDialog;
    private String ImageUri;

    private FirebaseAuth firebaseAuth;


    private  String title,des;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_idea, container, false);


        imageView = view.findViewById(R.id.showphotoID);
        selectB = view.findViewById(R.id.selectphotoId);
        uploadB = view.findViewById(R.id.UploadID);
        titleE = view.findViewById(R.id.ideaTitelID);
        desE = view.findViewById(R.id.ideaDesId);


        progressDialog = new ProgressDialog(getActivity());


        firebaseAuth = FirebaseAuth.getInstance();


        storageReference = FirebaseStorage.getInstance().getReference("ideas");

        selectB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);


                } else {
                    requestStoragePermission();

                }

            }
        });




        uploadB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = titleE.getText().toString().trim();
                des = desE.getText().toString().trim();
                if (title.isEmpty()){
                    titleE.setError("Title Required");
                }
                else  if (des.isEmpty()){
                    desE.setError("Description Required");
                }
                else {

                    photoupload();

                }
            }
        });



        return view;
    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);


                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {

            muri = data.getData();
            Picasso.get().load(muri).fit().centerCrop().into(imageView);
        }

    }


    private String getFileExtension(Uri uri) {
        ContentResolver resolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }



    private void photoupload() {

            progressDialog.setMessage("Please Wait...!");
            progressDialog.show();


        if (muri==null){
            ImageUri = "No Image";
            final   String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            PostModel postModel = new PostModel(title,des,ImageUri,userid,"erewrwerw","new",System.currentTimeMillis(),0,0,"true");

            FirebaseFirestore.getInstance().collection("Ideas").add(postModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    PostModel postModel2 = new PostModel(title,des,ImageUri,userid,task.getResult().getId(),"new",System.currentTimeMillis(),0,0,"true");

                    FirebaseFirestore.getInstance().collection("Ideas")
                            .document(task.getResult().getId()).set(postModel2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                progressDialog.dismiss();

                                Toast.makeText(getActivity(), "Upload Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();

                                FirebaseFirestore.getInstance().collection("userinfo").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        DocumentSnapshot doc = task.getResult();
                                        UserModel userModel = doc.toObject(UserModel.class);

                                        int t = userModel.getSubmit();

                                        int tt=t+1;

                                        FirebaseFirestore.getInstance().collection("userinfo").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("submit",tt);
                                    }
                                });



                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Upload Fail", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Upload Fail", Toast.LENGTH_SHORT).show();
                }
            });


        }else
        {

            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(muri));
            mUploadTask = fileReference.putFile(muri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {

                                    ImageUri = uri.toString();

                                  final   String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    PostModel postModel = new PostModel(title,des,ImageUri,userid,"erewrwerw","new",System.currentTimeMillis(),0,0,"true");

                                    FirebaseFirestore.getInstance().collection("Ideas").add(postModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            PostModel postModel2 = new PostModel(title,des,ImageUri,userid,task.getResult().getId(),"new",System.currentTimeMillis(),0,0,"true");

                                            FirebaseFirestore.getInstance().collection("Ideas")
                                                    .document(task.getResult().getId()).set(postModel2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()){


                                                        progressDialog.dismiss();


                                                        Toast.makeText(getActivity(), "Upload Successful", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getActivity(), MainActivity.class));
                                                        getActivity().finish();


                                                        FirebaseFirestore.getInstance().collection("userinfo").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                DocumentSnapshot doc = task.getResult();
                                                                UserModel userModel = doc.toObject(UserModel.class);

                                                               int t = userModel.getSubmit();

                                                               int tt=t+1;

                                                                FirebaseFirestore.getInstance().collection("userinfo").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                        .update("submit",tt);
                                                            }
                                                        });

                                                    }
                                                    else
                                                    {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getActivity(), "Upload Fail", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), "Upload Fail", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();

                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });



        }




    }




}


