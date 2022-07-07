package com.garudpuran.pawpawadm.fragments

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.garudpuran.pawpawadm.R
import com.garudpuran.pawpawadm.adapters.AddServiceProviderGalleryRcViewAdapter
import com.garudpuran.pawpawadm.databinding.FragmentAddServiceBinding
import com.garudpuran.pawpawadm.models.PicModel
import com.garudpuran.pawpawadm.models.ServiceModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*


class AddServiceFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var _binding: FragmentAddServiceBinding
    private val binding get() = _binding
    private lateinit var day: String
    private var profUri: Uri? = null
    private val REQUEST_CODE_IMAGE_PICK: Int = 11
    private lateinit var serviceProviderGalleryRcView: RecyclerView
    private var profilePicUrl: String? = null
    private var openingTime: Boolean = false
    val PICK_IMAGES_MULTIPLE_CODE: Int = 12
    private var galleryImgList: MutableList<Uri>? = mutableListOf()
    private val args: AddServiceFragmentArgs by navArgs()
    private var currentTime: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val storage = Firebase.storage
        val storageRef = storage.reference
        val serviceType = args.serviceType


// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.weeks_day_array,
            android.R.layout.simple_list_item_1
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.daysOfWeek.adapter = adapter
        }
        binding.daysOfWeek.onItemSelectedListener = this

        binding.addOpeningTimeBtn.setOnClickListener {
            openingTime = true
            val timePicker = TimePickerDialog(
                // pass the Context
                requireContext(),
                // listener to perform task
                // when time is picked
                timePickerDialogListener,
                // default hour when the time picker
                // dialog is opened
                12,
                // default minute when the time picker
                // dialog is opened
                10,
                // 24 hours time picker is
                // false (varies according to the region)
                false
            )

            // then after building the timepicker
            // dialog show the dialog to user
            timePicker.show()


        }
        binding.addClosingTimeBtn.setOnClickListener {
            openingTime = false
            val timePicker = TimePickerDialog(
                // pass the Context
                requireContext(),
                // listener to perform task
                // when time is picked
                timePickerDialogListener,
                // default hour when the time picker
                // dialog is opened
                12,
                // default minute when the time picker
                // dialog is opened
                10,
                // 24 hours time picker is
                // false (varies according to the region)
                false
            )

            // then after building the timepicker
            // dialog show the dialog to user
            timePicker.show()
        }

        binding.addProfilePicBtn.setOnClickListener {
            // getContent.launch("image/*")

            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(it, REQUEST_CODE_IMAGE_PICK)
            }

        }
        binding.addGalleryPicsBtn.setOnClickListener {
            pickImagesMultiple()
        }

        /* binding.serviceLocationEt.setOnClickListener {
             val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=your location"))
             startActivity(intent)
         }*/
        binding.createNewServiceBtn.setOnClickListener {

            val serviceTitle = binding.providerTitleEt.text
            val serviceDescription = binding.descriptionEt.text
            val serviceNotice = binding.NoticeEt.text
            val serviceAddress = binding.serviceLocationEt.text

            if (serviceTitle.isNullOrEmpty() ||
                serviceAddress.isNullOrEmpty() ||
                serviceNotice.isNullOrEmpty() ||
                serviceDescription.isNullOrEmpty() ||
                galleryImgList == null ||
                profUri == null
            ) {
                Toast.makeText(
                    requireContext(),
                    "Please provide all the fields.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                currentTime = Calendar.getInstance().timeInMillis.toString()
                val providerProfileImagesRef = storageRef.child("Services").child(serviceType)
                    .child(currentTime.toString()).child("ProfilePic")
                providerProfileImagesRef.putFile(profUri!!).continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    } else {
                        providerProfileImagesRef.downloadUrl
                    }

                }.addOnCompleteListener {
                    profilePicUrl = it.result.toString()
                    val galleryUploadedPicUrlStringList = mutableListOf<PicModel>()
                    for (i in galleryImgList!!) {
                        val currTime = currentTime.toString()
                        val picModel = PicModel()
                        val providerProfileImagesRef =
                            storageRef.child("Services").child(serviceType)
                                .child(currTime)
                        providerProfileImagesRef.putFile(i).addOnCompleteListener {
                            picModel.id = currTime
                            picModel.picUrl = providerProfileImagesRef.downloadUrl.toString()
                            galleryUploadedPicUrlStringList.add(picModel)
                        }
                    }
                    // we get the list of galleryPicModels , the form is filled
                    // assign this to the model
                    val serviceModel = ServiceModel()
                    serviceModel.title = serviceTitle.toString()
                    serviceModel.galleryPicList = galleryUploadedPicUrlStringList
                    serviceModel.address = serviceAddress.toString()
                    serviceModel.noticeBoard = serviceNotice.toString()
                    serviceModel.serviceProfilePicUrl = profilePicUrl
                    serviceModel.description = serviceDescription.toString()
                    serviceModel.serviceCategory = serviceType

                    val fbDb = FirebaseDatabase.getInstance()
                    fbDb.reference.child("Services").child(serviceType).push()
                        .setValue(serviceModel).addOnCompleteListener { it1 ->
                        if (it1.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Service provider creator successfully.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Task not completed.Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    }

                }


                /*  val serviceModel = DataClassForSelectedService()
                  serviceModel.titleTxt = serviceTitle.toString()
                  serviceModel.Ratings = "Empty"
                  serviceModel.galleryPicList = galleryImgList.toList()*/

            }

        }
    }

    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute -> // logic to properly handle
            // the picked timings by user
            val formattedTime: String = when {
                hourOfDay == 0 -> {
                    if (minute < 10) {
                        "${hourOfDay + 12}:0${minute} am"
                    } else {
                        "${hourOfDay + 12}:${minute} am"
                    }
                }
                hourOfDay > 12 -> {
                    if (minute < 10) {
                        "${hourOfDay - 12}:0${minute} pm"
                    } else {
                        "${hourOfDay - 12}:${minute} pm"
                    }
                }
                hourOfDay == 12 -> {
                    if (minute < 10) {
                        "${hourOfDay}:0${minute} pm"
                    } else {
                        "${hourOfDay}:${minute} pm"
                    }
                }
                else -> {
                    if (minute < 10) {
                        "${hourOfDay}:${minute} am"
                    } else {
                        "${hourOfDay}:${minute} am"
                    }
                }
            }

            //set the formatted time in the button
            if (openingTime) {
                binding.addOpeningTimeBtn.text = "Opens At: $formattedTime"
            } else {
                binding.addClosingTimeBtn.text = "Closes At: $formattedTime"
            }

        }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        day = binding.daysOfWeek.getItemAtPosition(p2).toString()
        if (day == "Unspecified") {
            binding.addClosedDayTv.text = "Add Closed Day"
        } else {
            binding.addClosedDayTv.text = "Closed on:"
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK) {
            data?.data?.let {
                profUri = it
                binding.profilePicImgV.setImageURI(profUri)
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGES_MULTIPLE_CODE) {
            data?.data?.let {
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imgUri = data.clipData!!.getItemAt(i).uri
                    galleryImgList?.add(imgUri)
                }
                serviceProviderGalleryRcView = binding.serviceProviderGalleryRcView
                serviceProviderGalleryRcView.adapter =
                    AddServiceProviderGalleryRcViewAdapter(galleryImgList)
                Toast.makeText(
                    requireContext(),
                    "onImageResult:Number of image picked " + galleryImgList?.size,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun pickImagesMultiple() {
        val intent = Intent().also {
            it.type = "image/*"
            it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            it.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(it, PICK_IMAGES_MULTIPLE_CODE)

        }
    }


}
