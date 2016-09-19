package com.gayatry.prelogin.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gayatry.R;
import com.gayatry.model.ProjectModel;
import com.gayatry.prelogin.ProjectFragment;

import java.util.ArrayList;
import java.util.List;

public class ProjectGridAdapter extends RecyclerView.Adapter<ProjectGridAdapter.TextViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    private int lastPosition = 0;

    private View header;
    private ArrayList<ProjectModel> arrayList;
    private ProjectFragment projectFragment;

    public ProjectGridAdapter(View header, ArrayList<ProjectModel> arrayList, ProjectFragment projectFragment) {
        if (header == null) {
            throw new IllegalArgumentException("header may not be null");
        }
        this.header = header;
        this.arrayList = arrayList;
        this.projectFragment = projectFragment;
    }

    public boolean isHeader(int position) {
        return position == 0;
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            return new TextViewHolder(header);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_project, parent, false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TextViewHolder holder, final int position) {
        if (isHeader(position)) {
            return;
        }
        //final String label = labels.get(position - 1);  // Subtract 1 for header
        holder.textView.setText(arrayList.get(position-1).getProjectName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectFragment.openMenu(arrayList.get(position-1));
            }
        });
        animate(holder.card_view, position);
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        private CardView card_view;

        public TextViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.txtTitle);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    private void animate(View view, final int pos) {
        if (pos > lastPosition) {
            view.animate().cancel();
            view.setTranslationY(100);
            view.setAlpha(0);
            view.animate().alpha(1.0f).translationY(0).setDuration(300).setStartDelay(100);
            lastPosition = pos;
        }
    }
}