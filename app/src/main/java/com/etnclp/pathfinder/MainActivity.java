package com.etnclp.pathfinder;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;

    private ArrayList<Coordinate> mCoordinates;
    private String mPolyline = "qcg{Fwhm|E}PpgAuYxcAkNvk@s[vj@ao@ll@cY`Rud@j^s_@`Ni\\`K";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Koordinat arraylist'i olusturalim.
         */
        createCoordinateList();


        /**
         * MAP 'i yerlestirelim.
         */
        SupportMapFragment frMap = new SupportMapFragment();
        /**
         * Class'a OnMapReadyCallback'i implement ettigimiz icin
         * burada this diyebiliyoruz.
         */
        frMap.getMapAsync(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.map, frMap)
                .commit();
    }

    private void createCoordinateList() {
        mCoordinates = new ArrayList<>();

        mCoordinates.add(new Coordinate("Mimar Sinan", 41.329372, 36.281878));
        mCoordinates.add(new Coordinate("Türkiş", 41.332242, 36.270267));
        mCoordinates.add(new Coordinate("Ömürevleri", 41.336511, 36.259261));
        mCoordinates.add(new Coordinate("Çobanlı", 41.338967, 36.252103));
        mCoordinates.add(new Coordinate("Atakent", 41.343550, 36.245097));
        mCoordinates.add(new Coordinate("Yeni Mahalle", 41.351236, 36.237825));
        mCoordinates.add(new Coordinate("Kurupelit", 41.355422, 36.234778));
        mCoordinates.add(new Coordinate("Pelitköy", 41.361447, 36.229756));
        mCoordinates.add(new Coordinate("Körfez", 41.366667, 36.227347));
        mCoordinates.add(new Coordinate("Üniversite", 41.371361, 36.225417));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        drawPath(mPolyline);
        setMarker();
    }

    private void setMarker() {
        for (Coordinate item : mCoordinates) {
            mGoogleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                    .position(new LatLng(item.Latitude, item.Longitude)));
        }

        /**
         * Ilk koordinata zoomlama yapalim.
         */
        Coordinate firstItem = mCoordinates.get(0);

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(firstItem.Latitude, firstItem.Longitude), 14));
    }

    public void drawPath(String polyline) {
        List<LatLng> list = decodePoly(polyline);

        for (int z = 0; z < list.size() - 1; z++) {
            LatLng src = list.get(z);
            LatLng dest = list.get(z + 1);

            mGoogleMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude))
                    .width(10)
                    .color(getResources().getColor(R.color.material_deep_teal_500))
                    .geodesic(true));
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
