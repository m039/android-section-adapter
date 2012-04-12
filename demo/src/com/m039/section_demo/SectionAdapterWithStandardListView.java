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

public class SectionAdapterWithStandardListView extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ListView list = new ListView(this);

        list.setAdapter(AdapterUtils.createSectionAdapter(this));
        list.setOnItemClickListener(AdapterUtils.createOnItemClickListener(this));

        setContentView(list);
    }
}
