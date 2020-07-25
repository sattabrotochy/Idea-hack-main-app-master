package teamx.ideahacks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {


    private static int RC_SIGN_IN = 1;

    private FirebaseAuth mAuth;


    private TextView textView;
    private Button button;
    MaterialEditText email, pass;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        getSupportActionBar().hide();

        textView = findViewById(R.id.texttttt);

        email = findViewById(R.id.loginemailId);
        pass = findViewById(R.id.loginPassId);
        button = findViewById(R.id.loginId);


        progressDialog = new ProgressDialog(this);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                Animatoo.animateSwipeLeft(LoginActivity.this);
            }
        });

        findViewById(R.id.forgetPASS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startActivity(new Intent(LoginActivity.this,ForgetPassActivity.class));


            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email = email.getText().toString().trim();
                final String Pass = pass.getText().toString().trim();

                if (Email.isEmpty()) {
                    email.setError("Email Required");
                } else if (Pass.isEmpty()) {
                    pass.setError("Password Required");
                } else {
                    progressDialog.setMessage("please wait...");
                    progressDialog.show();

                    firebaseAuth = FirebaseAuth.getInstance();

                    firebaseAuth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                Animatoo.animateFade(LoginActivity.this);
                                finish();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });


        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

    }
}

