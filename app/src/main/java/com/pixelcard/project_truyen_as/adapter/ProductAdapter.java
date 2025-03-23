package com.pixelcard.project_truyen_as.adapter;

import android.app.Activity;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.pixelcard.project_truyen_as.Product;

public class ProductAdapter extends ArrayAdapter<Product> {
    Activity activity;
    int resource;
    public ProductAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.activity=context;
        this.resource=resource;
    }
}
