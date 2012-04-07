/** Utils.java --- 
 *
 * Copyright (C) 2012 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 */

package com.m039.section_demo;

import java.util.List;
import android.widget.Adapter;
import java.util.ArrayList;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import com.m039.wf.SectionAdapter;

/**
 * 
 *
 * Created: 04/07/12
 *
 * @author Mozgin Dmitry
 * @version 
 * @since 
 */
public class AdapterUtils {

    static ListAdapter  createSectionAdapter(final Context c) {
        return new SectionAdapter(new SectionAdapter.Constructor() {
                public Adapter getMainSectionAdapter() {
                    return AdapterUtils.createMainSectionAdapter(c);
                }

                public Adapter getSectionAdapter(Object s) {
                    if (s instanceof String) {
                        String str = (String) s;
                        String parts[] = str.split("-");

                        return AdapterUtils.createSectionAdapter(c,
                                                                 Integer.parseInt(parts[0]),
                                                                 Integer.parseInt(parts[1]));
                    }

                    return null;
                }
            });
    }
    
    static Adapter      createMainSectionAdapter(Context c) {
        List<String> data = new ArrayList<String>();

        int amount = 10;
                    
        int distance = 0;
                    
        for (int i = 0; i < amount; i++) {
            data.add("" + i * distance + "-" + (i + 1) * distance);
        }

        distance = 1;
                    
        for (int i = 0; i < amount; i++) {
            data.add("" + i * distance + "-" + (i + 1) * distance);
        }                   

        distance = 5;
                    
        for (int i = 0; i < amount; i++) {
            data.add("" + i * distance + "-" + (i + 1) * distance);
        }

        distance = 20;
                    
        for (int i = 0; i < amount; i++) {
            data.add("" + i * distance + "-" + (i + 1) * distance);
        }                   
                    
        return new ArrayAdapter<String>(c, R.layout.section, data);     
    }
    
    static Adapter      createSectionAdapter(Context c, int from, int to) {
        List<String> data = new ArrayList<String>();

        for (int i = from; i < to; i++) {
            data.add(String.valueOf(i));
        }
        
        return new ArrayAdapter<String>(c, R.layout.content, data);
    }

} // Utils
