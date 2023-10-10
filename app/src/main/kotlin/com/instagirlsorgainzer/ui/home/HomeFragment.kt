package com.instagirlsorgainzer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.instagirlsorgainzer.App
import com.instagirlsorgainzer.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val girlAdapter = GirlAdapter { girl ->
        // handle girl click, e.g., navigate to detail screen or open URL
    }
    private val viewModel: GirlViewModel by viewModels {
        GirlViewModelFactory((requireActivity().application as App).repository)
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.girlsRecyclerView.adapter = girlAdapter

        // Запускаем корутину в области жизни фрагмента
        viewLifecycleOwner.lifecycleScope.launch {
            // Собираем данные из Flow
            viewModel.allGirls.collect { girls ->
                girlAdapter.submitList(girls)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}