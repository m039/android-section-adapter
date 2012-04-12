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
public class SectionBaseAdapter extends BaseAdapter {
    protected static final String TAG = "m039-SectionBaseAdapter";

    Adapter                 mMainSectionAdapter     = null;
    Map<Object, Adapter>    mSectionAdapters        = new HashMap<Object, Adapter>();

    public SectionBaseAdapter() {
    }

    public void     setMainSectionAdapter(Adapter a) {
        mMainSectionAdapter = a;
    }

    public Adapter  getMainSectionAdapter() {
        return mMainSectionAdapter;
    }

    public void     addSectionAdapter(Object s, Adapter a) {
        mSectionAdapters.put(s, a);
    }

    public Adapter  getSectionAdapter(Object s) {
        return mSectionAdapters.get(s);
    }

    public Object   getSectionAtIndex(int index) {
        int sections = mMainSectionAdapter.getCount();

        if (index >= sections)
            return null;

        return mMainSectionAdapter.getItem(index);
    }

    /**
     * Like getItem(position), but for section
     */ 
    public Object   getSection(int position) {
        return getSectionAtIndex(getSectionIndexAtPosition(position));
    }

    /**
     * Returns int[3], where:
     *
     * res[0] - previous section position
     * res[1] - current section position
     * res[2] - next section position
     */
    int[]        getSectionsAtPosition(int position) {
        int res[] = new int[3];

        int index = getSectionIndexAtPosition(position);

        res[0] = getSectionPositionAtIndex(index - 1);
        res[1] = getSectionPositionAtIndex(index);
        res[2] = getSectionPositionAtIndex(index + 1);

        return res;
    }

    public int          getSectionPositionAtIndex(int index) {
        int sections = mMainSectionAdapter.getCount();

        int p = 0;

        for (int i = 0; i < sections; i++) {
            if (i == index)
                return p;

            p++;

            Object s = mMainSectionAdapter.getItem(i);

            // go through childs
            if (s != null) {
                Adapter a = getSectionAdapter(s);

                if (a != null) {
                    p += a.getCount();
                }
            }
        }

        return -1;
    }

    public int          getSectionIndexAtPosition(int position) {
        int sections = mMainSectionAdapter.getCount();

        int p = 0;

        for (int i = 0; i < sections; i++) {
            if (p == position)
                return i;

            p++;

            Object s = mMainSectionAdapter.getItem(i);

            // go through childs
            if (s != null) {
                Adapter a = getSectionAdapter(s);

                if (a != null) {
                    int childs = a.getCount();

                    if (position - p < childs) {
                        return i;
                    }

                    p += childs;
                }
            }
        }

        return -1;
    }

    public final static int SECTION_T = 0;
    public final static int CONTENT_T = 1;

    public final static int TYPE_COUNT = CONTENT_T + 1;

    @Override
    public int      getItemViewType(int position) {
        int sections = mMainSectionAdapter.getCount();

        int p = 0;

        for (int i = 0; i < sections; i++) {
            Object s = mMainSectionAdapter.getItem(i);

            if (p == position)
                return SECTION_T;

            p++;

            // go through childs
            if (s != null) {
                Adapter a = getSectionAdapter(s);

                if (a != null) {
                    int childs = a.getCount();

                    if (position - p < childs) {
                        return CONTENT_T;
                    }

                    p += childs;
                }
            }
        }

        return -1;
    }

    @Override
    public boolean  isEnabled(int position) {
        return getItemViewType(position) != SECTION_T;
    }

    @Override
    public int      getViewTypeCount () {
        return TYPE_COUNT;
    }

    @Override
    public View     getView(int position, View convertView, ViewGroup parent) {
        // Log.d(TAG, "pos: " + position);

        int sections = mMainSectionAdapter.getCount();

        int p = 0;

        for (int i = 0; i < sections; i++) {
            // Log.d(TAG, "p = " + p);

            if (p == position) {
                return mMainSectionAdapter.getView(i, convertView, parent);
            }

            p++;

            Object s = mMainSectionAdapter.getItem(i);
            // Log.d(TAG, "s = " + s);

            // go through childs
            if (s != null) {
                Adapter a = getSectionAdapter(s);
                // Log.d(TAG, "a = " + a);

                if (a != null) {
                    int childs = a.getCount();

                    if (position - p < childs) {
                        return a.getView(position - p, convertView, parent);
                    }

                    p += childs;
                }
            }
        }

        return null;
    }



    @Override
    public long     getItemId(int position) {
        return position;
    }

    @Override
    public Object   getItem(int position) {
        int sections = mMainSectionAdapter.getCount();

        int p = 0;

        for (int i = 0; i < sections; i++) {
            Object s = mMainSectionAdapter.getItem(i);

            if (p == position)
                return s;

            p++;

            // go through childs
            if (s != null) {
                Adapter a = getSectionAdapter(s);

                if (a != null) {
                    int childs = a.getCount();

                    if (position - p < childs) {
                        return a.getItem(position - p);
                    }

                    p += childs;
                }
            }
        }

        return null;
    }

    @Override
    public int      getCount() {
        int sections = mMainSectionAdapter.getCount();
        int childs = 0;

        for (int i = 0; i < sections; i++) {
            Object s = mMainSectionAdapter.getItem(i);

            if (s != null) {
                Adapter a = getSectionAdapter(s);

                if (a != null)
                    childs += a.getCount();
            }
        }

        return sections + childs;
    }

} // SectionsAdapter
