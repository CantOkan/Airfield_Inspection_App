package com.example.heasapp



import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.order_row.view.*
import org.w3c.dom.Text

class InspectionAdapter(var con:Context,var list:ArrayList<Inspections>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        //Row Layoutumuzu RecyclerView içinde
        var my_view=LayoutInflater.from(con).inflate(R.layout.order_row,parent,false)

        return Orders(my_view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as Orders).bind(list[position].mobile,list[position].Inspection_type,list[position].startDate,list[position].facility,
            list[position].facility_condition,list[position].layer)

        holder.itemView.setOnClickListener {
            Toast.makeText(con,list[position].Inspection_type,Toast.LENGTH_LONG).show()



            UserInfo.latidu=list[position].latitude
            UserInfo.longti=list[position].long

            Toast.makeText(con,UserInfo.latidu.toString(),Toast.LENGTH_LONG).show()
        }

        holder.itemView.button_delete.setOnClickListener {
            Toast.makeText(con,"Inspection was deleted by you",Toast.LENGTH_LONG).show()


            var url="http://localhost/Denemeler/HEASApp/changeWorkOrder.php?id="+list[position].Id


            var rq=Volley.newRequestQueue(con)


            var str=StringRequest(Request.Method.GET,url,
                Response.Listener {

                    response ->

                    if("Changed"==response){
                        Toast.makeText(con,"Deleted",Toast.LENGTH_LONG).show()


                        var int=Intent(con,MapsInsActivity::class.java)

                        con.startActivity(int)
                    }
                    else
                    {
                        Toast.makeText(con,"Error Occured",Toast.LENGTH_LONG).show()

                    }

                },
                Response.ErrorListener {
                    error->
                    Toast.makeText(con,"Error MEssage:"+error.message,Toast.LENGTH_LONG).show()


                })

            rq.add(str)



        }

    }

}

class Orders(view:View):RecyclerView.ViewHolder(view)
{
    //View yani order_row icindeki textviewleri yakalıyoruz

    var tv_mobile=view.findViewById<TextView>(R.id.textView_mobile)

    var tv_ins=view.findViewById<TextView>(R.id.textView_inspectionType)

    var tv_date=view.findViewById<TextView>(R.id.textView_date)

    var tv_facility=view.findViewById<TextView>(R.id.textView_facility)

    var tv_facilitycond=view.findViewById<TextView>(R.id.textView_facilitycond)

    var tv_layer=view.findViewById<TextView>(R.id.textView_layers)

    fun bind(mob:String,ins:String,date:String,faci:String,facilitycond:String,layer:String)
    {
        tv_mobile.text=mob

        tv_ins.text=ins

        tv_date.text=date

        tv_facility.text=faci

        tv_facilitycond.text=facilitycond

        tv_layer.text=layer
    }






}