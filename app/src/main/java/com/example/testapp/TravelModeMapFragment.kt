package com.example.testapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.screen_map.MapViewModel
import com.example.testapp.databinding.FragmentTravelModeMapBinding
import com.example.torang_core.data.model.Filter
import com.example.torang_core.util.Logger
import com.example.torang_core.viewmodels.FilterViewModel
import com.example.torang_core.viewmodels.MapSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TravelModeMapFragment : Fragment() {

    /** 뷰모델 */
    private val mViewModel: MapViewModel by activityViewModels()

    private val selectNationViewModel: SelectNationViewModel by activityViewModels()

    private val filterViewModel: FilterViewModel by activityViewModels()

    private val nationFragment = SelectNationFragment()

    /** 공유 뷰모델 */
    private val mapSharedViewModel: MapSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTravelModeMapBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.constraintLayout.setOnClickListener {
            nationFragment.show(childFragmentManager, "dialog")
        }

        selectNationViewModel.selected.observe(viewLifecycleOwner) {
            if (nationFragment.isVisible)
                nationFragment.dismiss()


            it.nationLocation?.let { it ->
                mViewModel.setLocation(it.lat, it.lon, 10f)
                mapSharedViewModel.searchFilterRestaurant(
                    latitudeNorthEast = filterViewModel.latitudeNorthWest,
                    latitudeSouthWest = filterViewModel.latitudeSouthEast,
                    longitudeNorthEast = filterViewModel.longitudeNorthWest,
                    longitudeSouthWest = filterViewModel.longitudeSouthEast,
                    searchType = Filter.SearchType.BOUND
                )
            }
        }

        selectNationViewModel.restaurants.observe(viewLifecycleOwner) {

        }

        binding.viewModel = selectNationViewModel
        return binding.root
    }
}