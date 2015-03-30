package com.example.ex12gps;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {
	
	private TextView latitude, longitude, distancia;
	private ImageView icon;
	private LocationManager lm;
	private String provider;
	GoogleMap map;
	Marker LocalAtual;
	LatLng Lpraca;
	LatLng Atual;
	Location latual;
	Location Ppraca;
	double dist;
	double distKM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		map=((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap() ;
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		
		
		latitude=(TextView) findViewById(R.id.latitude);
		longitude=(TextView) findViewById(R.id.longitude);
		distancia=(TextView) findViewById(R.id.distancia);
		
		icon=(ImageView) findViewById(R.id.icone);
		
		
		double lat=41.6556891;
		double lng=-0.8775525;
		Lpraca=new LatLng(lat,lng);
		Ppraca= new Location("");
		Ppraca.setLatitude(lat);
		Ppraca.setLongitude(lng);
		
		Marker Mpraca = map.addMarker(new MarkerOptions().position(Lpraca).title("Plaza del Pilar, Zaragoza").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
		
		
		
		
		
		lm= (LocationManager) getSystemService(Context. LOCATION_SERVICE);
		
		Criteria criterio=new Criteria();
		
		provider = lm.getBestProvider(criterio,false);
		
		Location latual=lm.getLastKnownLocation(provider);
		
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latual.getLatitude(),latual.getLongitude()), 15));
		
		Atual=new LatLng(latual.getLatitude(),latual.getLongitude());
		
		LocalAtual = map.addMarker(new MarkerOptions().position(Atual).title("Onde está atualmente").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
		
		latitude.setText(String.valueOf(latual.getLatitude()));
		longitude.setText(String.valueOf(latual.getLongitude()));
		Toast.makeText(getApplicationContext(), provider, Toast.LENGTH_SHORT).show();
		
		dist=latual.distanceTo(Ppraca);
		
		if (dist<=1000)
		{
			icon.setImageResource(R.drawable.green);
			distancia.setText(String.valueOf(dist) +"m");
		}
			
		else
		{
			icon.setImageResource(R.drawable.red);
			distancia.setText(String.valueOf(dist/1000) + "km");
		}
		
		

		
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location latual) {
		// TODO Auto-generated method stub
		LocalAtual.remove();
		Toast.makeText(getApplicationContext(), "mudou", Toast.LENGTH_SHORT).show();
		
		double lat = (latual.getLatitude());
		double lng = (latual.getLongitude());
		latitude.setText(String.valueOf(lat));
		longitude.setText(String.valueOf(lng));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latual.getLatitude(),latual.getLongitude()), 15));
		LocalAtual = map.addMarker(new MarkerOptions().position(new LatLng(latual.getLatitude(),latual.getLongitude())).title("Onde está atualmente").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
		
		dist=(double)Math.round((latual.distanceTo(Ppraca)) * 1) / 1;
		
		if (dist<=1000)
		{
			icon.setImageResource(R.drawable.green);
			distancia.setText(String.valueOf(dist) +"m");
		}
			
		else
		{
			icon.setImageResource(R.drawable.red);
			distancia.setText(String.valueOf(dist/1000) + "km");
		}
		
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(getApplicationContext(), "GPS ativado",Toast.LENGTH_LONG).show();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(getApplicationContext(), "GPS desativado",Toast.LENGTH_LONG).show();
		// TODO Auto-generated method stub
		
	}
	public void onResume(){
		super.onResume();
		lm.requestLocationUpdates(provider,400,1,this);
	}
	
	public void onPause(){
		super.onPause();
		lm.removeUpdates(this);
	}
	
	
	
}
