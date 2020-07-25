package teamx.ideahacks.Adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import teamx.ideahacks.Models.PostModel;

import teamx.ideahacks.R;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    private static final String TAG = "PostAdapter";

    Context context;
    List<PostModel>postModelList;


    public BookAdapter(Context context, List<PostModel> postModelList) {
        this.context = context;
        this.postModelList = postModelList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final PostModel postModel = postModelList.get(position);



        holder.createat.setText(TimeAgo.from(postModel.getCreate_at()));


        if (postModel.getImgaeurl().equals("No Image")){

            holder.image.setImageResource(R.drawable.images);
        }
        else {

            Picasso.get().load(postModel.getImgaeurl()).into(holder.image);
        }
        holder.title.setText(postModel.getTitle());
        holder.des.setText(postModel.getDescription());


    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }





    private OnItemClickListener mListener;

    public interface OnItemClickListener {

        void onItemClick(View view, int position, PostModel postModel);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }




    public  class  MyViewHolder  extends RecyclerView.ViewHolder {



        ImageView bookmark;
        TextView createat;
        ImageView image;
        TextView title,des;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            bookmark = itemView.findViewById(R.id.bookbutton);

            createat = itemView.findViewById(R.id.bookcreate);
            image = itemView.findViewById(R.id.bookimage);
            title = itemView.findViewById(R.id.booktitle);
            des = itemView.findViewById(R.id.bookdes);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(view, position, postModelList.get(getAdapterPosition()));
                        }
                    }

                }
            });

        }
    }



    public void setfilter(List<PostModel> itemData) {
        postModelList = new ArrayList<>();
        postModelList.addAll(itemData);
        notifyDataSetChanged();

    }





}
