package com.example.own.Diary

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import com.example.own.MainActivity
import com.example.own.R
import com.example.own.databinding.FragmentDiaryWriteBinding
import java.io.File
import java.io.FileOutputStream
import java.util.*

class DiaryWriteFragment: Fragment() {
    lateinit var binding: FragmentDiaryWriteBinding
    lateinit var newdiarydataimage : String
    var newdiarydatadate = "yyyy-mm-dd"
    lateinit var newdiarydatacontent: String
    var table_name = "Diary"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //dataget
        setFragmentResultListener("DiaryWrite") { key, bundle ->
            newdiarydatadate = bundle.getString("date").toString()
        }

        binding = FragmentDiaryWriteBinding.inflate(inflater, container, false)


        //image selected
        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val imageURI = it.data?.data!!
                    //image show on write fragment
                    binding.imageinput.setImageURI(imageURI)

                    //image copy outputstream
                    val storage = context?.filesDir
                    //image saving filename format
                    //variable filenum will be changed to the date
                    val calendar = Calendar.getInstance()
                    val filename = newdiarydatadate + ".jpg"
                    val tempFile = File(storage, filename)
                    Log.e("image", context?.filesDir.toString() + "/" + filename + ".jpg")
                    tempFile.createNewFile()
                    val out = FileOutputStream(tempFile)

                    //open image with uri for saving
                    val stream = requireActivity().contentResolver.openInputStream(imageURI);
                    var bitmap = BitmapFactory.decodeStream(stream)


                    //image resize//////
                    var cropsize = bitmap.width
                    var x = 0
                    var y = 0
                    if(bitmap.width > bitmap.height) {
                        cropsize = bitmap.height
                        y = 0
                        x = (bitmap.width-bitmap.height)/2
                    }
                    else {
                        cropsize = bitmap.width
                        y = (bitmap.height-bitmap.width)/2
                        x = 0
                    }
                    bitmap = Bitmap.createBitmap(bitmap, x, y, cropsize, cropsize)

                    //image press to output
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    out.close()
                }
            }

        //image select ↑↑↑↑↑↑↑↑↑↑
        binding.imageinput.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startForResult.launch(intent)
        }

        //write button click listener
        binding.button.setOnClickListener {


            newdiarydatacontent = binding.editContent.text.toString()
            //datapath will be changed as package name changes (com.example.myapplication)
            newdiarydataimage =
                "data/data/com.example.own/files/" + "$newdiarydatadate" + ".jpg"
            //경로에 따라 수정 필요


            val newdiarydata = DiaryData(newdiarydatadate, newdiarydatacontent, newdiarydataimage)
            parentFragmentManager.popBackStack()
            var result = (activity as MainActivity).dbhelper.insertDiaryData(newdiarydata)
        }


        return binding.root
    }
}


