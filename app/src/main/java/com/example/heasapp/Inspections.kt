package com.example.heasapp

import java.sql.Date

class Inspections {

    var Id:Int
    var mobile:String

    var Inspection_type:String

    var facility:String

    var facility_condition:String

    var layer:String


    var latitude:Double

    var long:Double

    var startDate:String
    //var StartDate:Date

    constructor(Id:Int,mobile:String,Inspection_type:String,facility:String,facility_condition:String,layer:String,lat:Double,lon:Double,date:String)
    {

        this.Id=Id

        this.mobile=mobile
        this.Inspection_type=Inspection_type

        this.facility=facility

        this.facility_condition=facility_condition

        this.layer=layer

        this.latitude=lat

        this.long=lon

        this.startDate=date
    }

}
