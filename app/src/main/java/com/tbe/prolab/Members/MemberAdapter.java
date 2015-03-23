package com.tbe.prolab.Members;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbe.prolab.R;

import java.util.List;

/**
 * Created by badetitou on 23/03/15.
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder>{
    private String[] usernames;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MemberAdapter(String[] usernames) {
        this.usernames = usernames;
    }


    public void setData(List<String> username) {
        this.usernames = new String[username.size()];
        for (int i = 0; i < username.size(); ++i) {
            this.usernames[i] = username.get(i);
        }
        this.notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.username.setText(usernames[i]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return usernames.length;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView username;

        public ViewHolder(LinearLayout ll) {
            super(ll);
            username = (TextView) ll.findViewById(R.id.member_item_username);

        }
    }

}
