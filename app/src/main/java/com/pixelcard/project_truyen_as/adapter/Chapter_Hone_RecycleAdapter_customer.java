package com.pixelcard.project_truyen_as.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.model.Chapter;

import java.util.List;

public class Chapter_Hone_RecycleAdapter_customer extends RecyclerView.Adapter<Chapter_Hone_RecycleAdapter_customer.ChapterViewHolder> {
    private List<Chapter> chapterList;

    public Chapter_Hone_RecycleAdapter_customer(List<Chapter> chapterList) {
        this.chapterList = chapterList;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);
        holder.txtChapterName.setText("Chương " + chapter.getChapterName());
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView txtChapterName;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtChapterName = itemView.findViewById(R.id.txtChapterName);
        }
    }
}
