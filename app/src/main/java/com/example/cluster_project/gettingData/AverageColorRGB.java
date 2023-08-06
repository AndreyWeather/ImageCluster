package com.example.cluster_project.gettingData;

import android.graphics.Bitmap;
import android.graphics.Color;

public class AverageColorRGB {

    public Integer getAverageColorRGB(Bitmap orientedBitmap) {


        int height = orientedBitmap.getHeight();
        int width = orientedBitmap.getWidth();

        int minWidth = width / 10;
        int minHeight = height / 10;
        Bitmap bitmap_min = Bitmap.createScaledBitmap(orientedBitmap, minWidth,
                minHeight, false);

        long red = 0;
        long green = 0;
        long blue = 0;
        long iteration = 0;


        for (int x = 0; x < minWidth; x++) {
            for (int y = 0; y < minHeight; y++) {

                int colour = bitmap_min.getPixel(x, y);

                red += Color.red(colour);
                green += Color.green(colour);
                blue += Color.blue(colour);
                iteration = x * y;

            }

        }

        Integer average_colour = (int) ((red / iteration + green / iteration + blue / iteration) / 3);


        return average_colour;

    }
}
