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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.lahsuak.apps.wallpaperapp.R
import com.lahsuak.apps.wallpaperapp.databinding.FragmentWallpaperBinding

class WallpaperFragment : Fragment() {
    private lateinit var binding: FragmentWallpaperBinding
    private val args: WallpaperFragmentArgs by navArgs()
    private lateinit var image: Bitmap


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        binding = FragmentWallpaperBinding.inflate(inflater, container, false)

        binding.composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // In Compose world
                MaterialTheme {
                    SetWallpaper()
                }
            }
        }
        return binding.root
    }

    @Preview
    @Composable
    fun GreetingPreview() {
        SetWallpaper()
    }

    @Composable
    fun SetWallpaper(){
        Box(Modifier.fillMaxSize()) {
            Text(
                "Set as wallpaper",
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(0.4f))
                    .padding(8.dp)
                    .align(Alignment.BottomCenter)
                    .clickable {
                        setWallpaper()
                    }
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    transition: Transition<in Bitmap>?,
                ) {
                    binding.imageView.setImageBitmap(resource)
                    image = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
//        binding.downloadBtn.setOnClickListener {
//            setWallpaper()
//        }
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