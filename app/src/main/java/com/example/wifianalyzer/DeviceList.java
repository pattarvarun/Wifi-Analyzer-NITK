package com.example.wifianalyzer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.os.Looper.getMainLooper;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.example.wifianalyzer.DeviceList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceList extends Fragment implements WifiP2pManager.PeerListListener,
        WifiP2pManager.ChannelListener {
    RecyclerView recyclerView;
    List<com.example.wifianalyzer.Device> list = new ArrayList<>();
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    IntentFilter intentFilter = new IntentFilter();
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

    private WifiP2pDevice device;
    BroadcastReceiver receiver, mWifiScanReceiver;
    DeviceListAdapter deviceListAdapter;
    private List _peers = new ArrayList();
    private WiFiDirectBroadcastReceiver _broadcastReceiver = null;

    private WifiP2pManager.PeerListListener _peerListListener;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeviceList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeviceList.
     */
    // TODO: Rename and change types and number of parameters
    public static com.example.wifianalyzer.DeviceList newInstance(String param1, String param2) {
        com.example.wifianalyzer.DeviceList fragment = new com.example.wifianalyzer.DeviceList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


    }

    @Override
    public void onResume() {
        super.onResume();
        //   Toast.makeText(getContext(), "No  found",Toast.LENGTH_LONG).show();

        _broadcastReceiver = new WiFiDirectBroadcastReceiver(manager, channel, this,
                _peerListListener);
        getContext().registerReceiver(mWifiScanReceiver, intentFilter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getContext(), "On start", Toast.LENGTH_LONG).show();

        _broadcastReceiver = new WiFiDirectBroadcastReceiver(manager, channel, this,
                _peerListListener);
        getContext().registerReceiver(mWifiScanReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_device_list, container, false);
        TextView name, number_devices;
        name = root.findViewById(R.id.name_wifi);
        number_devices = root.findViewById(R.id.number_devices);
        recyclerView = root.findViewById(R.id.liste_device);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        manager = (WifiP2pManager) getContext().getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(getContext(), getMainLooper(), this);
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this, _peerListListener);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        manager = (WifiP2pManager) getContext().getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(getContext(), getMainLooper(), this);
        if (channel == null) {
            Toast.makeText(getContext(), "channel nul", Toast.LENGTH_LONG).show();

        }

        /*int permissionCheck = getActivity().checkSelfPermission( Manifest.permission.ACCESS_FINE_LOCATION);
        if (!(permissionCheck == PackageManager.PERMISSION_GRANTED)) {
            // User may have declined earlier, ask Android if we should show him a reason
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                // show an explanation to the user
                // Good practise: don't block thread after the user sees the explanation, try again to request the permission.
            } else {
                // request the permission.
                // CALLBACK_NUMBER is a integer constants
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                // The callback method gets the result of the request.
            }
        } else {
            managePeers();
        }*/

        managePeers();

        deviceListAdapter = new DeviceListAdapter(getContext(), list);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(deviceListAdapter);
        List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

        return root;
    }

    @SuppressLint("MissingPermission")
    public void managePeers(){
        manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {

                for (WifiP2pDevice device : wifiP2pDeviceList.getDeviceList()) {
                    Toast.makeText(getContext(),
                            wifiP2pDeviceList.getDeviceList().size() + "jajajajaj",
                            Toast.LENGTH_LONG).show();
                    peers.add(device);
                    list.add(new com.example.wifianalyzer.Device(device.primaryDeviceType,
                            device.deviceName, device.deviceAddress));

                    if (device.deviceName.equals("ABC")) {
                        Toast.makeText(getContext(),
                                "widi" + wifiP2pDeviceList.getDeviceList().size(),
                                Toast.LENGTH_LONG).show();
                    }
                    // device.deviceName
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
//        getContext().unregisterReceiver(_broadcastReceiver);
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {

        peers.clear();
        peers.addAll(peerList.getDeviceList());
        Toast.makeText(getContext(), peerList.getDeviceList().size() + "jajajajaj",
                Toast.LENGTH_LONG).show();

        ((DeviceListAdapter) deviceListAdapter).notifyDataSetChanged();
        if (peers.size() == 0) {
            return;
        }
    }

    @Override
    public void onChannelDisconnected() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        Log.d("devicelist",requestCode+"!"+grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    managePeers();
                } else {
                    //requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                break;
            }
            case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    _broadcastReceiver.discoverPeers();
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                }
                break;
            }
            case 3: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    _broadcastReceiver.requestPeers();
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);
                }
                break;
            }

            // other 'case' statements for other permssions
        }
    }
}
