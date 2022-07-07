package com.garudpuran.pawpawadm.models

data class ReviewModel(var reviewerProfilePicUrl:String ?=null,
                       var userName:String ?=null,
                       var RatingsGiven:Int ?=null,
                       var dateOfReview:String ?=null,
                       var reviewDescription:String ?=null )