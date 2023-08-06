package com.example.cluster_project;

import android.content.Context;
import android.content.res.AssetManager;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class Parcer_out_csv {


    public static ArrayList<String> Parcer_out(InputStreamReader csvStreamReader) {
        ArrayList<String> out_list = new ArrayList<String>();
        List<String[]> list = new ArrayList<String[]>();
        String next[] = {};

        try {


            CSVReader reader = new CSVReader(csvStreamReader);
            for (; ; ) {
                next = reader.readNext();
                if (next != null) {
                    list.add(next);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < list.size(); i++) {
            out_list.add(list.get(i)[7]);

        }


        out_list.remove(0);
        return out_list;
    }
}





