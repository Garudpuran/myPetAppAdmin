package com.garudpuran.pawpawadm.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.garudpuran.pawpawadm.DashboardSalesAdapter
import com.garudpuran.pawpawadm.DashboardServicesAdapter
import com.garudpuran.pawpawadm.adapters.DashboardBillboardAdapter
import com.garudpuran.pawpawadm.databinding.FragmentDashboardBinding
import com.garudpuran.pawpawadm.models.DashBoardSalesModel
import com.garudpuran.pawpawadm.models.DashboardBillBoardModel
import com.garudpuran.pawpawadm.models.DashboardServiceModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardFragment : Fragment() {
    private lateinit var _binding: FragmentDashboardBinding
    private val binding get() = _binding
    private lateinit var recyclerViewBillBoard: RecyclerView
    private lateinit var recyclerViewServices: RecyclerView
    private lateinit var recyclerViewSales: RecyclerView

    private val dataBase = Firebase.database
    var serviceArray: MutableList<DashboardServiceModel> = mutableListOf()
    private var serviceModel = DashboardServiceModel()

    var saleArray: MutableList<DashBoardSalesModel> = mutableListOf()
    private var saleModel = DashBoardSalesModel()

    var billboardArray: MutableList<DashboardBillBoardModel> = mutableListOf()
    private var billboardModel = DashboardBillBoardModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissions()
        binding.servicesAddBtn.setOnClickListener {
            addSalesOrServices("ServicesSquares")
        }

        binding.salesAddBtn.setOnClickListener {
            addSalesOrServices("SalesSquares")
        }

    }

    private fun addSalesOrServices(type: String) {
        val action =
            DashboardFragmentDirections.actionDashboardFragmentToCreateOrSelectSquareFragment(type)
        findNavController().navigate(action)
        Toast.makeText(requireContext(), type, Toast.LENGTH_SHORT).show()
    }

    private fun hasReadStoragePermission() = ActivityCompat.checkSelfPermission(
        requireActivity(),
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        var permissionsToRequest = mutableListOf<String>()
        if (!hasReadStoragePermission()) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest.toTypedArray(),
                0
            )
        } else {
            loadBillboards()
            loadServices()
            loadSales()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireActivity(), "Granted", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(
                requireActivity(),
                "Permissions are required for the app.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadBillboards() = CoroutineScope(Dispatchers.Default).launch {
        try {
            val billboardRef = dataBase.getReference("Billboards")
            CoroutineScope(Dispatchers.IO).launch {
                billboardRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        billboardArray.clear()
                        if (snapshot.exists()) {
                            for (i in snapshot.children) {
                                billboardModel = i.getValue(DashboardBillBoardModel::class.java)!!
                                billboardModel.billboardId = i.key.toString()
                                billboardArray.add(billboardModel)
                            }
                            recyclerViewBillBoard = binding.billBoardRcViewDashboard
                            recyclerViewBillBoard.adapter =
                                DashboardBillboardAdapter(requireContext(), billboardArray)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }.isCompleted
            withContext(Dispatchers.Main){
                binding.loadingLayer.visibility = View.GONE
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                binding.loadingLayer.visibility = View.GONE
                Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadServices() = CoroutineScope(Dispatchers.Default).launch {
        try {
            val serviceRef = dataBase.getReference("ServicesSquares")
            CoroutineScope(Dispatchers.IO).launch {
                serviceRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        serviceArray.clear()
                        if (snapshot.exists()) {
                            for (i in snapshot.children) {
                                serviceModel = i.getValue(DashboardServiceModel::class.java)!!
                                serviceModel.serviceCategoryId = i.key.toString()
                                serviceArray.add(serviceModel)
                            }
                            recyclerViewServices = binding.servicesRcViewDashboard
                            recyclerViewServices.adapter =
                                DashboardServicesAdapter(requireContext(), serviceArray)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }.isCompleted

            withContext(Dispatchers.Main) {
                binding.loadingLayer.visibility = View.GONE
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                binding.loadingLayer.visibility = View.GONE
                Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadSales() = CoroutineScope(Dispatchers.Default).launch {
        try {
            val salesRef = dataBase.getReference("SalesSquares")
            CoroutineScope(Dispatchers.IO).launch {
                salesRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        saleArray.clear()
                        if (snapshot.exists()) {
                            for (i in snapshot.children) {
                                saleModel = i.getValue(DashBoardSalesModel::class.java)!!
                                saleModel.salesCategoryId = i.key.toString()
                                saleArray.add(saleModel)
                            }
                            recyclerViewSales = binding.salesRcViewDashboard
                            recyclerViewSales.adapter =
                                DashboardSalesAdapter(requireContext(), saleArray)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }.isCompleted

            withContext(Dispatchers.Main) {
                binding.loadingLayer.visibility = View.GONE
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                binding.loadingLayer.visibility = View.GONE
                Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}