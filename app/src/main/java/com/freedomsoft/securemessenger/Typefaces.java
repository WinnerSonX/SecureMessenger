package com.freedomsoft.securemessenger;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

public class Typefaces {
    private Typefaces() {
    }

    public static void init(Context context) {
        AssetManager assetManager = context.getAssets();
        medium = Typeface.createFromAsset(assetManager, "Roboto-Medium.ttf");
        regular = Typeface.createFromAsset(assetManager, "Roboto-Regular.ttf");
        bold = Typeface.createFromAsset(assetManager, "Roboto-Bold.ttf");
        thin = Typeface.createFromAsset(assetManager, "Roboto-Thin.ttf");
    }

    public static Typeface medium;
    public static Typeface regular;
    public static Typeface bold;
    public static Typeface thin;
}
