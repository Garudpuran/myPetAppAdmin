package com.garudpuran.pawpawadm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.garudpuran.pawpawadm.databinding.FragmentSelectedServicesDetailBinding
import com.garudpuran.pawpawadm.models.ServiceModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SelectedServicesDetailFragment : Fragment() {
    private lateinit var _binding: FragmentSelectedServicesDetailBinding
    private val binding get() = _binding

    private val dataBase = Firebase.database
    var serviceArray: MutableList<ServiceModel> = mutableListOf()
    private var serviceModel = ServiceModel()
    private val args: SelectedServicesDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedServicesDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val serviceId = args.serviceId
        val serviceType = args.typeOfService
        val serviceRef = dataBase.getReference("Services").child(serviceType).child(serviceId)
        try {
            serviceRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    serviceArray.clear()
                    if (snapshot.exists()) {
                        serviceModel = snapshot.getValue(ServiceModel::class.java)!!
                        Glide.with(requireContext()).load(serviceModel.serviceProfilePicUrl)
                            .into(binding.serviceProfilePic)
                        binding.providerTitle.text = serviceModel.title
                        binding.providerLocationTv.text = serviceModel.address
                        binding.descriptionTv.text = serviceModel.description
                        binding.NoticeBoardTv.text = serviceModel.noticeBoard
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Services not found",
                Toast.LENGTH_SHORT
            ).show()

        }

    }


}