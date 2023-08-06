package com.example.cluster_project;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {


    public static String file_text;
    public static String URI_path;
    public static String URI_PARCE;

    private static final int PERMISSION_REQUEST_CODE = 7;
    String FolderName = "cluster";

    Uri imageUri;

    EditText userInput;
    static TextView fileContent;
    TextView SSE;
    TextView minimum_sse;
    TextView TV3;


    Button buttonAdd, select, intent_test, Created_folder, service, result_s;
    ImageView test_img_louder, test_img_louder1, test_img_louder2, test_img_louder3, test_img_louder4;

    EditText userCluster;
    Bitmap bitmap, bitmap1;


    private Intent mMyServiceIntent;


    private String filename = "data.csv";
    int select_code = 100;
    Long a = Long.valueOf(1);


    List<String[]> data_in = new ArrayList<>();

    private processingService processing_image;
    private boolean bound = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            processingService.ProcessingBinder processingBinder =
                    (processingService.ProcessingBinder) binder;
            processing_image = processingBinder.getProcessingService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;

        }
    };


    @SuppressLint({"SdCardPath", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        intent_test = (Button) findViewById(R.id.intent_test);
        Created_folder = (Button) findViewById(R.id.Created_folder);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        select = findViewById(R.id.select);
        service = findViewById(R.id.service);
        result_s = findViewById(R.id.result_s);
        userCluster = findViewById(R.id.userCluster);


        SSE = (TextView) findViewById(R.id.SSE);
        minimum_sse = (TextView) findViewById(R.id.min_sse);
        TV3 = (TextView) findViewById(R.id.TV3);

        test_img_louder = (ImageView) findViewById(R.id.test_img_louder);
        test_img_louder1 = (ImageView) findViewById(R.id.test_img_louder1);
        test_img_louder2 = (ImageView) findViewById(R.id.test_img_louder2);
        test_img_louder3 = (ImageView) findViewById(R.id.test_img_louder3);
        test_img_louder4 = (ImageView) findViewById(R.id.test_img_louder4);


        String input_data = getApplication().getFilesDir().getAbsolutePath() + File.separator + "data_input.csv";
        String output_data = getApplication().getFilesDir().getAbsolutePath() + File.separator + "output_data.csv";
        String path = this.getFilesDir().getPath();

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.d7);
        String path_img = Environment.getExternalStorageDirectory().toString() + "/Claster_test/d4.jpg";
        bitmap1 = BitmapFactory.decodeFile(path_img);
        String path_img_out = Environment.getExternalStorageDirectory().toString() + "/Claster_test";


        String path_to_external = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "Cluster" + File.separator + "output_data.csv";

        final int width = bitmap.getWidth();

        test_img_louder.setImageResource(R.drawable.d4);


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (userCluster.getText() != null) {
                    try {

                        Integer cluster = Integer.parseInt(String.valueOf(userCluster.getText()));

                        Integer number = Cluster_number(input_data, output_data);
                        Integer clusterMaker = 0;

                        while (number != cluster) {
                            clusterMaker = Cluster_number(input_data, output_data);
                            if (clusterMaker == cluster){KMeans.general(input_data, output_data,clusterMaker);
                                break;
                            }
                        }
                    }
                    catch (Exception e) {
                        Integer number = Cluster_number(input_data, output_data);
                        KMeans.general(input_data, output_data, number);
                    }

                } else {
                    Integer number = Cluster_number(input_data, output_data);
                    KMeans.general(input_data, output_data, number);
                }
                buttonAdd.setBackgroundColor(Color.GREEN);

                if (userCluster.getText() != null) {
                    try {
                        Integer cluster = Integer.parseInt(String.valueOf(userCluster.getText()));
                        TV3.setText(cluster.toString());
                    }catch (Exception e) {
                        Integer number = Cluster_number(input_data, output_data);
                        TV3.setText(Cluster_number(input_data, output_data).toString());
                    }
                }
                else {
                    Integer number = Cluster_number(input_data, output_data);
                    TV3.setText(Cluster_number(input_data, output_data).toString());
                }

            }

        });
        intent_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);
                }

                intent_test.setBackgroundColor(Color.GREEN);


            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            Context context;

            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {

                select.setBackgroundColor(Color.YELLOW);

                ArrayList<Integer> Averege_colour_main_img = new ArrayList<>();
                ArrayList<Integer> Averege_colour_1_part_img = new ArrayList<>();
                ArrayList<Integer> Averege_colour_2_part_img = new ArrayList<>();
                ArrayList<Integer> Averege_colour_3_part_img = new ArrayList<>();
                ArrayList<Integer> Averege_colour_4_part_img = new ArrayList<>();
                ArrayList<Integer> Dominant_colour = new ArrayList<>();
                ArrayList<Integer> Time = new ArrayList<>();
                List<String[]> data_set_input = new ArrayList<>();


                String path_from_parametr = String.format("/%s", URI_PARCE);
                String getNameFilesInDir_size = Integer.toString(getNameFilesInDir(URI_PARCE).size());
                int getNameFilesInDir_size_int = getNameFilesInDir(URI_PARCE).size();

                final Bitmap[] bitmap_conver_from_img = {null};


                for (Integer i = 0; i < getNameFilesInDir_size_int; i++) {

                    String path_directory_file = String.format("/%s/%s", URI_PARCE, getNameFilesInDir(URI_PARCE).get(i));
                    String path = Environment.getExternalStorageDirectory().toString() + path_directory_file;
                    bitmap_conver_from_img[0] = BitmapFactory.decodeFile(path);
                    Bitmap orientedBitmap = ExifUtil.rotateBitmap(path, bitmap_conver_from_img[0]);


                    Averege_colour_main_img.add(((getAverageColorRGB(orientedBitmap))));
                    Averege_colour_1_part_img.add(abs(getAverageColorRGB(divideImages(orientedBitmap).get(0))));
                    Averege_colour_2_part_img.add(abs(getAverageColorRGB(divideImages(orientedBitmap).get(1))));
                    Averege_colour_3_part_img.add(abs(getAverageColorRGB(divideImages(orientedBitmap).get(2))));
                    Averege_colour_4_part_img.add(abs(getAverageColorRGB(divideImages(orientedBitmap).get(3))));
                    Dominant_colour.add(abs((getDominantColor(orientedBitmap))));
                    try {
                        Time.add(getCreationImgTime(Date_to_milliss(Creation_time_from_metadata(path))));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }


                    test_img_louder1.setImageBitmap(divideImages(orientedBitmap).get(1));
                    test_img_louder2.setImageBitmap(divideImages(orientedBitmap).get(2));
                    test_img_louder3.setImageBitmap(divideImages(orientedBitmap).get(3));


                }
                for (int i = 0; i < Averege_colour_main_img.size(); i++) {

                    data_set_input = new ArrayList<>(getData_in_add(
                            getNormalizationValues(Averege_colour_main_img).get(i),
                            getNormalizationValues(Averege_colour_1_part_img).get(i),
                            getNormalizationValues(Averege_colour_2_part_img).get(i),
                            getNormalizationValues(Averege_colour_3_part_img).get(i),
                            getNormalizationValues(Averege_colour_4_part_img).get(i),
                            getNormalizationValues(Dominant_colour).get(i),
                            Time.get(i)));


                }
                data_set_input.add(0, new String[]{"average", "average_1", "average_2", "average_3", "average_4", "dominant_colour", "time"
                });

                writeInputDataCsv(data_set_input);

                select.setBackgroundColor(Color.GREEN);


                /*String Averege_colour_main_img_size_string = Integer.toString(Averege_colour_main_img.size());
                String getNormalizationValues_toString = Double.toString(getNormalizationValues(Averege_colour_main_img).get(10));
                String getNormalizationValues_toString1 = Double.toString(getNormalizationValues(Averege_colour_1_part_img).get(10));
                String Averege_colour_1_part_img_toString = Integer.toString(Averege_colour_1_part_img.get(1));*/


                String pathName = Environment.getExternalStorageDirectory() + File.separator + "d2.jpg";


                Integer Average_color = getAverageColor(bitmap);


                test_img_louder1.setImageBitmap((divideImages(bitmap).get(0)));
                test_img_louder2.setImageBitmap((divideImages(bitmap).get(1)));
                test_img_louder3.setImageBitmap((divideImages(bitmap).get(2)));
                test_img_louder4.setImageBitmap((divideImages(bitmap).get(3)));

                String divideImages_size_string = Integer.toString(divideImages(bitmap).size());


            }
        });

        Created_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStreamReader csvStreamReader;
                try {
                    csvStreamReader = new InputStreamReader(new FileInputStream(output_data));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                //Integer gg = Sort_img_name_list(getNameFilesInDir(URI_PARCE),Parcer_out_csv.Parcer_out(csvStreamReader)).size();
                //TV_test_2.setText(Parcer_out_csv.Parcer_out(csvStreamReader).get(1).toString());
                createSortImageInExternalStorage("cluster_sort_img", Sort_img_name_list(getNameFilesInDir(URI_PARCE), Parcer_out_csv.Parcer_out(csvStreamReader)));
                Created_folder.setBackgroundColor(Color.GREEN);
            }
        });

        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this, processingService.class);
                if (URI_PARCE != null) intent.putExtra("path", URI_PARCE);
                if (bound && processing_image != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {



                    }
                }


                bindService(intent, connection, Context.BIND_AUTO_CREATE);

                final Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String test = "";
                        if (bound && processing_image != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                //processing_image.create_path_array();

                                if (processing_image.test_size() < getNameFilesInDir(URI_PARCE).size()) {
                                    processing_image.multitrading_average_colour();
                                    //processing_image.multitrading_average_colour1Part();

                                    test = ("Данные получены");

                                    TV3.setText(test);
                                    writeInputDataCsv(processing_image.ACM());
                                    select.setBackgroundColor(Color.GREEN);
                                }


                            }
                        }

                        handler.postDelayed(this, 10);


                    }


                });
            }

        });

        result_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myApp", "no network");
            }
        });


        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
                .check();


    }

    private String getServiceTest() {
        String test = "";
        if (bound && processing_image != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {


                test = (processing_image.ACM().toString());


            }
        }
        return test;
    }


    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == select_code && data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                test_img_louder.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        switch (requestCode) {

            case 9999:
                ArrayList<String> Uri_to_ArrayList = new ArrayList<>();
                String UriResult = "";
                File path = new File(data.getData().toString());
                URI_path = (data.getData().getPath());
                String[] Uri_correct_array = URI_path.split(":");
                String URI_correct = String.join(",", Uri_correct_array[1]);
                URI_PARCE = URI_correct;


        }

    }


    ArrayList<String> getNameFilesInDir(String get_dir_name) {
        String path_from_parametr = String.format("/%s", get_dir_name);
        String path = Environment.getExternalStorageDirectory().toString() + path_from_parametr;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        ArrayList<String> get_name_files = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            get_name_files.add(files[i].getName());
        }
        return get_name_files;
    }


    public static String Creation_time_from_metadata(String path) {

        String data_created = "";
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new File(path));
            data_created = getDate(metadata);
        } catch (ImageProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data_created;
    }

    public static String getDate(Metadata metadata) {
        DateTimeFormatter dtf = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH:mm:ss");
        }
        String date0 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date0 = dtf.format(LocalDateTime.now());
        }
        if (metadata == null) {

        }

        ExifDirectoryBase exifDir = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        Long time = null;
        if (exifDir != null) {
            date0 = exifDir.getString(ExifDirectoryBase.TAG_DATETIME_ORIGINAL);
            if (date0 != null) {
                String[] s = date0.split(" ");
                time = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    time = (Duration.between(
                            LocalTime.MIN,
                            LocalTime.parse(s[1])
                    ).toMillis());

                }
                return s[0].replace(":", "/") + ' ' + s[1];
            }
        } else {
        }
        ;

        return null;
    }

    public static Long Date_to_milliss(String date) {
        DateTimeFormatter dtf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        }
        LocalDateTime ldt = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ldt = LocalDateTime.parse(date, dtf);
        }
        long millis = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            millis = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        return millis;
    }


    public static Integer getCreationImgTime(Long metadata) throws ParseException {


        String dateString = new SimpleDateFormat("yyyy/MM/dd").format(new Date(metadata));
        // получение часа в котором создано изображение;
        String time_hh = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time_hh = DateTimeFormatter.ofPattern("HH")
                    .format(Instant.ofEpochMilli(metadata).atZone(ZoneId.of("Europe/Moscow")));
        }
        // сортировка часов по категориям;
        int time_hh_to_time_categoria = 0;
        int time_hh_to_int = Integer.parseInt(time_hh);
        if ((time_hh_to_int >= 0) && (time_hh_to_int <= 4)) {
            time_hh_to_time_categoria = 1;
        }
        if ((time_hh_to_int >= 4) && (time_hh_to_int <= 8)) {
            time_hh_to_time_categoria = 2;
        }
        if ((time_hh_to_int >= 8) && (time_hh_to_int <= 12)) {
            time_hh_to_time_categoria = 3;
        }
        if ((time_hh_to_int >= 12) && (time_hh_to_int <= 16)) {
            time_hh_to_time_categoria = 4;
        }
        if ((time_hh_to_int >= 16) && (time_hh_to_int <= 18)) {
            time_hh_to_time_categoria = 5;
        }
        if ((time_hh_to_int >= 18) && (time_hh_to_int <= 21)) {
            time_hh_to_time_categoria = 6;
        }
        if ((time_hh_to_int >= 21) && (time_hh_to_int <= 23)) {
            time_hh_to_time_categoria = 7;
        }

        String time_hh_to_time_categoria_to_String;
        time_hh_to_time_categoria_to_String = Integer.toString(time_hh_to_time_categoria);

        // получение дня недели в котором было создано изображениеж

        Date yourDate = new SimpleDateFormat("yyyy/MM/dd").parse(dateString);
        Calendar c = Calendar.getInstance();
        c.setTime(yourDate);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        dateString = Integer.toString(dayOfWeek);

        if ((dayOfWeek == 7) || (dayOfWeek == 1)) {
            dateString = "1";
        } else {
            dateString = "0";
        }

        String date_created = dateString;
        Integer date = Integer.parseInt(date_created);
        return date;
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Integer getAverageColorRGB(Bitmap bitmap) {


        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        int minWidth = width / 10;
        int minHeight = height / 10;
        Bitmap bitmap_min = Bitmap.createScaledBitmap(bitmap, minWidth,
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

        int average_colour = (int) ((red / iteration + green / iteration + blue / iteration) / 3);


        return average_colour;

    }


    public static Integer getAverageColor(Bitmap bitmap) {
        if (null == bitmap) return Color.TRANSPARENT;

        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int alphaBucket = 0;

        boolean hasAlpha = bitmap.hasAlpha();
        int pixelCount = bitmap.getWidth() * bitmap.getHeight();
        int[] pixels = new int[pixelCount];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int y = 0, h = bitmap.getHeight(); y < h; y++) {
            for (int x = 0, w = bitmap.getWidth(); x < w; x++) {
                int color = pixels[x + y * w]; // x + y * width
                redBucket += (color); // Color.red
                greenBucket += (color); // Color.greed
                blueBucket += (color); // Color.blue
                if (hasAlpha) alphaBucket += (color >>> 24); // Color.alpha
            }
        }

        return Color.argb(
                (hasAlpha) ? (alphaBucket / pixelCount) : 255,
                redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount);
    }


    private ArrayList<Bitmap> divideImages(Bitmap b) {
        ArrayList<Bitmap> bs = new ArrayList<Bitmap>();
// TODO Auto-generated method stub
        final int width = b.getWidth();
        final int height = b.getHeight();

        final int pixelByCol = width / 2;
        final int pixelByRow = height / 2;
//List<Bitmap> bs = new ArrayList<Bitmap>();
        for (int i = 0; i < 2; i++) {
            System.out.println("row no. " + i);
            for (int j = 0; j < 2; j++) {

                System.out.println("Column no." + j);
                int startx = pixelByCol * j;
                int starty = pixelByRow * i;
                Bitmap b1 = Bitmap.createBitmap(b, startx, starty, pixelByCol, pixelByRow);
                bs.add(b1);

            }

        }
        return bs;
    }

    ArrayList<Double> getNormalizationValues(ArrayList<Integer> input_array) {
        ArrayList<Double> normalize_0_1_values = new ArrayList<>();
        Double min_input_array_values = Double.valueOf(Collections.min(input_array));
        Double max_input_array_values = Double.valueOf(Collections.max(input_array));
        for (int i = 0; i < input_array.size(); i++) {
            Double normalize_values = ((Double.valueOf(input_array.get(i)) - min_input_array_values) / (max_input_array_values - min_input_array_values));
            double val = normalize_values;
            val = val * 100;
            val = Math.round(val);
            val = val / 100;
            normalize_0_1_values.add(val);
        }
        ;
        return normalize_0_1_values;
    }


    Bitmap IMG_louder() throws FileNotFoundException {
        String photoPath = android.os.Environment.getExternalStorageDirectory() + "/Claster_test/d2.jpg";
        File sdCard = android.os.Environment.getExternalStorageDirectory();

        File directory = new File(sdCard.getAbsolutePath() + "/Claster_test");

        File file = new File(directory, "d2.jpg"); //or any other format supported

        FileInputStream streamIn = new FileInputStream(file);

        Bitmap bitmap = BitmapFactory.decodeStream(streamIn); //This gets the image

        try {
            streamIn.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bitmap;
    }

    private List<String[]> getData_in_add(Double average, Double average_1, Double average_2, Double average_3, Double average_4, Double dominant_colours, Integer time) {

        data_in.add(new String[]{average.toString(), average_1.toString(), average_2.toString(), average_3.toString(), average_4.toString(), dominant_colours.toString(), time.toString()});
        return data_in;
    }


    private void writeInputDataCsv(List<String[]> data_in) {
        String filePath = getApplication().getFilesDir().getAbsolutePath() + File.separator + "data_input.csv";

        // first create file object for file placed at location
        // specified by filepath
        File file = new File(filePath);

        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter with '|' as separator
            CSVWriter writer = new CSVWriter(outputfile, ',',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            writer.writeAll(data_in);

            // closing writer connection
            writer.close();
            Toast.makeText(getApplicationContext(), "записан!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public List<String> Sort_img_name_list(ArrayList<String> name_img_file, ArrayList<String> cluster_number) {


        Map<String, Integer> Img_Cluster_Map = new HashMap<>();
        for (int i = 0; i < name_img_file.size(); i++)
            Img_Cluster_Map.put(name_img_file.get(i), Integer.parseInt(cluster_number.get(i)));


        // Сортировка карты по значениям
        Img_Cluster_Map = Img_Cluster_Map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));


        Map<String, Integer> finalImg_Cluster_Map = Img_Cluster_Map;

        List<String> Sort_image_keyList = new ArrayList<>(finalImg_Cluster_Map.keySet());


        return Sort_image_keyList;

    }


    private void createSortImageInExternalStorage(String name_of_directory, List<String> sort_img_name_list) {

        final File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), name_of_directory);

        if (!f.exists()) {
            boolean rv = f.mkdir();
        } else {
            Toast.makeText(MainActivity.this, "problem with creating a folder in external storage", Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < sort_img_name_list.size(); i++) {

            String path_directory_file = String.format("/%s/%s", URI_PARCE, sort_img_name_list.get(i));
            String path = Environment.getExternalStorageDirectory().toString() + path_directory_file;
            Bitmap bitmap_input = BitmapFactory.decodeFile(path);

            Uri uri;
            ContentResolver resolver = getContentResolver();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

            } else {
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            }
            String imgName = "my file";
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, sort_img_name_list.get(i) + ".jpg");
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/" + name_of_directory);
            Uri finalUri = resolver.insert(uri, contentValues);
            imageUri = finalUri;

            try {


                OutputStream outputStream = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                bitmap_input.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                Objects.requireNonNull(outputStream);

                Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();

            } catch (FileNotFoundException e) {
                Toast.makeText(MainActivity.this, "Image Not Saved!", Toast.LENGTH_SHORT).show();
                throw new RuntimeException(e);
            }
        }
    }

    ArrayList<Double> SSE_total(String input_data, String output_data) {

        ArrayList<Double> ERR = new ArrayList<>();
        Map<Double, Integer> sse_cluster = new HashMap<>();
        int i = 0;
        boolean exit = true;
        while (exit) {
            KMeans.general(input_data, output_data, i + 1);
            ERR.add(KMeans.newSSE);
            sse_cluster.put(KMeans.newSSE, i);
            if (i >= 2) {
                if ((ERR.get(i - 1) - ERR.get(i)) < ((ERR.get(i - 2) - ERR.get(i - 1)) / sqrt(i * i))) {
                    exit = false;

                }
            }
            i++;
        }
        sse_cluster = sse_cluster.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        Set<Double> keys = sse_cluster.keySet();
        ArrayList<Double> key = new ArrayList<>();
        key.addAll(keys);

        return key;

    }


    Integer Cluster_number(String input_data, String output_data) {
        ArrayList<Double> sse = new ArrayList<>();
        ArrayList<Double> sse_sort = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ;
            sse.addAll(SSE_total(input_data, output_data));


        }

        Double min_sse = Collections.min(sse);
        sse.sort(Comparator.naturalOrder());
        ArrayList<Double> ERR = new ArrayList<>();
        Double min_SSE = 0.0;
        int i = 0;


        Map<Double, Integer> sse_cluster = new HashMap<>();

        boolean exit = true;

        while (exit) {
            KMeans.general(input_data, output_data, i + 1);
            ERR.add(KMeans.newSSE);
            if (i >= 2) {
                if ((ERR.get(i) - min_sse) < (ERR.get(i - 1) - (ERR.get(i)))) {
                    exit = false;

                }
            }

            i++;
        }


        minimum_sse.setText(min_sse.toString());
        SSE.setText(ERR.toString());

        // if ((ERR.get(i-2)-ERR.get(i-1))/i > min_sse) {i=i+1;}


        return i;
    }


    public static Integer getDominantColor(Bitmap bitmap) {
        List<Palette.Swatch> swatchesTemp = Palette.from(bitmap).generate().getSwatches();
        List<Palette.Swatch> swatches = new ArrayList<Palette.Swatch>(swatchesTemp);
        Collections.sort(swatches, new Comparator<Palette.Swatch>() {
            @Override
            public int compare(Palette.Swatch swatch1, Palette.Swatch swatch2) {
                return swatch2.getPopulation() - swatch1.getPopulation();
            }
        });
        return swatches.get(0).getRgb();
    }

    public static class ExifUtil {

        public static Bitmap rotateBitmap(String src, Bitmap bitmap) {
            try {
                int orientation = getExifOrientation(src);

                if (orientation == 1) {
                    return bitmap;
                }

                Matrix matrix = new Matrix();
                switch (orientation) {
                    case 2:
                        matrix.setScale(-1, 1);
                        break;
                    case 3:
                        matrix.setRotate(180);
                        break;
                    case 4:
                        matrix.setRotate(180);
                        matrix.postScale(-1, 1);
                        break;
                    case 5:
                        matrix.setRotate(90);
                        matrix.postScale(-1, 1);
                        break;
                    case 6:
                        matrix.setRotate(90);
                        break;
                    case 7:
                        matrix.setRotate(-90);
                        matrix.postScale(-1, 1);
                        break;
                    case 8:
                        matrix.setRotate(-90);
                        break;
                    default:
                        return bitmap;
                }

                try {
                    Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    bitmap.recycle();
                    return oriented;
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        private static int getExifOrientation(String src) throws IOException {
            int orientation = 1;

            try {
                /**
                 * if your are targeting only api level >= 5
                 * ExifInterface exif = new ExifInterface(src);
                 * orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                 */
                if (Build.VERSION.SDK_INT >= 5) {
                    Class<?> exifClass = Class.forName("android.media.ExifInterface");
                    Constructor<?> exifConstructor = exifClass.getConstructor(new Class[]{String.class});
                    Object exifInstance = exifConstructor.newInstance(new Object[]{src});
                    Method getAttributeInt = exifClass.getMethod("getAttributeInt", new Class[]{String.class, int.class});
                    Field tagOrientationField = exifClass.getField("TAG_ORIENTATION");
                    String tagOrientation = (String) tagOrientationField.get(null);
                    orientation = (Integer) getAttributeInt.invoke(exifInstance, new Object[]{tagOrientation, 1});
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            return orientation;
        }
    }

}







