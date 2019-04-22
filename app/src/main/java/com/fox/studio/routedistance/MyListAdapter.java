package com.fox.studio.routedistance;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;


public class MyListAdapter extends RealmBaseAdapter<RouteModel> implements ListAdapter {

    public MyListAdapter(@Nullable OrderedRealmCollection<RouteModel> data) {
        super(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list, parent, false);
        }

        TextView tvFrom = convertView.findViewById(R.id.tvFrom);
        TextView tvTo = convertView.findViewById(R.id.tvTo);
        TextView tvDate = convertView.findViewById(R.id.tvDate);

        RouteModel model = getItem(position);
        tvFrom.setText("From point: " + model.getFromPointLat() + ", " + model.getFromPointLon());
        tvTo.setText("To point: " + model.getToPointLat() + ", " + model.getToPointLon());
        tvDate.setText(model.getDate().toString());


        return convertView;
    }
}
