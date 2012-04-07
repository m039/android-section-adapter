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
import android.content.Intent;
import android.content.Context;

public class SectionAdapterDemo extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
    }

    public void onDemoClick(View v) {
        int id = v.getId();

        Intent  i;
        Context c = v.getContext();
        
        switch (id) {
        case R.id.section_adapter_with_standard_listview:
            i = new Intent(c, SectionAdapterWithStandardListView.class);
            c.startActivity(i);
            break;
        case R.id.section_adapter_with_section_listview:
            i = new Intent(c, SectionAdapterWithSectionListView.class);
            c.startActivity(i);
            break;
        case R.id.section_adapter_with_section_listview_debug:
            i = new Intent(c, SectionAdapterWithSectionListViewDebug.class);
            c.startActivity(i);
            break;          
        default:
            break;
        }
    }
}
