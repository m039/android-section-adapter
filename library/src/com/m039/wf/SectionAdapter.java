/** SectionsAdapter.java ---
 *
 * Copyright (C) 2012 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.m039.wf;

import android.widget.BaseAdapter;
import java.util.Map;
import java.util.List;
import android.widget.Adapter;
import java.util.HashMap;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

/**
 * Standalone class.
 *
 * Created: 04/06/12
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
abstract public  class SectionAdapter extends SectionBaseAdapter {
    abstract public Adapter  createMainSectionAdapter();
    abstract public Adapter  createSectionAdapter(Object s);

    public SectionAdapter() {
        reinit();
    }

    void clear() {
        mMainSectionAdapter = null;
        mSectionAdapters.clear();
    }
    
    public void     reinit() {
        clear();
        
        mMainSectionAdapter = createMainSectionAdapter();

        if (mMainSectionAdapter == null)
            return;

        int sections = mMainSectionAdapter.getCount();

        for (int i = 0; i < sections; i++) {
            Object s = mMainSectionAdapter.getItem(i);

            Adapter a = createSectionAdapter(s);

            if (a != null)
                addSectionAdapter(s, a);
        }
    }

} // SectionsAdapter
