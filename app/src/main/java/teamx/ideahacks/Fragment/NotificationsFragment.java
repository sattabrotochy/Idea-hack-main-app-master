package teamx.ideahacks.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import teamx.ideahacks.Adapters.NotificationAdapter;
import teamx.ideahacks.Models.NotificationModel;
import teamx.ideahacks.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment {


    public NotificationsFragment() {
        // Required empty public constructor
    }



    ListView listView;

    List<NotificationModel> listModelList=new ArrayList<>();






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view =inflater.inflate(R.layout.fragment_notifications, container, false);







        listView = view.findViewById(R.id.mainlistId);

        Query query = FirebaseFirestore.getInstance().collection("notification").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("notify");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){


                    listModelList.clear();
                    for (DocumentSnapshot doc : task.getResult()){

                        NotificationModel notificationModel = doc.toObject(NotificationModel.class);

                        listModelList.add(notificationModel);

                    }

                    NotificationAdapter adapter = new NotificationAdapter(getActivity(),listModelList);
                    listView.setAdapter(adapter);

                }

            }
        });





        return view;
    }

}
