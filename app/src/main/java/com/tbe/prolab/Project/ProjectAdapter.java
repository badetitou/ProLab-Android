package com.tbe.prolab.Project;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbe.prolab.R;

import java.util.List;

/**
 * Created by badetitou on 14/03/15.
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private int[] projectNumber;
    private String[] titles;
    private String[] punchlines;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProjectAdapter(String[] titles, String[] punchlines, int[] projectNumber) {
        this.titles = titles;
        this.punchlines = punchlines;
        this.projectNumber = projectNumber;
    }

    public int getProjectNumber(int position) {
        return projectNumber[position];
    }

    public void setData(List<String> titles, List<String> punchlines, List<Integer> projectNumber) {
        this.titles = new String[titles.size()];
        this.punchlines = new String[punchlines.size()];
        this.projectNumber = new int[projectNumber.size()];
        for (int i = 0; i < titles.size(); ++i) {
            this.titles[i] = titles.get(i);
            this.punchlines[i] = punchlines.get(i);
            this.projectNumber[i] = projectNumber.get(i);
        }
        this.notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.title.setText(titles[i]);
        viewHolder.punchline.setText(punchlines[i]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return titles.length;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView punchline;

        public ViewHolder(LinearLayout ll) {
            super(ll);
            title = (TextView) ll.findViewById(R.id.project_item_title);
            punchline = (TextView) ll.findViewById(R.id.project_item_punchline);

        }
    }

}
