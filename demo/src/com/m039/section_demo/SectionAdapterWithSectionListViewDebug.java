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
import android.widget.ListView;

public class SectionAdapterWithSectionListViewDebug extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SectionListView list = new SectionListView(this);
       
        list.setAdapter(AdapterUtils.createSectionAdapter(this));
        list.setOnItemClickListener(AdapterUtils.createOnItemClickListener(this));      

        list.setShowHideSections(false);
        list.setShowDebugTextView(true);        

        setContentView(list);
    }
}
