package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.setFragmentResultListener
import com.example.myapplication.databinding.FragmentDiaryReadBinding
import java.io.File
import java.util.*


class DiaryReadFragment : Fragment() {
    var diarydate = "temporary"
    var diarycontent = "temporary content"
    //datapath will be changed as package name changes (com.example.myapplication)
    var imagepath = "data/data/com.example.myapplication/files/2022-6-6.jpg"
    lateinit var binding:FragmentDiaryReadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //dataget
        setFragmentResultListener("DiaryRead") { key, bundle ->
            diarydate = bundle.getString("date").toString()
            diarycontent = bundle.getString("content").toString()
            imagepath = bundle.getString("image").toString()
            getimagediary()

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDiaryReadBinding.inflate(inflater, container, false)
        binding.shareButton.setOnClickListener{//인스타그램 공유하는거
            val type = "image/*";
            createInstagramIntent(type);
        }

        return binding.root
    }
    fun createInstagramIntent(type:String){

        // Create the new Intent using the 'Send' action.
        val share: Intent = Intent(Intent.ACTION_SEND);
        // Set the MIME type
        share.setType(type);
        // Create the URI from the media
        // Path will be changed as pakcage name chanes
        val uri = FileProvider.getUriForFile(requireActivity(), "com.example.myapplication", File(imagepath))
        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);
        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    }

    private fun getimagediary() {
        binding.DiaryDate.text=diarydate
        binding.DiaryContent.text = diarycontent
        var bitmap = BitmapFactory.decodeFile(imagepath)
        binding.DiaryReadImage.setImageBitmap(bitmap)
    }


    companion object {

    }
}