package com.example.mapviewsample.provider;

import com.google.android.gms.maps.model.LatLng;

import com.example.mapviewsample.R;

import java.util.ArrayList;

public class StationProvider {

    public static final LatLng NEWYORK = new LatLng(40.714346, -74.005966);

    public static class StationInfo {
        public String line;
        public String title;
        public ArrayList<LatLng> entrances;
        public int panorama;
        public int icon;

        public StationInfo(String line, String title, ArrayList<LatLng> entrances, int panorama, int icon) {
            this.line = line;
            this.title = title;
            this.entrances = entrances;
            this.panorama = panorama;
            this.icon = icon;
        }
    }

    /**
     * MTA Subway data pulled from:
     * http://www.mta.info/developers/sbwy_entrance.html
     *
     * See here for more info:
     * http://www.mta.info/developers/
     */

    public static final StationInfo SEVENTY_SECOND_STOP = new StationInfo(
            "1, 2 and 3 trains",
            "72nd St Stop",
            new ArrayList<LatLng>() {{
                add(new LatLng(40.779146, -73.981822));
                add(new LatLng(40.778875, -73.981877));
                add(new LatLng(40.778585, -73.981923));
                add(new LatLng(40.778437, -73.981947));
            }},
            R.raw.pano4,
            R.drawable.ace
    );

    public static final StationInfo SEVENTY_NINTH_STOP = new StationInfo(
            "1, 2 and 3 trains",
            "79th St Stop",
            new ArrayList<LatLng>() {{
                add(new LatLng(40.783985, -73.979662));
                add(new LatLng(40.783620, -73.979910));
                add(new LatLng(40.784066, -73.980051));
                add(new LatLng(40.783765, -73.980274));
            }},
            R.raw.pano3,
            R.drawable.ace
    );

    public static final StationInfo FOURTEENTH_AND_8TH_STOP = new StationInfo(
            "A, C and E trains",
            "14th St and 8th Ave Stop",
            new ArrayList<LatLng>() {{
                add(new LatLng(40.741046, -74.001284));
                add(new LatLng(40.740944, -74.001402));
                add(new LatLng(40.741235, -74.001715));
                add(new LatLng(40.741234, -74.001732));
                add(new LatLng(40.740410, -74.001750));
                add(new LatLng(40.740938, -74.001919));
                add(new LatLng(40.740598, -74.002185));
                add(new LatLng(40.739776, -74.002256));
                add(new LatLng(40.739949, -74.002655));
            }},
            R.raw.pano2,
            R.drawable.ace
    );

    public static final StationInfo FOURTEENTH_AND_6TH_STOP = new StationInfo(
            "F and V trains",
            "14th St and 6th Ave Stop",
            new ArrayList<LatLng>() {{
                add(new LatLng(40.738678, -73.995632));
                add(new LatLng(40.738529, -73.995685));
                add(new LatLng(40.738838, -73.996023));
                add(new LatLng(40.738718, -73.996121));
                add(new LatLng(40.737286, -73.996437));
                add(new LatLng(40.737316, -73.996508));
                add(new LatLng(40.737519, -73.996970));
                add(new LatLng(40.737370, -73.997079));
                add(new LatLng(40.737572, -73.997111));
                add(new LatLng(40.737423, -73.997220));
            }},
            R.raw.pano1,
            R.drawable.ace
    );

    public static final ArrayList<StationInfo> STATIONS = new ArrayList<StationInfo>() {{
        add(SEVENTY_SECOND_STOP);
        add(SEVENTY_NINTH_STOP);
        add(FOURTEENTH_AND_8TH_STOP);
        add(FOURTEENTH_AND_6TH_STOP);
    }};
}
