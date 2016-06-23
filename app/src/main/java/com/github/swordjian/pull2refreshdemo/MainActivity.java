package com.github.swordjian.pull2refreshdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.swordjian.pull2refresh.PullToRefreshBase;
import com.github.swordjian.pull2refresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener2<RecyclerView> {

    private List<String> list;
    private RecyclerAdapter recyclerAdapter;
    private PullToRefreshRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (PullToRefreshRecyclerView) findViewById(R.id.recycler_view);

        list = new ArrayList<>();
        initList();

        recyclerAdapter = new RecyclerAdapter();
        recyclerView.setOnRefreshListener(this);
        recyclerView.getRefreshableView().setHasFixedSize(true);
        recyclerView.getRefreshableView().setAdapter(recyclerAdapter);
        recyclerView.getRefreshableView().setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.getRefreshableView().setItemAnimator(new DefaultItemAnimator());
    }

    public Activity getActivity() {
        return this;
    }

    public void initList() {
        for (int i = 0; i < 10; i++) {
            list.add("" + i);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        if (list.size() > 10){
            recyclerAdapter.itemRangeRemoved(10, list.size() - 10);
        }
        list.clear();
        initList();
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.onRefreshComplete();
                recyclerAdapter.notifyItemRangeChanged(0, list.size());
            }
        }, 1000);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        initList();
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.onRefreshComplete();
                recyclerAdapter.notifyItemRangeInserted(list.size(), 10);
            }
        }, 1000);
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View convertView;
            RecyclerView.ViewHolder holder = null;
            switch (viewType) {
                case 0:
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                    holder = new ViewHolder(convertView);
                    break;
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

            switch (getItemViewType(position)) {
                case 0:
                    final ViewHolder lViewHolder = (ViewHolder) viewHolder;
                    lViewHolder.text.setText(""+position);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        /**
         * 获取消息的类型
         */
        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        public void itemInserted(final int position) {
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemInserted(position);
                    }
                });

            } catch (Exception e) {
            }

        }

        public void itemChanged(final int position) {
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemChanged(position);
                    }
                });
            } catch (Exception e) {
            }

        }

        public void itemRangeRemoved(final int positionStart, final int itemCount) {
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemRangeRemoved(positionStart, itemCount);
                    }
                });
            } catch (Exception e) {

            }
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            View convertView;

            TextView text;

            public ViewHolder(View convertView) {
                super(convertView);

                this.convertView = convertView;
                text = (TextView) convertView.findViewById(R.id.text);

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = getAdapterPosition();
                    }
                });
            }
        }
    }
}
