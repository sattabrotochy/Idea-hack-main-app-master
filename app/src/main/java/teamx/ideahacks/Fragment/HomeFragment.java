package teamx.ideahacks.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import teamx.ideahacks.Adapters.PostAdapter;
import teamx.ideahacks.Models.PostModel;
import teamx.ideahacks.R;
import teamx.ideahacks.ShowIdeaActivity;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    ProgressBar progressBar ;
    RecyclerView recyclerView;

    List<PostModel>postModelList=new ArrayList<>();
    private PostAdapter adapter;

    private CollectionReference reference;
    private Query query1;

    private EditText editText;

    private Spinner spinner;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recypostID);
        progressBar = view.findViewById(R.id.recyprogragbarId);
        spinner = view.findViewById(R.id.spinnerId);


        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        reference = firestore.collection("Ideas");


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = spinner.getItemAtPosition(i).toString();

                if (s.equals("All")){
                    query1 = reference.orderBy("create_at", Query.Direction.DESCENDING);
                    setRecycler();

                }
                if (s.equals("new")){
                    query1  =  reference.whereEqualTo("status",s);
                    setRecycler();
                }
                if (s.equals("in-progress")){
                    query1  =  reference.whereEqualTo("status",s);
                    setRecycler();
                }
                if (s.equals("completed")){
                    query1  =  reference.whereEqualTo("status",s);
                    setRecycler();
                }
                if (s.equals("on hold")){
                    query1  =  reference.whereEqualTo("status",s);
                    setRecycler();
                }
                if (s.equals("upvote")){
                    query1  =  reference.orderBy("upvote", Query.Direction.DESCENDING);
                    setRecycler();
                }
                if (s.equals("downvote")){
                    query1  =  reference.orderBy("downvote", Query.Direction.DESCENDING);
                    setRecycler();
                }

                Log.d(TAG, "onItemSelected: " +s);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        editText = view.findViewById(R.id.editsearchID);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                
                try {
                    String  newText = editText.getText().toString().trim();
                    List<PostModel> newList = filter(postModelList, newText);
                    Log.d(TAG, "onQueryTextChange: " + newList);
                    adapter.setfilter(newList);
                }
                catch (Exception e){
                    Log.d(TAG, "onTextChanged: ");
                }
                
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        return view;
    }

    private void setRecycler() {

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



    }


    private List<PostModel> filter(List<PostModel> pd, String query) {


        query = query.toLowerCase().trim();

        List<PostModel> filterProductData = new ArrayList<>();
        for (PostModel prodata : pd) {
            if (prodata!=null){
                final String text = prodata.getTitle().toLowerCase();
                if (text.contains(query)) {
                    filterProductData.add(prodata);
                }
            }
            else
            {
                Log.d(TAG, "filter: ");
            }

        }
        return filterProductData;
    }

}
