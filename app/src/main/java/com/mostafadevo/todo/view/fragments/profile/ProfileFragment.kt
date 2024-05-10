package com.mostafadevo.todo.view.fragments.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.mostafadevo.todo.R
import com.mostafadevo.todo.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private val REQUEST_CODE = 1000
    private lateinit var binding: FragmentProfileBinding
    private val mProfileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProfileViewModel.imageUrl.observe(viewLifecycleOwner, Observer { imageurl ->
            Log.d("ProfileFragment", "ImageUrl: $imageurl")
            displayImage(binding.imageView2, imageurl)
        })
        binding.imageView2.setOnClickListener {
            openGallery()
        }
        setNameAndEmailToViews()

    }

    private fun setNameAndEmailToViews() {
        binding.emailTv.text = mProfileViewModel.getEmail()
        binding.nameTv.text = mProfileViewModel.getName()
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                mProfileViewModel.uploadImage(uri)
            }
            Log.d("ProfileFragment", "onActivityResult: $data")
        }
    }

    private fun displayImage(imageView: ImageView, imageUrl: String) {
        Glide.with(this)
            .load(imageUrl).placeholder(R.drawable.baseline_download_24)
            .into(imageView)
    }

}