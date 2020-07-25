package teamx.ideahacks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import teamx.ideahacks.Adapters.ComentAdapter;
import teamx.ideahacks.Models.CommentModel;
import teamx.ideahacks.Models.NotificationModel;
import teamx.ideahacks.Models.PostModel;
import teamx.ideahacks.Models.UserModel;

public class CommentActivity extends AppCompatActivity {



    ImageView sendbutton;
    MaterialEditText sendtext;

    String id;
    private RecyclerView recyclerView;
    ArrayList<CommentModel> list=new ArrayList<>();

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        sendbutton = findViewById(R.id.sendbutton);
        sendtext = findViewById(R.id.sendtext);




        Intent intent = getIntent();


      id = intent.getStringExtra("id");


        final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();





        FirebaseFirestore.getInstance().collection("userinfo").document(user)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();
                String value = document.getString("username");
                username=value;

            }
        });




        showcomment();



        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = sendtext.getText().toString();

                if (!msg.isEmpty()){


                    CommentModel commentModel = new CommentModel(username,msg,System.currentTimeMillis());

                    FirebaseFirestore.getInstance().collection("Ideas").document(id)
                            .collection("comments").add(commentModel);

                    showcomment();

                }
                else
                {
                    Toast.makeText(CommentActivity.this, "You cant send empty message", Toast.LENGTH_SHORT).show();
                }
                sendtext.setText("");

            }
        });

    }

    private void showcomment() {


        FirebaseFirestore.getInstance().collection("Ideas").document(id)
                .collection("comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){


                    list.clear();

                    for (DocumentSnapshot doc : task.getResult()){
                        CommentModel commentModel = doc.toObject(CommentModel.class);
                        list.add(commentModel);
                    }


                    ComentAdapter comentAdapter = new ComentAdapter(CommentActivity.this,list);
                    recyclerView = findViewById(R.id.recychat);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
                    recyclerView.setAdapter(comentAdapter);


                }

            }
        });


    }


}

