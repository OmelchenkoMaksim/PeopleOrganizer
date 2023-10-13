package com.friendsorgainzer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.friendsorgainzer.App
import com.friendsorgainzer.R
import com.friendsorgainzer.databinding.FragmentHomeBinding
import com.friendsorgainzer.enums.CrashLevel
import com.friendsorgainzer.enums.InteractionLevel
import com.friendsorgainzer.enums.ZodiacSign
import com.friendsorgainzer.room.PersonEntity
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), HomeAdapterToFragmentBridge {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeAdapter = HomeAdapter(this)

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory((requireActivity().application as App).repository)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.girlsRecyclerView.adapter = homeAdapter
        binding.girlsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allGirls
                .collect { girls ->
                    homeAdapter.submitList(girls)
                }
        }

        binding.fab.setOnClickListener { viewFAB ->
            showFabMenu(viewFAB)
        }

    }

    override fun onPhotoClick(id: Int) {
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationDetails(id)
        findNavController().navigate(action)
    }

    private fun showFabMenu(view: View) {
        val popup = PopupMenu(requireContext(), view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.fab_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_insert_default -> viewModel.insertDefaultGirls()
                R.id.action_add -> navigateToAddGirlFragment()
                R.id.action_clear_room -> showClearRoomConfirmationDialog()
                else -> Unit
            }
            true
        }
        popup.show()
    }

    private fun navigateToAddGirlFragment() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Добавить девушку")

        val layout = LayoutInflater.from(requireContext()).inflate(R.layout.add_girl_dialog, null)
        builder.setView(layout)

        val nameEditText = layout.findViewById<EditText>(R.id.nameEditText)
        val ageEditText = layout.findViewById<EditText>(R.id.ageEditText)
        ageEditText.setText("0")

        builder.setPositiveButton("Добавить") { _, _ ->

            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString()

            if (name.isNotEmpty() && age.isNotEmpty()) {
                viewModel.addGirl(name, age = age.toInt())
            }
        }

        builder.setNegativeButton("Отмена", null)
        builder.show()
    }

    private fun showClearRoomConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Очистить базу данных")
            .setMessage("Вы уверены? Это удалит все данные.")
            .setPositiveButton("Да") { _, _ ->
                viewModel.clearRoom()
            }
            .setNegativeButton("Нет", null)
            .show()
    }

    override fun onZodiacSelected(girlId: Int, selectedSign: ZodiacSign) {
        viewModel.updateGirlZodiac(girlId, selectedSign)
    }

    override fun onInteractionSelected(id: Int, selectedLevel: InteractionLevel) {
        viewModel.updateInteractionSelected(id, selectedLevel)
    }

    override fun onCrashSelected(id: Int, selectedLevel: CrashLevel) {
        viewModel.updateCrashSelected(id, selectedLevel)
    }

    override fun onAgeEntered(girlId: Int, age: Int) {
        viewModel.updateGirlAge(girlId, age)
    }

    override fun onCommentEntered(girlId: Int, comment: String) {
        viewModel.updateGirlComment(girlId, comment)
    }

    override fun onUrlEntered(id: Int, url: String) {
        viewModel.updateGirlUrl(id, url)
    }

    override fun onHasBoyfriendToggled(girlId: Int, hasBoyfriend: Boolean) {
        viewModel.updateGirlHasBoyfriend(girlId, hasBoyfriend)
    }

    override fun onGirlDeleted(person: PersonEntity) {
        viewModel.deleteGirl(person)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
