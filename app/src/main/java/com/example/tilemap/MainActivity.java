package com.example.tilemap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private WebView map;
    private static String START_GEO = "25.220317,55.232048,13";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map = (WebView) findViewById(R.id.mapView);
        map.setVerticalScrollBarEnabled(false);
        map.getSettings().setJavaScriptEnabled(true);
        map.loadUrl("file:///android_asset/map_api.html");
        map.addJavascriptInterface(new MarkerWebInterface(getApplicationContext()), "android_callback");
        map.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String js = "initMap(" + START_GEO + ");";

                if (android.os.Build.VERSION.SDK_INT >= 19) {
                    map.evaluateJavascript(js, null);
                } else {
                    map.loadUrl("javascript: " + js);
                }

                List<Marker> markers = new ArrayList<>();
                markers.add(new Marker("70000001006275165", 55.288396, 25.263831));
                markers.add(new Marker("70000001006186603", 55.285606, 25.260066));
                markers.add(new Marker("70000001007008967", 55.318308, 25.24457));

                showMarkers(markers);
            }
        });
    }

    private void showMarkers(final List<Marker> markers) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (android.os.Build.VERSION.SDK_INT >= 19) {
                    map.evaluateJavascript(prepareMarkerMessage(markers), null);
                } else {
                    map.loadUrl("javascript: "+prepareMarkerMessage(markers));
                }
            }
        }, 600);
    }

    private String prepareMarkerMessage(List<Marker> markers) {
//        return getMarkersArray(markers) + "var lat;var lon;setMarkersAndAvatar(markers, 25.248772, 55.36103, 'https://cdn-new.flamp.us/default-avatar-m_100_100.png');";
        return getMarkersArray(markers) + "var lat; var lon; setMarkersAndAvatar(markers, null, null, null);";
    }

    public static StringBuilder getMarkersArray(List<Marker> markers) {
        final int optionsCount = 3;
        StringBuilder builder = new StringBuilder("var markers = new Array(" + markers.size() + ");");
        builder.append("for (var i = 0; i < " + markers.size() + "; i++) {markers[i] = new Array(" + optionsCount + ");} ");

        for (int i = 0; i < markers.size(); i++) {
            builder.append("markers[" + i + "][0] = " + markers.get(i).getLat() + ";");
            builder.append("markers[" + i + "][1] = " + markers.get(i).getLon() + ";");
            String[] ids = markers.get(i).getFilialIDs().substring(0, markers.get(i).getFilialIDs().length() - 1).split(",");
            builder.append("markers[" + i + "][2] = [");
            for (int j = 0; j < ids.length; j++) {
                builder.append("'" + ids[j] + "'");
                if (j + 1 < ids.length) builder.append(",");
            }
            builder.append("];");
        }
        return builder;
    }

    public class Marker {
        private String filial_id;
        private double lon;
        private double lat;
        private String filialIDs;

        public Marker(String filial_id, double lon, double lat) {
            this.filial_id = filial_id;
            this.lon = lon;
            this.lat = lat;
            this.filialIDs = filial_id;
        }

        public String getFilial_id() {
            return filial_id;
        }

        public void setFilial_id(String filial_id) {
            this.filial_id = filial_id;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public String getFilialIDs() {
            return filialIDs;
        }

        public void setFilialIDs(String filialIDs) {
            this.filialIDs = filialIDs;
        }
    }

    private class MarkerWebInterface {
        private Context context;

        MarkerWebInterface(Context c) {
            context = c;
        }

        @JavascriptInterface
        public void clickCopyright(final String url) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }

        @JavascriptInterface
        public void touchMarker(final String filialIds, final String lat, final String lon, final boolean isActive) {
            Toast.makeText(context, "Filial id is "+filialIds,Toast.LENGTH_LONG).show();
        }
    }
}