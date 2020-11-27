package com.example.maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.Response;
import com.yandex.mapkit.search.SearchFactory;
import com.yandex.mapkit.search.Session;
import com.yandex.mapkit.Animation;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;


import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements Session.SearchListener, YandexGeoCoder.Callback { //IPGeoCoder.Callback {

    private static final String YANDEX_GEOCODER_API = "7892126b-5fa4-4594-b514-809c557b5210";

    private MapView mapView;
    private EditText sT;
    private YandexGeoCoder ygc;

    public void onSearchResponse(Response response) {
        MapObjectCollection mapObjects = mapView.getMap().getMapObjects();
        mapObjects.clear();
        for (GeoObjectCollection.Item searchResult : response.getCollection().getChildren()) {
            Point resultLocation = searchResult.getObj().getGeometry().get(0).getPoint();
            if (resultLocation != null) {
                mapView.getMap().move(
                        new CameraPosition(resultLocation, 14.0f, 0.0f, 0.0f));
                mapObjects.addPlacemark(resultLocation, ImageProvider.fromResource(mapView.getContext(), R.drawable.search_point));
                break;
            }
        }
    }

    @Override
    public void onSearchError(@NonNull Error error) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("72db52dc-5876-4afe-9478-31ae02cff945");
        MapKitFactory.initialize(this);
        SearchFactory.initialize(this);
        setContentView(R.layout.activity_main);
        mapView = (MapView)findViewById(R.id.mapView);
        sT = (EditText)findViewById(R.id.searchT);
        ygc = new YandexGeoCoder(this.YANDEX_GEOCODER_API, this);
        mapView.getMap().move(
                new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
        Toast.makeText(getApplicationContext(), "Добро пожаловать", Toast.LENGTH_LONG).show();
    }

    public void onSearchB(View view) throws IOException, JSONException {
            ygc.SearchObject(sT.getText().toString());

    }


    @Override
    public void onAddressSuccessfully(ArrayList<GeoPoint> points) {
        Toast.makeText(getApplicationContext(), String.format("%s, %s", points.get(0).getX(), points.get(0).getY()), Toast.LENGTH_LONG).show();
        mapView.getMap().move(
                new CameraPosition(new Point(points.get(0).getX(), points.get(0).getY()), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),null);

    }

    @Override
    public void onErrorAddress(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

}