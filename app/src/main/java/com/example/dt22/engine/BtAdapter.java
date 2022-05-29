package com.example.dt22.engine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dt22.R;

import java.util.ArrayList;
import java.util.List;

public class BtAdapter extends ArrayAdapter<BtListItem> {
    public static final String DEF_ITEM_TYPE = "normal";
    public static final String TITLE_ITEM_TYPE = "title";
    public static final String DISCOVERY_ITEM_TYPE = "discovery";
    private List<BtListItem> mainList; //список для адаптера
    private List<ViewHolder> listViewHolders;
    private SharedPreferences pref;
    private boolean isDiscoveryType = false;


    public BtAdapter(@NonNull Context context, int resource, List<BtListItem> btList) {
        super(context, resource, btList);
        mainList = btList;
        listViewHolders = new ArrayList<>();
        pref = context.getSharedPreferences(BtConst.MY_PREF, Context.MODE_PRIVATE);
    }

    // Рисование списка устройств
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        switch (mainList.get(position).getItemType()){
            case TITLE_ITEM_TYPE:convertView = titleItem(convertView, parent);
                break;
            default: convertView = defaultItem(convertView, position, parent);
            break;
        }
        return convertView;
    }

    // Записать в память MAC отмеченного устройства
    private void savePref(int pos){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(BtConst.MAC_KEY, mainList.get(pos).getBtDevice().getAddress());
        editor.apply();
        Log.d("MyLog", "BtMac "+pref.getString(BtConst.MAC_KEY, "no bt selected"));
    }

    static class ViewHolder {
        TextView tvBtName;
        CheckBox chBtSelected;
    }

    // Нарисовать устройства из существующего списка Bluetooth
    @SuppressLint("MissingPermission")
    private View defaultItem(View convertView, int position, ViewGroup parent){
        ViewHolder viewHolder;
        boolean hasViewHolder = false;
        if(convertView!=null) hasViewHolder = (convertView.getTag() instanceof  ViewHolder);
        if(convertView == null || !hasViewHolder){ // Создаем список
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bt_list_item, null, false);
            viewHolder.tvBtName = convertView.findViewById(R.id.tvBtName);
            viewHolder.chBtSelected = convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
            listViewHolders.add(viewHolder);
        } else { // Перезаполняем список
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.chBtSelected.setChecked(false);
        }

        if (mainList.get(position).getItemType().equals(BtAdapter.DISCOVERY_ITEM_TYPE)){
            viewHolder.chBtSelected.setVisibility(View.GONE);
            isDiscoveryType = true;
        } else {
            viewHolder.chBtSelected.setVisibility(View.VISIBLE);
            isDiscoveryType = false;
        }
        viewHolder.tvBtName.setText(mainList.get(position).getBtDevice().getName());
        viewHolder.chBtSelected.setOnClickListener(v -> {
            if(!isDiscoveryType) {
                for (ViewHolder holder : listViewHolders) {
                    holder.chBtSelected.setChecked(false);
                }
                viewHolder.chBtSelected.setChecked(true);
                savePref(position);
            }
        });
        if(pref.getString(BtConst.MAC_KEY, "no bt selected").equals(mainList.get(position).getBtDevice().getAddress())){
            viewHolder.chBtSelected.setChecked(true);
        };
        isDiscoveryType = false;
        return convertView;
    }

    // Разделение "Найденные устройства"
    private View titleItem(View convertView, ViewGroup parent){
        boolean hasViewHolder = false;
        if(convertView!=null){ hasViewHolder = (convertView.getTag() instanceof ViewHolder); }
        if(convertView == null || hasViewHolder){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bt_list_item_title, null, false);
        }
        return convertView;
    }

}
































