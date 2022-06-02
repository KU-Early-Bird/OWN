package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.own.databinding.FragmentDiaryWriteBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URI

class DiaryWriteFragment: Fragment(){
    lateinit var binding:FragmentDiaryWriteBinding
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
                var filenum = 0
                var filename = "diaryimage$filenum.jpg"
                var tempFile = File(storage, filename)
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

        //image select
        binding.imageinput.setOnClickListener{
            var intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startForResult.launch(intent)
        }

        //write button click listener
        binding.button.setOnClickListener {

            val diarycontent= binding.editContent.text.toString()
            //데이터 db에 저장과 동시에 manager? 관리


        }


        return binding.root
    }


}