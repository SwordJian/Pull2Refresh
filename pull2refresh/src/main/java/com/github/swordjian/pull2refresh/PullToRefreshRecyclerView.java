/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.github.swordjian.pull2refresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {


    public PullToRefreshRecyclerView(Context context) {
        super(context);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshRecyclerView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshRecyclerView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    @Override
    public Orientation getPullToRefreshScrollDirection() {

        return Orientation.VERTICAL;
    }

    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        RecyclerView lRecyclerView;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            lRecyclerView = new InternalExpandableListViewSDK9(context, attrs);
        } else {
            lRecyclerView = new RecyclerView(context, attrs);
        }
        lRecyclerView.setId(R.id.recyclerview);
        return lRecyclerView;
    }

    @Override
    protected boolean isReadyForPullStart() {
        boolean isReadyForPullStart = false;
        RecyclerView lRecyclerView = mRefreshableView;
        if (lRecyclerView.getLayoutManager() != null) {
            if (lRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager lLayoutManager = (LinearLayoutManager) lRecyclerView.getLayoutManager();
                if (lLayoutManager.getItemCount() == 0) {
                    isReadyForPullStart = true;
                } else if (lLayoutManager.findFirstVisibleItemPosition() == 0
                        && lLayoutManager.getChildAt(0).getTop() >= 0) {
                    isReadyForPullStart = true;
                } else {
                    isReadyForPullStart = false;
                }
            } else if (lRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
                GridLayoutManager lLayoutManager = (GridLayoutManager) lRecyclerView.getLayoutManager();
                if (lLayoutManager.getItemCount() == 0) {
                    isReadyForPullStart = true;
                } else if (lLayoutManager.findFirstVisibleItemPosition() == 0
                        && lLayoutManager.getChildAt(0).getTop() >= 0) {
                    isReadyForPullStart = true;
                } else {
                    isReadyForPullStart = false;
                }
            }
        }
        return isReadyForPullStart;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        boolean isReadyForPullStart = false;
        RecyclerView lRecyclerView = mRefreshableView;
        if (lRecyclerView.getLayoutManager() != null) {
            if (lRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager lLayoutManager = (LinearLayoutManager) lRecyclerView.getLayoutManager();
                if (lLayoutManager.getItemCount() == 0) {
                    isReadyForPullStart = true;
                } else if (lLayoutManager.findLastVisibleItemPosition() == (lLayoutManager.getItemCount() - 1)) {
                    int last_first = lLayoutManager.findLastVisibleItemPosition() - lLayoutManager.findFirstVisibleItemPosition();
                    if (lLayoutManager.getChildAt(last_first) != null && lLayoutManager.getChildAt(last_first).getBottom() <= getMeasuredHeight()) {
                        isReadyForPullStart = true;
                    }
                }
            } else if (lRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
                GridLayoutManager lLayoutManager = (GridLayoutManager) lRecyclerView.getLayoutManager();
                if (lLayoutManager.getItemCount() == 0) {
                    isReadyForPullStart = true;
                } else if (lLayoutManager.findLastVisibleItemPosition() == (lLayoutManager.getItemCount() - 1)) {
                    int last_first = lLayoutManager.findLastVisibleItemPosition() - lLayoutManager.findFirstVisibleItemPosition();
                    if (lLayoutManager.getChildAt(last_first) != null && lLayoutManager.getChildAt(last_first).getBottom() <= getMeasuredHeight()) {
                        isReadyForPullStart = true;
                    }
                }
            }
        }
        return isReadyForPullStart;
    }

    @TargetApi(9)
    final class InternalExpandableListViewSDK9 extends RecyclerView {

        public InternalExpandableListViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                       int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

            final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                    scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
            OverscrollHelper.overScrollBy(PullToRefreshRecyclerView.this, deltaX, scrollX, deltaY, scrollY,
                    isTouchEvent);

            return returnValue;
        }
    }


}
