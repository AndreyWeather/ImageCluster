package com.example.cluster_project.externalStorage;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class NameFilesInDir {
    public ConcurrentHashMap<Integer, String> getNameFilesInDir(String get_dir_name) {
        String path_from_parametr = String.format("/%s", get_dir_name);
        String path = Environment.getExternalStorageDirectory().toString() + path_from_parametr;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);

        ConcurrentHashMap<Integer, String> get_name_files = new  ConcurrentHashMap();
        for (int i = 0; i < files.length; i++) {
            get_name_files.put(i,files[i].getName());
        }
        return get_name_files;
    }
}
