package com.example.own.Diary

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.setFragmentResultListener
import com.example.own.R
import com.example.own.databinding.FragmentDiaryReadBinding
import java.io.File

class DiaryReadFragment : Fragment() {
    var diarydate = "temporary"
    var diarycontent = "temporary content"
    //datapath will be changed as package name changes (com.example.myapplication)
    var imagepath = "data/data/com.example.own/files/2022-6-6.jpg"
    lateinit var binding: FragmentDiaryReadBinding

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
        val uri = FileProvider.getUriForFile(requireActivity(), "com.example.own", File(imagepath))
        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);
        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    }

    private fun getimagediary() {
        binding.DiaryDate.text=diarydate
        binding.DiaryContent.text = diarycontent

        if (File(imagepath).exists()) {
            var bitmap = BitmapFactory.decodeFile(imagepath)
            binding.DiaryReadImage.setImageBitmap(bitmap)
        }
        else{
            binding.DiaryReadImage.setImageResource(R.drawable.ic_baseline_report_24)
        }
    }


    companion object {

    }
}