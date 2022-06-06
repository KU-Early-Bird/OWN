package com.example.myapplication

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.myapplication.DiaryData
import com.example.myapplication.databinding.FragmentDiaryWriteBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URI
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DiaryWriteFragment: Fragment(){
    lateinit var binding:FragmentDiaryWriteBinding
    lateinit var newdiarydataimage:String
    lateinit var newdiarydatadate:String
    lateinit var newdiarydatacontent:String
    var table_name = "Diary"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater:LayoutInflater,container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentDiaryWriteBinding.inflate(inflater, container, false)


        //image selected
        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                var imageURI = it.data?.data!!
                //image show on write fragment
                binding.imageinput.setImageURI(imageURI)

                //image copy outputstream
                var storage = context?.filesDir
                //image saving filename format
                //variable filenum will be changed to the date
                val calendar = Calendar.getInstance()
                val year =calendar.get(Calendar.YEAR).toString()
                val month = (calendar.get(Calendar.MONTH)+1).toString() //달은 0부터 시작함
                val day = calendar.get(Calendar.DATE).toString()
                newdiarydatadate = "$year-$month-$day"
                var filename = newdiarydatadate+".jpg"
                var tempFile = File(storage, filename)
                Log.e("image", context?.filesDir.toString()+"/"+filename+".jpg")
                tempFile.createNewFile()
                var out = FileOutputStream(tempFile)

                //open image with uri for saving
                val stream = requireActivity().contentResolver.openInputStream(imageURI);
                var bitmap = BitmapFactory.decodeStream(stream)
                //image resize
                bitmap = Bitmap.createScaledBitmap(bitmap, 640, 640, true)
                //image press to output
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.close()
            }
        }

        //image select ↑↑↑↑↑↑↑↑↑↑
        binding.imageinput.setOnClickListener{
            var intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startForResult.launch(intent)
        }

        //write button click listener
        binding.button.setOnClickListener {


            newdiarydatacontent= binding.editContent.text.toString()
            //datapath will be changed as package name changes (com.example.myapplication)
            newdiarydataimage = "data/data/com.example.myapplication/files/" +"$newdiarydatadate"+".jpg"
            //경로에 따라 수정 필요


            //binding.editContent.setText(newdiarydataimage)

            var newdiarydata = DiaryData(newdiarydatadate, newdiarydatacontent, newdiarydataimage)
            var result = (activity as MainActivity).dbhelper.insertDiaryData(newdiarydata)
        }


        return binding.root
    }


}