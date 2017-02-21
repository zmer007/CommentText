package robu.dfer.commenttext;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowerFragment extends Fragment {

    RecyclerView mRecordListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flower, container, false);
        mRecordListView = (RecyclerView) view.findViewById(R.id.flower_recyclerView);
        List<FlowerItem> mockData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ArrayList<String> mockComments = new ArrayList<>();
            int rand = (int) (Math.random() * 10);
            for (int j = 0; j < rand; j++) {
                mockComments.add("comment" + j);
            }
            mockData.add(new FlowerItem(mockComments));
        }
        FlowerAdapter adapter = new FlowerAdapter(mockData);
        LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecordListView.setHasFixedSize(true);
        mRecordListView.setLayoutManager(layoutManager);
        mRecordListView.setAdapter(adapter);
        return view;
    }

    public void smoothScrollBy(int dy){
        mRecordListView.smoothScrollBy(0, dy);
    }
}
