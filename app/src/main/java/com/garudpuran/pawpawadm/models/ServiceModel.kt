package com.garudpuran.pawpawadm.models

data class ServiceModel(
    var serviceProfilePicUrl: String? = null,
    var title: String? = null,
    var ratings: String? = null,
    var address: String? = null,
    var noOfRatings: String? = null,
    var galleryPicList: List<PicModel>? = null,
    var noticeBoard: String? = null,
    var reviews: List<ReviewModel>? = null,
    var serviceId: String? = null,
    var description: String? = null,
    var serviceCategory: String? = null
)