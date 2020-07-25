package teamx.ideahacks.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import teamx.ideahacks.Adapters.BookAdapter;
import teamx.ideahacks.Adapters.PostAdapter;
import teamx.ideahacks.Models.PostModel;
import teamx.ideahacks.Models.UserModel;
import teamx.ideahacks.R;
import teamx.ideahacks.ShowIdeaActivity;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }



    TextView textView ;
    RecyclerView recyclerView,recyclerView2;

    List<PostModel> postModelList=new ArrayList<>();
    private PostAdapter adapter;

    private CollectionReference reference;
    private Query query1;

    ProgressBar progressBar , progressBar2;


    List<PostModel> booklist;

    private  TextView Id,Submit,Givevote;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        textView = view.findViewById(R.id.userNameIDpro);

        recyclerView = view.findViewById(R.id.proRecyId);
        recyclerView2 = view.findViewById(R.id.bookmarkrecyID);

        progressBar  =view.findViewById(R.id.progress_circular2);
        progressBar2  =view.findViewById(R.id.progress_circular44);

        Id = view.findViewById(R.id.profileID);
        Submit = view.findViewById(R.id.profileIdeasubmit);
        Givevote = view.findViewById(R.id.profiletotalvote);



        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        reference = firestore.collection("Ideas");


       final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();





        FirebaseFirestore.getInstance().collection("userinfo").document(user)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();
                String value = document.getString("username");
                String id = document.getString("university_id");
                textView.setText(value);
                Id.setText(id);

                DocumentSnapshot doc = task.getResult();
                UserModel userModel = doc.toObject(UserModel.class);

                int sub = userModel.getSubmit();
                int vote = userModel.getGiveVote();

                Submit.setText(String.valueOf(sub));
                Givevote.setText(String.valueOf(vote));

            }
        });



        FirebaseFirestore.getInstance().collection("BookMarks").document(user)
                .collection("posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if (task.isSuccessful()){

                    booklist = new ArrayList<>();
                   booklist .clear();
                    for (DocumentSnapshot doc : task.getResult()){
                        PostModel post = doc.toObject(PostModel.class);
                        booklist.add(post);
                    }


                    BookAdapter bookAdapter = new BookAdapter(getActivity(),booklist);


                    bookAdapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, PostModel postModel) {

                            Intent intent = new Intent(getActivity(), ShowIdeaActivity.class);
                            intent.putExtra("img",postModel.getImgaeurl());
                            intent.putExtra("title",postModel.getTitle());
                            intent.putExtra("des",postModel.getDescription());
                            intent.putExtra("create",postModel.getCreate_at());
                            intent.putExtra("userid",postModel.getUserID());
                            startActivity(intent);


                        }
                    });



                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                    recyclerView2.setLayoutManager(linearLayoutManager);
                    recyclerView2.setHasFixedSize(true);

                    recyclerView2.setAdapter(bookAdapter);


                }


            }
        });





        query1 = reference.whereEqualTo("userID",user);

        progressBar.setVisibility(View.VISIBLE);
        query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){

                    postModelList.clear();
                    for (DocumentSnapshot doc : task.getResult()){
                        PostModel post = doc.toObject(PostModel.class);
                        postModelList.add(post);
                    }
                    adapter = new PostAdapter(getActivity(),postModelList);


                    adapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, PostModel postModel) {

                            Intent intent = new Intent(getActivity(), ShowIdeaActivity.class);
                            intent.putExtra("img",postModel.getImgaeurl());
                            intent.putExtra("title",postModel.getTitle());
                            intent.putExtra("des",postModel.getDescription());
                            intent.putExtra("create",postModel.getCreate_at());
                            intent.putExtra("userid",postModel.getUserID());
                            startActivity(intent);


                            Log.d(TAG, "onItemClick: "+postModel.getTitle());

                        }
                    });



                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setHasFixedSize(true);

                    progressBar.setVisibility(View.GONE);
                    recyclerView.setAdapter(adapter);

                }

            }
        });







        return view;
    }

}

