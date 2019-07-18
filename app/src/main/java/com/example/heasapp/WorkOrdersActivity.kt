package com.example.heasapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps_ins.*
import kotlinx.android.synthetic.main.activity_work_orders.*
import kotlinx.android.synthetic.main.order_row.*

class WorkOrdersActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var locationListener: LocationListener?=null

    var locationManager: LocationManager?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_orders)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        textView_UserName.text=UserInfo.name


        //liste olusturduk

        //adapter ile recyclerView'a ekledik

        //We're going to show all Orders in RecyclerView row by row




        var url="http://localhost/Denemeler/HEASApp/workorder.php";

        //Web apiden cektiğimiz dataları yazabilmek icin Arraylist olusturduk

        var list=ArrayList<Inspections>()

        var rq=Volley.newRequestQueue(this)


        //Response geldiyse Response listner
        //error var ise Reponse.Error
        var jar=JsonArrayRequest(Request.Method.GET,url,null,
            Response.Listener {  response ->

                //Response olarak JSONARRAY GELECEK
                for(x in 0.. response.length()-1)
                {

                    //Web servisten donen bütün değerleri kullanarak objeler yaratıp onları ArrayListimize yerleştiriyoruz
                    list.add(Inspections(response.getJSONObject(x).getInt("id"),response.getJSONObject(x).getString("mobile"),
                        response.getJSONObject(x).getString("Inspection_type"),response.getJSONObject(x).getString("facility"),
                        response.getJSONObject(x).getString("facility_condition"),response.getJSONObject(x).getString("layers"),
                        //lattiude and long icin
                        response.getJSONObject(x).getDouble("lat"),response.getJSONObject(x).getDouble("lon"),
                        //date
                        response.getJSONObject(x).getString("startDate").toString()))


                }

                var adp=InspectionAdapter(this,list)
                recyclerView_order.layoutManager=LinearLayoutManager(this)
                recyclerView_order.adapter=adp


            },
            Response.ErrorListener {error ->

                Toast.makeText(applicationContext,"Error:"+error.message,Toast.LENGTH_LONG).show()
            })



            rq.add(jar)





    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager

        /**
        //konumu almak icin location listener kullanıyoruz
        locationListener=object:LocationListener {
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onProviderEnabled(provider: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onProviderDisabled(provider: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onLocationChanged(p0: Location?) {

                if (p0 != null) {
                    var userLocation = LatLng(p0!!.latitude, p0!!.longitude)
                    mMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                }
            }

        }




        //Güncel konuma erişmek
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,2,2f,locationListener)
         **/

        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Add a marker in Sydney and move the camera
        mMap.clear()
        val HEAS = LatLng(40.902613, 29.317083)
        val lastLocation=HEAS//locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if(lastLocation!=null) {
            var lastUserLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
            mMap.addMarker(MarkerOptions().position(lastUserLocation).title("Repairman").snippet("here is my location")
                )

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,17f))



        }


    }
    fun addMarker(view: View)
    {

        mMap.clear()
        val locationIns = LatLng(UserInfo.latidu, UserInfo.longti)

        var lastUserLocation = locationIns
        mMap.addMarker(MarkerOptions().position(lastUserLocation).title("Repairman").snippet("here is my location")
        )

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,17f))

    }

    fun goInspection(view:View)
    {

        var int= Intent(this,MapsInsActivity::class.java)

        startActivity(int)
    }
}
