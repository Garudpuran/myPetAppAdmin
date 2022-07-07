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
import com.garudpuran.pawpawadm.databinding.FragmentSalesBinding
import com.garudpuran.pawpawadm.models.SalesModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pawpaw.Adapters.SalesMainRcViewAdapter


class SalesFragment : Fragment() {
    private lateinit var _binding: FragmentSalesBinding
    private val binding get() = _binding

    private lateinit var recyclerViewSales: RecyclerView
    private val dataBase = Firebase.database
    var salesArray: MutableList<SalesModel>  = mutableListOf()
    private var salesModel = SalesModel()
    private val args:SalesFragmentArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentSalesBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val salesType = args.salesType

        val serviceRef = dataBase.getReference("Sales").child(salesType)
        serviceRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               salesArray.clear()
                if (snapshot.exists()) {
                    for( i in snapshot.children){
                        salesModel = i.getValue(SalesModel::class.java)!!
                        salesModel.salesId = i.key.toString()
                        salesArray.add(salesModel)
                    }
                    recyclerViewSales.adapter?.notifyDataSetChanged()
                }
                else{

                    Toast.makeText(requireContext(),"Sales not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),error.message, Toast.LENGTH_SHORT).show()
            }
        })
        recyclerViewSales = binding.salesMainRcView
        recyclerViewSales.adapter = SalesMainRcViewAdapter(requireContext(), salesArray)

        binding.addSalesProviderItem.setOnClickListener {
            val action = ServicesFragmentDirections.actionServicesFragmentToAddServiceFragment(salesType)
            findNavController().navigate(action)
        }




    }



}