package com.example.wifianalyzer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeviceListAdapter extends
        RecyclerView.Adapter<com.example.wifianalyzer.DeviceListAdapter.ViewHolder> {
    List<com.example.wifianalyzer.Device> list;
    private LayoutInflater mInflater;

    public DeviceListAdapter(Context context, List<com.example.wifianalyzer.Device> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public com.example.wifianalyzer.DeviceListAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.device_list, parent, false);
        return new com.example.wifianalyzer.DeviceListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull com.example.wifianalyzer.DeviceListAdapter.ViewHolder holder, int position) {
        com.example.wifianalyzer.Device device = list.get(position);
        holder.ip.setText(device.getIp());
        holder.name.setText(device.getDevice());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView name, mac, ip, marque;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.tv);
            name = itemView.findViewById(R.id.name_device);
            mac = itemView.findViewById(R.id.mac);
            ip = itemView.findViewById(R.id.ip);
            marque = itemView.findViewById(R.id.marque);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
