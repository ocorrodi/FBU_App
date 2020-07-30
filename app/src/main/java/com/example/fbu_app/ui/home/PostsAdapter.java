package com.example.fbu_app.ui.home;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fbu_app.Post;
import com.example.fbu_app.R;
import com.parse.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private final List<Post> posts;
    FragmentManager manager;
    Context context;

    public PostsAdapter(List<Post> posts, FragmentManager manager, Context context) {
        this.posts = posts;
        this.manager = manager;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Post newPost = posts.get(position);
        holder.tvTitle.setText(newPost.getTitle());

        Glide.with(context).load(newPost.getUser().getString("profileImageUri")).apply(RequestOptions.circleCropTransform()).into(holder.ivImage);

        String place = newPost.getPlace();
        if (place != null) {
            holder.tvLocation.setText(place);
            return;
        }
        holder.tvLocation.setText(getAddress(newPost.getLocation().getLatitude(), newPost.getLocation().getLongitude()));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View mView;
        public TextView tvTitle;
        public TextView tvLocation;
        public ImageView ivImage;
        public Post post;


        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.tvTitle = view.findViewById(R.id.tvTitle);
            this.tvLocation = view.findViewById(R.id.tvAddress);
            this.ivImage = view.findViewById(R.id.ivImage);
            view.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString();
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            post = posts.get(position);
            PostDetailDialogFragment newFrag = PostDetailDialogFragment.newInstance(post);
            newFrag.show(manager, "fragment_post_detail");
        }
    }

    //returns formatted address string from latitude and longitude of a place
    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = "";
            String locality = obj.getLocality();
            String admin = obj.getAdminArea();
            String country = obj.getCountryName();
            if (locality != null) add += locality + ", ";
            if (admin != null) add += admin + ", ";
            if (country != null) add += country;
            return add;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return "";
        }
    }
}