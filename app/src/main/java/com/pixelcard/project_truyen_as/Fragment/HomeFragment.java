package com.pixelcard.project_truyen_as.Fragment;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.adapter.AdapterBanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeFragment extends Fragment {

    private ViewPager2 viewPagerTruyen;
    private AdapterBanner adapterBanner;
    private final Handler bannerHandler = new Handler();
    private int currentPosition = 0;
    private static final int[] bannerImages = new int[] {
            R.drawable.daiquangialamaghoang,
            R.drawable.dialogbkg,
            R.drawable.dialogbkgg,

    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPagerTruyen = view.findViewById(R.id.viewPagerBanner);
        adapterBanner = new AdapterBanner(getContext(), bannerImages);
        viewPagerTruyen.setAdapter(adapterBanner);
        startBannerAutoSlide();

    }
    private void startBannerAutoSlide() {
        Runnable bannerRunnable = () -> {
            currentPosition++;
            if (currentPosition >= bannerImages.length) {
                currentPosition = 0;
            }
            viewPagerTruyen.setCurrentItem(currentPosition, true);
        };
        bannerHandler.postDelayed(bannerRunnable, 3000);
    }
}