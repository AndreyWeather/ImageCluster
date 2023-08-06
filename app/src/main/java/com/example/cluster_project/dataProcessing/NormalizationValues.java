package com.example.cluster_project.dataProcessing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class NormalizationValues {

  /*public  ArrayList<Double> getNormalizationValues(ConcurrentHashMap<Integer, Integer> input_array) {
        ArrayList<Double> normalize_0_1_values = new ArrayList<>();
       ArrayList<Integer> input_array1 = new ArrayList<>();
        for (int i = 0; i < input_array.size(); i++){
            input_array1.add(input_array.get(i));
        }
        Double min_input_array_values = Double.valueOf(Collections.min(input_array1));
        Double max_input_array_values = Double.valueOf(Collections.max(input_array1));
        for (int i = 0; i < input_array.size(); i++) {
            Double normalize_values = ((Double.valueOf(input_array.get(i)) - min_input_array_values) / (max_input_array_values - min_input_array_values));
            double val = normalize_values;
            val = val * 100;
            val = Math.round(val);
            val = val / 100;
            normalize_0_1_values.add(val);
        }
        return normalize_0_1_values;
    }*/
  public ArrayList<Double> getNormalizationValues(CopyOnWriteArrayList<Integer> input_array) {
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
}
