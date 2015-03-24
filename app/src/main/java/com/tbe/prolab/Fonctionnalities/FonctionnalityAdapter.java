package com.tbe.prolab.Fonctionnalities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbe.prolab.R;

import java.util.List;

/**
 * Created by badetitou on 24/03/15.
 */
public class FonctionnalityAdapter extends RecyclerView.Adapter<FonctionnalityAdapter.ViewHolder>{
    private int[] fonctionalityNumber;
    private String[] name;
    private String[] description;

    // Provide a suitable constructor (depends on the kind of dataset)
    public FonctionnalityAdapter(String[] name, String[] description, int[] fonctionalityNumber) {
        this.name = name;
        this.description = description;
        this.fonctionalityNumber = fonctionalityNumber;
    }

    public int getFonctionnalityNumber(int position) {
        return fonctionalityNumber[position];
    }
    public String getFonctionnalityName(int position) {
        return name[position];
    }
    public String getFonctionnalityDescription(int position) {
        return description[position];
    }

    public void setData(List<String> name, List<String> description, List<Integer> fonctionalityNumber) {
        this.name = new String[name.size()];
        this.description = new String[description.size()];
        this.fonctionalityNumber = new int[fonctionalityNumber.size()];
        for (int i = 0; i < name.size(); ++i) {
            this.name[i] = name.get(i);
            this.description[i] = description.get(i);
            this.fonctionalityNumber[i] = fonctionalityNumber.get(i);
        }
        this.notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FonctionnalityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fonctionnality_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.name.setText(name[i]);
        viewHolder.description.setText(description[i]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return name.length;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public TextView description;

        public ViewHolder(LinearLayout ll) {
            super(ll);
            name = (TextView) ll.findViewById(R.id.fonctionnality_item_name);
            description = (TextView) ll.findViewById(R.id.fonctionnality_item_description);
        }
    }
}
