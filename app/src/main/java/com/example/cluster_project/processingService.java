package com.example.cluster_project;

import static java.lang.Math.abs;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

import com.example.cluster_project.dataProcessing.NormalizationValues;
import com.example.cluster_project.externalStorage.NameFilesInDir;
import com.example.cluster_project.gettingData.AverageColorRGB;
import com.example.cluster_project.gettingData.DivideImages;
import com.example.cluster_project.gettingData.DominantColour;
import com.example.cluster_project.gettingData.Orientation;
import com.example.cluster_project.gettingData.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class processingService extends Service {





    String URI_PARCE = "";




    private final IBinder binder = new ProcessingBinder();

    public class ProcessingBinder extends Binder {
        processingService getProcessingService() {
            return processingService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        URI_PARCE = intent.getExtras().getString("path");
        return binder;


    }
    NameFilesInDir nameFilesInDir = new NameFilesInDir();
    Integer size = nameFilesInDir.getNameFilesInDir(URI_PARCE).size();

    List<String[]> data_in = new ArrayList<>();
    public static List<String[]> data_set_input_s = new ArrayList<>();

    ConcurrentHashMap<Integer, Integer> average_colour_map = new ConcurrentHashMap<>(size,0.75f, size);
    ConcurrentHashMap<Integer, Integer> averege_colour_1_part_map = new ConcurrentHashMap<>(size,0.75f, size);
    ConcurrentHashMap<Integer, Integer> averege_colour_2_part_map = new ConcurrentHashMap<>(size,0.75f, size);
    ConcurrentHashMap<Integer, Integer> averege_colour_3_part_map = new ConcurrentHashMap<>(size,0.75f, size);
    ConcurrentHashMap<Integer, Integer> averege_colour_4_part_map = new ConcurrentHashMap<>(size,0.75f, size);
    ConcurrentHashMap<Integer, Integer> dominant_colour = new ConcurrentHashMap<>(size,0.75f, size);
    ConcurrentHashMap<Integer, Integer> time_map = new ConcurrentHashMap<>(size,0.75f, size);

    CopyOnWriteArrayList<Integer> AverageColour = new CopyOnWriteArrayList<Integer>();
    CopyOnWriteArrayList<Integer> AverageColour1Part = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<Integer> AverageColour2Part = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<Integer> AverageColour3Part = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<Integer> AverageColour4Part = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<Integer> Dominant_colour = new CopyOnWriteArrayList<>();

    CopyOnWriteArrayList<Integer> Get_time = new CopyOnWriteArrayList<>();


    public class MTgetAverageColour implements Runnable {

        private final String task;
        int i;
        volatile String path1 = "1";
        final Bitmap[] bitmap_conver_from_img = {null};

        MTgetAverageColour(String task, int i) {
            this.task = task;
            this.i = i;
        }

        @Override
        public void run() {
            AverageColorRGB averageColorRGB = new AverageColorRGB();
            NameFilesInDir nameFilesInDir = new NameFilesInDir();
            DivideImages divideImages = new DivideImages();
            DominantColour dominantColour = new DominantColour();
            Time time = new Time();
            Orientation orientation = new Orientation();



            String path = URI_PARCE;
            String path_directory_file = String.format("/%s/%s", path, nameFilesInDir.getNameFilesInDir(path).get(i));
            path1 = Environment.getExternalStorageDirectory().toString() + path_directory_file;
            bitmap_conver_from_img[0] = BitmapFactory.decodeFile(path1);
            Bitmap orientedBitmap = orientation.rotateBitmap(nameFilesInDir.getNameFilesInDir(path).get(i), bitmap_conver_from_img[0]);
            //Bitmap orientedBitmap = MainActivity.ExifUtil.rotateBitmap(path, bitmap_conver_from_img[0]);

            average_colour_map.put(i, averageColorRGB.getAverageColorRGB(bitmap_conver_from_img[0]));
            averege_colour_1_part_map.put(i, abs(averageColorRGB.getAverageColorRGB(divideImages.getDivideImages(orientedBitmap).get(0))));
            averege_colour_2_part_map.put(i, abs(averageColorRGB.getAverageColorRGB(divideImages.getDivideImages(orientedBitmap).get(1))));
            averege_colour_3_part_map.put(i, abs(averageColorRGB.getAverageColorRGB(divideImages.getDivideImages(orientedBitmap).get(2))));
            averege_colour_4_part_map.put(i, abs(averageColorRGB.getAverageColorRGB(divideImages.getDivideImages(orientedBitmap).get(3))));
            dominant_colour.put(i, abs((dominantColour.getDominantColor(orientedBitmap))));
            try {
                time_map.put(i, time.getCreationImgTime(time.Date_to_milliss(time.Creation_time_from_metadata(path1))));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }




        }
    }

    public void multitrading_average_colour() {
        NameFilesInDir nameFilesInDir = new NameFilesInDir();

        ExecutorService executor = Executors.newFixedThreadPool(size);
        for (int i = 0; i < nameFilesInDir.getNameFilesInDir(URI_PARCE).size(); i++) {
            Runnable worker = new MTgetAverageColour("Task" + i, i);
            executor.execute(worker);

        }

        executor.shutdownNow();

    }


    public List<String[]> ACM() {

        NameFilesInDir nameFilesInDir = new NameFilesInDir();
        NormalizationValues normalizationValues = new NormalizationValues();
        Integer Size = 0;

        for (int i = 0; i < nameFilesInDir.getNameFilesInDir(URI_PARCE).size(); i++) {


            AverageColour.add(average_colour_map.get(i));
            AverageColour1Part.add(averege_colour_1_part_map.get(i));
            AverageColour2Part.add(averege_colour_2_part_map.get(i));
            AverageColour3Part.add(averege_colour_3_part_map.get(i));
            AverageColour4Part.add(averege_colour_4_part_map.get(i));
            Dominant_colour.add(dominant_colour.get(i));
            Get_time.add(time_map.get(i));

        }

        for (int i = 0; i < nameFilesInDir.getNameFilesInDir(URI_PARCE).size(); i++) {
            data_set_input_s = new ArrayList<>(getData_in_add(
                    normalizationValues.getNormalizationValues(AverageColour).get(i),
                    normalizationValues.getNormalizationValues(AverageColour1Part).get(i),
                    normalizationValues.getNormalizationValues(AverageColour2Part).get(i),
                    normalizationValues.getNormalizationValues(AverageColour3Part).get(i),
                    normalizationValues.getNormalizationValues(AverageColour4Part).get(i),
                    normalizationValues.getNormalizationValues(Dominant_colour).get(i),
                    Get_time.get(i)));

        }
        data_set_input_s.add(0, new String[]{"average", "average_1", "average_2", "average_3", "average_4", "dominant_colour", "time"});

          return data_set_input_s;
    }


   public List<String[]> getData_in_add(Double average, Double average_1, Double average_2, Double average_3, Double average_4, Double dominant_colours, Integer time) {

        data_in.add(new String[]{average.toString(), average_1.toString(), average_2.toString(), average_3.toString(), average_4.toString(), dominant_colours.toString(), time.toString()});
        return data_in;
    }

    public Integer test_size() {

        return average_colour_map.size();
    }

}