package com.example.dt22.engine;

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

public class BtAdapter extends ArrayAdapter<ListItem> {
    public static final String DEF_ITEM_TYPE = "normal";
    public static final String TITLE_ITEM_TYPE = "title";
    public static final String DISCOVERY_ITEM_TYPE = "discovery";
    private List<ListItem> mainList; //список для адаптера
    private List<ViewHolder> listViewHolders;
    private SharedPreferences pref;


    public BtAdapter(@NonNull Context context, int resource, List<ListItem> btList) {
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

    //записать в память MAC отмеченного устройства
    private void savePref(int pos){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(BtConst.MAC_KEY, mainList.get(pos).getBtMac());
        editor.apply();
        Log.d("MyLog", "BtMac "+pref.getString(BtConst.MAC_KEY, "no bt selected"));
    }

    static class ViewHolder {
        TextView tvBtName;
        CheckBox chBtSelected;
    }

    // нарисовать устройства из существующего списка Bluetooth
    private View defaultItem(View convertView, int position, ViewGroup parent){
        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bt_list_item, null, false);
            viewHolder.tvBtName = convertView.findViewById(R.id.tvBtName);
            viewHolder.chBtSelected = convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
            listViewHolders.add(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvBtName.setText(mainList.get(position).getBtName());
        viewHolder.chBtSelected.setOnClickListener(v -> {
            for(ViewHolder holder : listViewHolders){
                holder.chBtSelected.setChecked(false);
            }
            viewHolder.chBtSelected.setChecked(true);
            savePref(position);
        });
        if(pref.getString(BtConst.MAC_KEY, "no bt selected").equals(mainList.get(position).getBtMac())){
            viewHolder.chBtSelected.setChecked(true);
        };
        return convertView;
    }

    // Разделение "Найденные устройства"
    private View titleItem(View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bt_list_item_title, null, false);
        }
        return convertView;
    }

}
































