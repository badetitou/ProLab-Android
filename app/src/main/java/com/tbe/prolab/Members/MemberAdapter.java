package com.tbe.prolab.Members;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbe.prolab.PopUp.RemoveMember;
import com.tbe.prolab.R;
import com.tbe.prolab.Users.InfoUser;

import java.util.List;

/**
 * Created by badetitou on 23/03/15.
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder>{
    private String[] usernames;
    private int[] idMembers;
    private int[] idFonctionnalities = new int[0];
    protected static Context context;
    protected static FragmentManager fragmentManager;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MemberAdapter(String[] usernames, int[] idMembers, Context context, int[] idFonctionnalities, FragmentManager fragmentManager) {
        this.usernames = usernames;
        this.idMembers = idMembers;
        this.idFonctionnalities = idFonctionnalities;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    public void setContext(Context context1, FragmentManager fragmentManager1){
        context = context1;
        fragmentManager = fragmentManager1;
    }


    public void setData(List<String> username, List<Integer> idMembers, int idFonctionnalities) {
        this.usernames = new String[username.size()];
        this.idMembers = new int[idMembers.size()];
        this.idFonctionnalities = new int[username.size()];
        for (int i = 0; i < username.size(); ++i) {
            this.usernames[i] = username.get(i);
            this.idMembers[i] = idMembers.get(i);
            this.idFonctionnalities[i] = idFonctionnalities;
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
        viewHolder.idMember = idMembers[i];
        viewHolder.idFonctionnality = idFonctionnalities[i];
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
        public int idMember;
        public int idFonctionnality = 0;

        public ViewHolder(LinearLayout ll) {
            super(ll);
            username = (TextView) ll.findViewById(R.id.member_item_username);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), InfoUser.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username.getText().toString());
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
            ll.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new RemoveMember(context,idMember, username.getText().toString(),idFonctionnality).show(fragmentManager,"removeMember");
                    return true;
                }
            });
        }
    }

}
