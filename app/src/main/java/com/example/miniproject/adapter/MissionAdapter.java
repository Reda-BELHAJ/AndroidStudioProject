package com.example.miniproject.adapter;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.miniproject.R;
import com.example.miniproject.model.Mission;

import java.util.ArrayList;

public class MissionAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Mission> missions;

    public MissionAdapter(Context context, ArrayList<Mission> missions) {
        this.context = context;
        this.missions = missions;
    }

    @Override
    public int getCount() {
        return missions.size();
    }

    @Override
    public Object getItem(int position) {
        return missions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = View.inflate(context, R.layout.card_mission, null);
        }

        TextView title = convertView.findViewById(R.id.title);
        TextView type = convertView.findViewById(R.id.type);
        TextView description = convertView.findViewById(R.id.description);

        Mission mission = missions.get(position);

        title.setText(mission.getMissionName());
        type.setText(mission.getMissionType());
        description.setText(mission.getDescription());

        return convertView;
    }
}
