package com.example.cluster_project.gettingData;

import android.os.Build;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class Time {

    public Integer getCreationImgTime(Long metadata) throws ParseException {


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

    public  Long Date_to_milliss(String date) {
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
    public  String Creation_time_from_metadata(String path) {

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

    public String getDate(Metadata metadata) {
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

}
