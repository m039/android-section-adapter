/** SectionListView.java ---
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

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.view.View;
import android.widget.AbsListView;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.AdapterView;
import android.graphics.Canvas;
import android.view.ViewGroup;
import java.util.Arrays;
import android.widget.FrameLayout;
import android.graphics.Color;
import android.widget.TextView;
import android.view.Gravity;
import android.graphics.Typeface;
import android.text.Html;
import android.view.MotionEvent;

/**
 *
 *
 * Created: 04/06/12
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class SectionListView extends ListView {
    protected static final String TAG = "m039-SectionListView";

    private FrameLayout             mOverlay;
    private View                    mPinnedView;

    // vars
    
    private OnScrollListener        mOnScrollListener = null;
    private OnPushedUpListener      mOnPushedUpListener = null;
    private OnClickListener         mOnPinnedViewClickListener = null;

    // for debugging
    // --- ---------
    private TextView                mDebugTextView = null;

    // config
    // ------

    public  boolean                 mShowHideSections = true;
    public  boolean                 mShowDebugTextView = false;

    public      SectionListView(Context context) {
        super(context);
    }

    public      SectionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static interface OnPushedUpListener {
        /**
         * relativeDistance is:
         *
         * from 1.0 (not moved) to 0.0 (moved away)
         */
        public void onPushedUp(View pinnedView, float relativeDistance);
    }

    {
        initScrollListener();
        initOverlay();

        if (mShowDebugTextView)
            initDebugTextView();
    }

    void        initOverlay() {
        mOverlay = new FrameLayout(getContext());
        mOverlay.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                                                  LayoutParams.MATCH_PARENT));
    }

    void        initDebugTextView() {
        if (mDebugTextView != null)
            return;
        
        mDebugTextView = new TextView(getContext());

        FrameLayout.LayoutParams lp = new  FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                                                                    LayoutParams.WRAP_CONTENT,
                                                                    Gravity.BOTTOM);

        mDebugTextView.setLayoutParams(lp);

        mDebugTextView.setBackgroundColor(Color.WHITE);
        mDebugTextView.setTextColor(Color.BLACK);
        mDebugTextView.setAlpha(0.9f);
        mDebugTextView.setTypeface(Typeface.MONOSPACE);

        mOverlay.addView(mDebugTextView);
    }

    void        initScrollListener() {
        super.setOnScrollListener(new OnScrollListener() {
                public void onScroll(AbsListView view,
                                     int firstVisibleItem,
                                     int visibleItemCount,
                                     int totalItemCount) {

                    if (mOnScrollListener != null) {
                        mOnScrollListener.onScroll(view,
                                                   firstVisibleItem,
                                                   visibleItemCount,
                                                   totalItemCount);
                    }

                    SectionListView.this.onScroll(view,
                                                  firstVisibleItem,
                                                  visibleItemCount,
                                                  totalItemCount);
                }

                public void onScrollStateChanged(AbsListView view,
                                                 int scrollState) {

                    if (mOnScrollListener != null) {
                        mOnScrollListener.onScrollStateChanged(view,
                                                               scrollState);
                    }

                    SectionListView.this.onScrollStateChanged(view,
                                                              scrollState);
                }
            });
    }

    @Override
    protected void  onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mOverlay != null) {
            mOverlay.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void  onLayout (boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mOverlay != null) {
            mOverlay.layout(left, top, right, bottom);
        }
    }

    @Override
    public void     dispatchDraw (Canvas canvas) {
        super.dispatchDraw(canvas);

        if (mOverlay != null)
            mOverlay.draw(canvas);
    }

    @Override
    public void     setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);

        SectionAdapter a = getSectionAdapter();

        // add clean pinned-view

        mPinnedView = a.getView(a.getSectionsAtPosition(0)[1],
                                mPinnedView,
                                mOverlay);

        if (mPinnedView.getParent() == null) {
            mOverlay.addView(mPinnedView);
        }

        mPinnedView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (mOnPinnedViewClickListener != null)
                        mOnPinnedViewClickListener.onClick(v);
                }
            });

        requestLayout();
    }

    @Override
    public boolean  onTouchEvent(MotionEvent event) {
        boolean res = mOverlay.dispatchTouchEvent(event);

        if (res) {
            requestLayout();
            return res;
        } else {
            return super.onTouchEvent(event);
        }
    }

    void        onScroll(final AbsListView listView,
                         final int firstVisibleItem,
                         final int visibleItemCount,
                         final int totalItemCount) {

        final StringBuilder sb = new StringBuilder();
        final SectionAdapter a = getSectionAdapter();

        if (a == null) {
            return;
        }

        sb.append("OnScroll:\n");

        new Runnable() {
            int spositions[];

            public void run() {
                spositions = a.getSectionsAtPosition(firstVisibleItem);

                translation();

                Integer prevPosition = (Integer) mPinnedView.getTag();

                if (prevPosition != null && prevPosition != spositions[1]) {
                    mPinnedView = a.getView(spositions[1], mPinnedView, mOverlay);
                    Log.d(TAG, "pinned-view = a.getView()");
                }

                mPinnedView.setTag(spositions[1]);

                log();
            }

            void log() {
                sb.append("  sections: " + Arrays.toString(spositions) + "\n");
                sb.append("  fvi: " + firstVisibleItem + " vic: " + visibleItemCount +
                          " tic:" + totalItemCount + "\n");
                sb.append("  pv: " + mPinnedView);

                SectionListView.this.log(sb.toString());
            }

            void    translation() {
                if (mPinnedView != null) {
                    hideCurrentSection();

                    int curPosition = spositions[2];
                    
                    if (isNextSectionVisible(curPosition)) {
                        showNextSections(curPosition);

                        View v = getNextSectionChild(spositions[2]);

                        if (v != null) {
                            int t, b;

                            t = v.getTop();
                            b = mPinnedView.getBottom();

                            sb.append("  t: " + t + " b: " + b + "\n");

                            if (t < b) {
                                mPinnedView.setTranslationY(-(b - t));

                                if (mOnPushedUpListener != null)
                                    mOnPushedUpListener.onPushedUp(mPinnedView, 1f - (b - t) / (float) b);
                                
                            } else {
                                mPinnedView.setTranslationY(0);
                                
                                if (mOnPushedUpListener != null)
                                    mOnPushedUpListener.onPushedUp(mPinnedView, 1f);
                            }
                        }

                    } else {
                        mPinnedView.setTranslationY(0);

                        if (mOnPushedUpListener != null)
                            mOnPushedUpListener.onPushedUp(mPinnedView, 1f);
                    }
                }
            }

            void    hideCurrentSection() {
                if (!mShowHideSections)
                    return;

                View v =  listView.getChildAt(0);
                int visibility = v.getVisibility();

                if (a.getItemViewType(firstVisibleItem) == SectionAdapter.SECTION_T) {

                    if (visibility != INVISIBLE)
                        v.setVisibility(INVISIBLE);

                } else {

                    if (visibility != VISIBLE)
                        v.setVisibility(VISIBLE);
                }
            }

            void    showNextSections(int curPosition) {
                if (!mShowHideSections)
                    return;

                while (curPosition != -1 &&
                       isNextSectionVisible(curPosition)) {

                    showNextSection(curPosition);

                    curPosition = a.getSectionsAtPosition(curPosition)[2];                      
                }
            }

            void   showNextSection(int curPosition) {
                if (!mShowHideSections)
                    return;

                View v =  getNextSectionChild(curPosition);
                int visibility = v.getVisibility();

                if (visibility != VISIBLE)
                    v.setVisibility(VISIBLE);
            }

            boolean isNextSectionVisible(int curPosition) {
                return ((firstVisibleItem < curPosition)
                        &&
                        (curPosition < firstVisibleItem + visibleItemCount));
            }

            View    getNextSectionChild(int curPosition) {
                return listView.getChildAt(curPosition - firstVisibleItem);
            }
        }.run();
    }

    SectionAdapter getSectionAdapter() {
        try {
            return (SectionAdapter) getAdapter();
        } catch (ClassCastException e){
            throw new RuntimeException("You should use SectionListView with SectionAdapter!");
        }
    }

    void        onScrollStateChanged(AbsListView view, int scrollState) {
        if (mPinnedView != null) {
            if (scrollState == OnScrollListener.SCROLL_STATE_FLING) {
                mPinnedView.setTranslationY(0);
            }
        }
    }

    //
    // Interface
    // 

    public void     setOnScrollListener (OnScrollListener l) {
        mOnScrollListener = l;
    }

    public void     setOnPushedUpListener(OnPushedUpListener l) {
        mOnPushedUpListener = l;
    }

    public void     setOnPinnedViewClickListener(OnClickListener l) {
        mOnPinnedViewClickListener = l;
    }

    public View     getPinnedView() {
        return mPinnedView;
    }

    public void     setShowHideSections(boolean trigger) {
        mShowHideSections = trigger;
    }

    public void     setShowDebugTextView(boolean show) {
        // remove debug-textview
        if (mDebugTextView != null) {
            mOverlay.removeView(mDebugTextView);
            mDebugTextView = null;
        }

        if (show) {
            initDebugTextView();
        }
    }

    // misc
    // ----

    void log(String what) {
        if (mDebugTextView != null) {
            mDebugTextView.setText(what);
            mDebugTextView.requestLayout();
            requestLayout();
        }

        Log.d(TAG, what);
    }

} // SectionListView
