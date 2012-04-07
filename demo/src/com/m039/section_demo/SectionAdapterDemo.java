package com.m039.section_demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Adapter;
import java.util.List;
import android.widget.ListAdapter;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.view.View;
import android.widget.Toast;
import android.util.Log;

import com.m039.wf.SectionAdapter;
import com.m039.wf.SectionListView;

public class SectionAdapterDemo extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SectionListView list = new SectionListView(this);

        list.setAdapter(createMainAdapter());
        
        list.setOnPushedUpListener(new SectionListView.OnPushedUpListener() {
                public void onPushedUp(View pinnedView, float relativeDistance) {
                    pinnedView.getBackground().setAlpha(200);
                    
                    pinnedView.setAlpha(relativeDistance);
                }
            });

        list.setOnPinnedViewClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "on-pinned-view-click", Toast.LENGTH_SHORT).show();
                }
            });

        setContentView(list);       
    }

    ListAdapter createMainAdapter() {
        return new SectionAdapter(new SectionAdapter.Constructor() {
                public Adapter getMainSectionAdapter() {
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
                    
                    return new ArrayAdapter<String>(getBaseContext(), R.layout.section, data);
                }

                public Adapter getSectionAdapter(Object s) {
                    if (s instanceof String) {
                        String str = (String) s;
                        String parts[] = str.split("-");

                        return createAdapter(Integer.parseInt(parts[0]),
                                             Integer.parseInt(parts[1]));
                    }

                    return null;
                }
            });
    }

    Adapter createAdapter(int from, int to) {
        List<String> data = new ArrayList<String>();

        for (int i = from; i < to; i++) {
            data.add(String.valueOf(i));
        }
        
        return new ArrayAdapter<String>(this, R.layout.content, data);
    }
}
