package com.example.heasapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_LoginIn.setOnClickListener {
            //Select * from table where mobile=? and ....
            var url="http://localhost/Denemeler/HEASApp/login.php?mobile="+editText_phone.text.toString()+"&password="+editText_password.text.toString()

            var rq=Volley.newRequestQueue(this)

            var sr=StringRequest(Request.Method.GET,url,
                Response.Listener {  response ->
                    if(response=="Error")
                    {
                        Toast.makeText(this,"Wrong Mobile or Password",Toast.LENGTH_LONG).show()


                    }
                    else{

                        UserInfo.mobile=editText_phone.text.toString()

                        UserInfo.name=response

                        var int=Intent(this,MapsInsActivity::class.java)

                        startActivity(int)
                        Toast.makeText(this,"UserName:"+response,Toast.LENGTH_LONG).show()

                    }
                },
                Response.ErrorListener {  Err->
                    Toast.makeText(this,"Err"+Err.message,Toast.LENGTH_LONG).show()
                })
            rq.add(sr)
        }
    }
}
