package teamx.ideahacks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

import teamx.ideahacks.Models.UserModel;


public class SignupActivity extends AppCompatActivity {



    private static final String TAG = "SignupActivity";

    private MaterialEditText Eusername, Eemail, Epass,universityId;
    private Button button;

    private FirebaseAuth firebaseAuth;
    private CollectionReference collectionReference;
    private FirebaseUser user;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();


        findViewById(R.id.backloginpage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        collectionReference = FirebaseFirestore.getInstance().collection("userinfo");


        Eusername = findViewById(R.id.usernameId);
        Eemail = findViewById(R.id.emailId);
        Epass = findViewById(R.id.passwordId);
        button = findViewById(R.id.signupId);
        universityId = findViewById(R.id.UniuserId);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String username = Eusername.getText().toString().trim();
                final String email = Eemail.getText().toString().trim();
                final String pass = Epass.getText().toString().trim();
                final String university_id = universityId.getText().toString().trim();

                if (username.isEmpty()) {

                    Eusername.setError("Username is required");
                } else if (email.isEmpty()) {

                    Eemail.setError("Email is required");
                } else if (pass.isEmpty()) {

                    Epass.setError("Password is required");
                }
                else if (university_id.isEmpty()) {

                    Epass.setError("Id is required");
                }
                else {
                    progressDialog.setMessage("please wait...");
                    progressDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser us = FirebaseAuth.getInstance().getCurrentUser();

                                UserModel userModel = new UserModel(username,university_id,email ,us.getUid(),null,System.currentTimeMillis(),0,0);

                                collectionReference.document(us.getUid()).set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                            finish();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } else {
                                progressDialog.dismiss();

                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(SignupActivity.this, "You are already Registered ", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });

                }

            }
        });

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(this); //fire the slide left animation
    }


}

