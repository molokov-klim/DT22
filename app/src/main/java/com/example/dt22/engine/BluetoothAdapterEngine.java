package com.example.dt22.engine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dt22.R;

import java.util.List;

public class BluetoothAdapterEngine extends ArrayAdapter<ListBluetoothItemEngine> {

    private List<ListBluetoothItemEngine> mainList;


    public BluetoothAdapterEngine(@NonNull Context context, int resource, List<ListBluetoothItemEngine> bluetoothList) {
        super(context, resource, bluetoothList);
        mainList = bluetoothList;
    }

    // Заполнение списка
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_list_item, null, false);
            viewHolder.tvBluetoothName = convertView.findViewById(R.id.tvBluetoothName);
            viewHolder.chBluetoothSelected = convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        };

        viewHolder.tvBluetoothName.setText(mainList.get(position).getBluetoothName());
        //viewHolder.chBluetoothSelected.setChecked(true);


        return convertView;
    }

    static class ViewHolder{
        TextView tvBluetoothName;
        CheckBox chBluetoothSelected;
    }




}
























