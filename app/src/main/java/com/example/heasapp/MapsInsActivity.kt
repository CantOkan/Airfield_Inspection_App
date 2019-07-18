package com.example.heasapp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
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
import kotlinx.android.synthetic.main.dialog_activity.*

class MapsInsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_ins)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        textview_user.text=UserInfo.name


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mymenu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        return super.onContextItemSelected(item)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener(myListener)



        if(Build.VERSION.SDK_INT<23)
        {
            var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            // Add a marker in Sydney and move the camera
            val HEAS = LatLng(40.902613, 29.317083)
            mMap.addMarker(MarkerOptions().position(HEAS).title("Marker in HEAS"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(HEAS))


        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION),123)
        }
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Add a marker in Sydney and move the camera
        val HEAS = LatLng(40.902613, 29.317083)
        mMap.addMarker(MarkerOptions().position(HEAS).title("Marker in HEAS").icon(BitmapDescriptorFactory.fromResource(R.drawable.repman1)))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HEAS,15f))


        //-----------------------------------------------------------------------------------------
        //Kendi lokasyounumuzu almak için
        /*  locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationListener=object:LocationListener{
            override fun onLocationChanged(p0: Location?) {

                if(p0!=null)
                {
                    var userLocation=LatLng(p0!!.latitude,p0!!.longitude)
                    mMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,17f))
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onProviderEnabled(provider: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onProviderDisabled(provider: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        } */

        //-----------------------------------------------------------------------------------------------


        show_inspection()

    }



    internal  lateinit var myDialog: Dialog
    lateinit var inspectionopt:TextView
    var array= arrayListOf<String>("Daytime","Nighttime","Special Inspection","Periodic Inspection")
    var inspection_type:String=""



    //Harita üzerinde uzun süre bir yer tıklandığında

    //A dialog is a small window that prompts the user to make a decision or enter additional information.
    // A dialog does not fill the screen and is normally used for modal events that require users to take an action before they can proceed.
    val myListener=object :GoogleMap.OnMapLongClickListener{
        override fun onMapLongClick(p0: LatLng?) {
        //parametre olarak koordinatları alıyor


            mMap.addMarker(MarkerOptions().position(p0!!).title("Arıza").icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow_icon)))

            Toast.makeText(applicationContext,"New Place", Toast.LENGTH_LONG).show()


            myDialog=Dialog(this@MapsInsActivity)

            myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            myDialog.setContentView(R.layout.dialog_activity)

            myDialog.setTitle("Inspection")

            val options= arrayOf("DayTime","NightTime","Special Inspection","Periodic Inspection")

            myDialog.Inspectionn_option.adapter=ArrayAdapter<String>(this@MapsInsActivity,android.R.layout.simple_list_item_1,options)


            myDialog.Inspectionn_option.onItemSelectedListener=object:AdapterView.OnItemSelectedListener,
                AdapterView.OnItemClickListener {
                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }


                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    inspection_type=options.get(position)
                }


            }

            //Dialog inspection button tıklandığında
            myDialog.textView_goDetails.setOnClickListener {
                var i =Intent(this@MapsInsActivity,DetailsActivity::class.java)

                if(p0.latitude!=null)
                {
                    i.putExtra("lat",p0.latitude.toString())
                    i.putExtra("long",p0.longitude.toString())
                }

                i.putExtra("Inspection",inspection_type)


                startActivity(i)

            }

            myDialog.show()



        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            //if user accept first and second permission
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }


    //WorkOrde buttonu tıklandığında layout değiştir
    fun goWorkOrder(view:View)
    {
        var int=Intent(this,WorkOrdersActivity::class.java)

        startActivity(int)
    }

    //Map üzerinde daha önceden oluşturulmuş inceptionları göster
    fun show_inspection()
    {



        var url="http://localhost/Denemeler/HEASApp/showmarkers.php";

        //Web apiden cektiğimiz dataları yazabilmek icin Arraylist olusturduk


        var rq= Volley.newRequestQueue(this)


        //Response geldiyse Response listner
        //error var ise Reponse.Error
        var jar= JsonArrayRequest(
            Request.Method.GET,url,null,
            Response.Listener { response ->

                //Response olarak JSONARRAY GELECEK
                for(x in 0.. response.length()-1)
                {
                    //Inspection'ın id,Lat,Lon Wep servisten çekip marker oluşuturken parametre olarak kulanıyoruz

                    val Insp = LatLng(response.getJSONObject(x).getDouble("lat"), response.getJSONObject(x).getDouble("lon"))
                    if(response.getJSONObject(x).getInt("workOrder")==1)
                    {
                        mMap.addMarker(MarkerOptions().position(Insp).title("Inspection Id: "+response.getJSONObject(x).getInt("id").toString()))

                    }
                    else if(response.getJSONObject(x).getInt("workOrder")==0){
                        mMap.addMarker(MarkerOptions().position(Insp).title("Inspection Id: "+response.getJSONObject(x).getInt("id").toString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.greenmarker2)))

                    }




                }







            },
            Response.ErrorListener { error ->

                Toast.makeText(applicationContext,"Error:"+error.message,Toast.LENGTH_LONG).show()
            })



        rq.add(jar)

    }
}
