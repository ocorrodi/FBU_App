package com.example.fbu_app.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbu_app.MainActivity;
import com.example.fbu_app.R;
import com.example.fbu_app.login.FBLoginActivity;
import com.example.fbu_app.login.LoginActivity;
import com.example.fbu_app.ui.home.PostListFragment;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.parse.ParseUser;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    final String[] titles = {"My Profile", "My Posts", "Liked Posts", "Logout", "Settings", "Facebook Logout"};
    final int[] icons = {R.drawable.profile_white, R.drawable.myposts_white, R.drawable.likedposts_white,
            R.drawable.exit_white, R.drawable.settings_white, R.drawable.com_facebook_button_icon};
    final String[] colors = {"#F94144", "#F3722C", "#F9C74F", "#90BE6D", "#43AA8B", "#577590"};
    final int numItems = 6;
    Context context;
    FragmentManager fragmentManager;

    public ProfileAdapter(Context context, FragmentManager manager) {
        this.context = context;
        this.fragmentManager = manager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_profile_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mtvTitle.setText(titles[position]);
        holder.icon.setImageResource(icons[position]);
        holder.card.setCardBackgroundColor(Color.parseColor(colors[position]));
        //holder.card.setCardBackgroundColor(Color.parseColor("#F94144"));
    }

    @Override
    public int getItemCount() {
        return numItems;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        MaterialTextView mtvTitle;
        ImageView icon;
        MaterialCardView card;

        public ViewHolder(View view) {
            super(view);
            this.mtvTitle = view.findViewById(R.id.mtvTitle);
            this.icon = view.findViewById(R.id.ivIcon);
            this.card = view.findViewById(R.id.mcvCards);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (getAdapterPosition()) {
                case 0:
                    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new ProfileFragment()).addToBackStack(null).commit();
                    break;
                case 2:
                    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new GenericPostListFragment(PostProperty.LIKED)).addToBackStack(null).commit();
                    break;
                case 3:
                    logout();
                    break;
                case 5:
                    disconnectFromFacebook();
                    break;
                case 1:
                    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new GenericPostListFragment(PostProperty.PERSONAL)).addToBackStack(null).commit();
                    break;
                default:
                    break;
            }
        }
    }

    public void logout() {
        ParseUser.logOut();

        //logout from FB without going to FB login screen
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();

        goLogin();
    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                //LoginManager.getInstance().logOut();
                goFBLogin();

            }
        }).executeAsync();
    }

    public void goFBLogin() {
        Intent i = new Intent(this.context, FBLoginActivity.class);
        i.putExtra("loggedOut", 1);
        this.context.startActivity(i);
    }

    public void goLogin() {
        Intent i = new Intent(this.context, LoginActivity.class);
        i.putExtra("loggedOut", true);
        this.context.startActivity(i);
    }
}