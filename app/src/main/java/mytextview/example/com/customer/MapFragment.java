package mytextview.example.com.customer;


import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap mMap;
    LocationManager locationManager;
    private DatabaseReference mDatabase;
    private DatabaseReference rootRef;
   // public ArrayList<Shoplocation> shoplocation;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        rootRef =  FirebaseDatabase.getInstance().getReference("Shop");

       // SupportMapFragment supportMapFragment=(SupportMapFragment)getFragmentManager().findFragmentById(R.id.map1);
       // mapFragment.getmapAsync();
        return  v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)) {

            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    //Instantiate the class Latlng
                    LatLng latLng = new LatLng(latitude, longitude);
                    //Instantiate the class Geocoder
                    Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = addressList.get(0).getSubLocality() + ",";
                        str += addressList.get(0).getLocality() + ",";
                        str += addressList.get(0).getCountryName();
                       // mMap.addMarker(new MarkerOptions().position(latLng).title(str).icon(BitmapDescriptorFactory.fromResource(R.drawable.loc)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
        else if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude =location.getLatitude();
                    double longitude = location.getLongitude();
                    //Instantiate the class Latlng
                    LatLng latLng = new LatLng(latitude,longitude);
                    //Instantiate the class Geocoder
                    Geocoder geocoder=new Geocoder(getActivity().getApplicationContext());
                    try {
                        List<Address> addressList=geocoder.getFromLocation(latitude,longitude,1);
                        String str =addressList.get(0).getSubLocality()+",";
                        str += addressList.get(0).getLocality()+",";
                        str += addressList.get(0).getCountryName();
                       // mMap.addMarker(new MarkerOptions().position(latLng).title(str).icon(BitmapDescriptorFactory.fromResource(R.drawable.loc)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.2f));


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
        rootRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    Shoplocation shoplocation = child.getValue(Shoplocation.class);
                    LatLng latLng=new LatLng(Double.parseDouble(shoplocation.getLatitude()),Double.parseDouble(shoplocation.getLongitude()));

                    mMap.addMarker(new MarkerOptions().position(latLng).title(shoplocation.getShopname()));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

}
