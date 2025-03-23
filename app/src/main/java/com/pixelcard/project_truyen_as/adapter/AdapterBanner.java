package com.pixelcard.project_truyen_as.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pixelcard.project_truyen_as.R;


public class AdapterBanner extends RecyclerView.Adapter<AdapterBanner.BannerViewHolder> {
    private final Context context;
    private final int[] bannerImages;
    public AdapterBanner(Context context, int[] bannerImages) {
        this.context = context;
        this.bannerImages = bannerImages;
    }
    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false);
        return new BannerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(BannerViewHolder holder, int position) {
        holder.imageView.setImageResource(bannerImages[position]);
    }

    @Override
    public int getItemCount() {
        return bannerImages.length;
    }
    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public BannerViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.banner_img);
        }
    }
}