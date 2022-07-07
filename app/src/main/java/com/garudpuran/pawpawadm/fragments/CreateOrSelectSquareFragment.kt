package com.garudpuran.pawpawadm.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.garudpuran.pawpawadm.databinding.FragmentCreateOrSelectSquareBinding
import com.garudpuran.pawpawadm.models.DashboardServiceModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*


class CreateOrSelectSquareFragment : Fragment() {

    private lateinit var _binding: FragmentCreateOrSelectSquareBinding
    private val binding get() = _binding

    private val REQUEST_CODE_IMAGE_PICK: Int = 11
    private var picUri: Uri? = null
    private val dataBase = Firebase.database
    private val storageRef = Firebase.storage.reference
    private var typeSelected: String? = null
    private val args: CreateOrSelectSquareFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCreateOrSelectSquareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        typeSelected = args.typeOfSquare



        binding.squareImgView.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/.png"
                startActivityForResult(it, REQUEST_CODE_IMAGE_PICK)
            }

        }

        binding.CreateSquareBtn.setOnClickListener {
            if (picUri != null && !binding.servicesTitleTv.text.isNullOrEmpty()) {
                val fileName = Calendar.getInstance().timeInMillis.toString()
                uploadImage(fileName,binding.servicesTitleTv.text.toString())
            } else {
                Toast.makeText(
                    requireContext(),
                    "Select an Image and Title to proceed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK) {
            data?.data?.let {
                picUri = it
                binding.PngImgView.setImageURI(picUri)
            }
        }
    }

    private fun uploadImage(fileName: String,squareTitle:String) = CoroutineScope(Dispatchers.IO).launch {

        try{
            picUri?.let {
                val imgUrl  =  storageRef.child(typeSelected!!).child(fileName).putFile(it).await().storage.downloadUrl.await().toString()
                val sqModel = DashboardServiceModel()
                sqModel.imgDownloadUrl = imgUrl
                sqModel.imgStoredFileName = fileName
                sqModel.title = squareTitle
                dataBase.reference.child(typeSelected!!).push().setValue(sqModel).await()
                withContext(Dispatchers.Main){
                    Toast.makeText(requireActivity(),"Successfully created square.",Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(requireActivity(),e.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

}
