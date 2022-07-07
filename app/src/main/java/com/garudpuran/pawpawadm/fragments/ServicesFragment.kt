package com.garudpuran.pawpawadm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.garudpuran.pawpawadm.databinding.FragmentServicesBinding
import com.garudpuran.pawpawadm.models.ServiceModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pawpaw.Adapters.ServicesMainRcViewAdapter

class ServicesFragment : Fragment() {
private lateinit var _binding: FragmentServicesBinding
private val binding get() = _binding


    private lateinit var recyclerViewServices: RecyclerView
    private val dataBase = Firebase.database
    var serviceArray: MutableList<ServiceModel>  = mutableListOf()
    private var serviceModel = ServiceModel()
    private val args:ServicesFragmentArgs by navArgs()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServicesBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serviceType = args.serviceType

        val serviceRef = dataBase.getReference("Services").child(serviceType)
        serviceRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                serviceArray.clear()
                if (snapshot.exists()) {
                    for( i in snapshot.children){
                        serviceModel = i.getValue(ServiceModel::class.java)!!
                        serviceModel.serviceId = i.key.toString()
                        serviceArray.add(serviceModel)
                    }
                    recyclerViewServices.adapter?.notifyDataSetChanged()
                }
                else{

                    Toast.makeText(requireContext(),"Services not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),error.message, Toast.LENGTH_SHORT).show()
            }
        })
        recyclerViewServices = binding.servicesMainRcView
        recyclerViewServices.adapter = ServicesMainRcViewAdapter(requireContext(), serviceArray)

        binding.backBtn.setOnClickListener{
           findNavController().navigateUp()
        }

        binding.addServiceProviderItem.setOnClickListener {
          val action = ServicesFragmentDirections.actionServicesFragmentToAddServiceFragment(serviceType)
            findNavController().navigate(action)
        }
    }
    }

