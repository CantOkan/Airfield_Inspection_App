package com.example.heasapp

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap



    //for mysql

    var InsType:String=""
    var InsCondition:String=""
    var Ins_layer:String=""

    var Ins_Status:Int=1 //daha Sonra değiştir

    var WorkOrder:Int=0
    var NotamStatus:Int=0


    var lat:String="0.0"
    var long:String="0.0"

    //---
    var cond1= arrayListOf<String>("Pavement lip over 3 inch ","Hole-5inch diameter 3 inch deep" ,"Cracking/Spalling/Heaves","FOD Gravel/Debris/Sand")
    var cond2= arrayListOf<String>("x","xx")



    val Facilities= arrayListOf<String>("Pavement","Safety Areas","Markings","Signage","Lighting","Navigotional Aids")

    val layer= arrayListOf<String>("Layer1","Layer2")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        /*
           val transcation=supportFragmentManager.beginTransaction()
        val fragment=fragment1()
        transcation.replace(R.id.fragment_holder,fragment)
        val bundle=Bundle()
        bundle.putString("str","I am coming from Fragment_01")

        fragment.arguments=bundle
        transcation.addToBackStack(null)
        transcation.commit()

        */

       /** Spinnerlar için array adapter **/


        //layer

        Inspectionn_layer.adapter= ArrayAdapter<String>(this@DetailsActivity,android.R.layout.simple_list_item_1,layer)
        Inspectionn_layer.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Ins_layer=layer.get(position)
            }

        }




        Inspectionn_facility.adapter=
            ArrayAdapter<String>(this@DetailsActivity,android.R.layout.simple_list_item_1,Facilities)


        //Spinnerdan hangi item selected
        Inspectionn_facility.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                InsType=Facilities.get(position)

                //her Facilityinin ayrı Durumları var bu ayrı durumları yine array listte tuttum ve
                //ikinci spinnera array list ile atadım.
                if(Facilities.get(0)==InsType)
                {
                    //
                    Inspectionn_condition.adapter=ArrayAdapter<String>(this@DetailsActivity,android.R.layout.simple_list_item_1,cond1)

                    Inspectionn_condition.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                            InsCondition=cond1.get(position)
                        }

                    }
                }
                else if(Facilities.get(1)==InsType){
                    Inspectionn_condition.adapter=ArrayAdapter<String>(this@DetailsActivity,android.R.layout.simple_list_item_1,cond2)


                    Inspectionn_condition.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                            InsCondition=cond2.get(position)
                        }

                    }

                }

            }

        }

        /*** Switchker **/
        workOrder_switch.setOnClickListener {
            if(workOrder_switch.isChecked)
            {
                WorkOrder=1
                Toast.makeText(this@DetailsActivity,"Checked",Toast.LENGTH_LONG).show()
            }
            else{
                WorkOrder=0
                Toast.makeText(this@DetailsActivity,"Not Checked",Toast.LENGTH_LONG).show()

            }

        }

        notam_switch.setOnClickListener{
            if(notam_switch.isChecked)
            {
                NotamStatus=1
            }
            else{
                NotamStatus=0
            }
        }


        var ins_type=intent.getStringExtra("Inspection")
        textView_inspectionType.setText(ins_type)


        textview_user.text=UserInfo.mobile




    }

    override fun onMapReady(googleMap: GoogleMap) {


        //Yaratmış olduğumu inspetion'ı DetailsActivity görmek için
        //intentden parametreleri çekiyoruz
        lat=intent.getStringExtra("lat")

        long=intent.getStringExtra("long")

        //.3f
        textView_lat.setText(lat)
        textView_log.setText(long)
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val insp = LatLng(lat.toDouble(), long.toDouble())
        mMap.addMarker(MarkerOptions().position(insp).title("Coordinates :"+lat+" : "+long).icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow_icon)))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(insp,17f))
    }

    fun addInsp(view:View)
    {
        //http://192.168.1.21/Denemeler/HEASApp/inspection.php?mobile=535468&Inspection_type=deneme2&facility=deneme2&
        // facility_condition=deneme2&layers=deneme2&inspection_status=1&workOrder=1&notamStatus=0&lat=32.30&lon=42.30


        var url="http://localhost/Denemeler/HEASApp/inspection.php?mobile="+UserInfo.mobile+"&Inspection_type="+textView_inspectionType.text+
                "&facility="+InsType+"&facility_condition="+InsCondition+"&layers="+Ins_layer+"&inspection_status="+Ins_Status+
                "&workOrder="+WorkOrder+"&notamStatus="+NotamStatus+"&lat="+lat.toDouble()+"&lon="+long.toDouble()


        var rq=Volley.newRequestQueue(this)

        var sr=StringRequest(Request.Method.GET,url,
            Response.Listener {

                response ->
                if(response=="Saved")
                {
                    Toast.makeText(applicationContext,"Your order has been sent",Toast.LENGTH_LONG).show()
                    var int=Intent(this,WorkOrdersActivity::class.java)
                    startActivity(int)

                }
                else{
                    Toast.makeText(applicationContext,"check everythin again",Toast.LENGTH_LONG).show()

                }

        },Response.ErrorListener {  err->

                Toast.makeText(applicationContext,"ERRRORR:"+err.message,Toast.LENGTH_LONG).show()


            })

        rq.add(sr)





    }
}
