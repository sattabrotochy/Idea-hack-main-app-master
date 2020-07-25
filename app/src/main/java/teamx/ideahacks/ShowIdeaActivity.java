package teamx.ideahacks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ShowIdeaActivity extends AppCompatActivity {



    ImageView imageView;

    TextView title,des,create,username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_idea);
        imageView = findViewById(R.id.showimage);
        title = findViewById(R.id.showtitleppppp);
        des  = findViewById(R.id.showdes);
        create = findViewById(R.id.showcreateat);
        username = findViewById(R.id.showusername);


        Intent intent = getIntent();

        String tlt = intent.getStringExtra("title");
        String img = intent.getStringExtra("img");
        String ds = intent.getStringExtra("des");
        String crt = intent.getStringExtra("create");
        String us = intent.getStringExtra("userid");


        if (img.equals("No Image")){

            imageView.setImageResource(R.drawable.images);
        }
        else
        {
            Picasso.get().load(img).into(imageView);
        }

        title.setText(tlt);
        des.setText(ds);
        create.setText(crt);


        FirebaseFirestore.getInstance().collection("userinfo").document(us)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    DocumentSnapshot document = task.getResult();
                    String value = document.getString("username");
                    username.setText(value);
                }

            }
        });




    }
}

