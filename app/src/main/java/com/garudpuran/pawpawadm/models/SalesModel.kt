package com.garudpuran.pawpawadm.models

data class SalesModel(var salesGalleryPicUrls: List<PicModel>? = null,
                      var salesProfilePicUrl:String? = null,
                      var title: String? = null,
                      var ratings: String? = null,
                      var noOfRatings: String? = null,
                      var noticeBoard: String? = null,
                      var reviews: List<ReviewModel>? = null,
                      var salesId: String? = null,
                      var description: String? = null,
                      var salesCategory: String? = null,
                      var mrp :String? = null,
                      var postDate : String?=null

)
