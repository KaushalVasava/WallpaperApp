package com.lahsuak.apps.wallpaperapp.ui

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.lahsuak.apps.wallpaperapp.R
import com.lahsuak.apps.wallpaperapp.databinding.FragmentWallpaperBinding

class WallpaperFragment : Fragment(R.layout.fragment_wallpaper) {
    private lateinit var binding: FragmentWallpaperBinding
    private val args: WallpaperFragmentArgs by navArgs()
    private lateinit var image: Bitmap


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWallpaperBinding.bind(view)
        // Redirect system "Back" press to our dispatcher

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedDispatcher
        )

        Glide.with(requireContext()).asBitmap().load(args.imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    binding.imageView.setImageBitmap(resource)
                    image = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
        binding.downloadBtn.setOnClickListener {
            setWallpaper()
        }
    }

    private fun setWallpaper() {
        val wallpaperManager = WallpaperManager.getInstance(requireActivity().baseContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wallpaperManager.setBitmap(image, null, true, WallpaperManager.FLAG_SYSTEM)
            wallpaperManager.setWallpaperOffsetSteps(1F, 1F)
        } else {
            wallpaperManager.setBitmap(image)
        }
        Toast.makeText(requireContext(), "Wallpaper set!", Toast.LENGTH_SHORT).show()
    }

    private val backPressedDispatcher = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Redirect to our own function
            this@WallpaperFragment.onBackPressed()
        }
    }

    private fun onBackPressed() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar!!.show()
    }

}