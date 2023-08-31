package com.lahsuak.apps.wallpaperapp.ui

import android.os.Bundle
import android.util.LayoutDirection
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat.OrientationMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lahsuak.apps.wallpaperapp.R
import com.lahsuak.apps.wallpaperapp.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            addView(
                ComposeView(requireContext()).apply {
                    setViewCompositionStrategy(
                        ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                    )
                    id = R.id.compose_view_1
                    setContent {
                        Text("Hello Compose 1")
                    }
                }
            )
            addView(TextView(requireContext()).apply {
                text =  "Hello TextView"
                textSize =  20f
            })
            addView(
                ComposeView(requireContext()).apply {
                    setViewCompositionStrategy(
                        ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                    )
                    id = R.id.compose_view_2
                    setContent {
                        Text("Hello Compose 2")
                    }
                }
            )
        }
    }
}