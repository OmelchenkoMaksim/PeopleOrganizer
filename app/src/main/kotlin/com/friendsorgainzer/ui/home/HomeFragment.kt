package com.friendsorgainzer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.friendsorgainzer.enums.CrushLevel
import com.friendsorgainzer.enums.InteractionLevel
import com.friendsorgainzer.enums.ZodiacSign
import com.friendsorgainzer.room.PersonEntity
import com.friendsorgainzer.ui.home.adapter.HomeAdapter
import com.friendsorgainzer.ui.home.adapter.HomeAdapterBridge
import com.friendsorgainzer.ui.home.viewmodel.HomeViewModel
import com.friendsorgainzer.ui.home.viewmodel.HomeViewModel.SortBy
import com.friendsorgainzer.ui.home.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), HomeAdapterBridge {

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

        binding.friendsRecyclerView.adapter = homeAdapter
        binding.friendsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.fab.setOnClickListener { viewFAB ->
            showFabMenu(viewFAB)
        }

        configureSort()

        lifecycleScope.launch {
            viewModel.allFriends.collect { friends ->
                homeAdapter.submitList(friends)
            }
        }
    }

    private fun configureSort() {
        // Настройка Spinner
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sort_options, // опции сортировки в strings.xml
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sortSpinner.adapter = adapter
        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val selected = SortBy.values()[pos]
                viewModel.sortList(selected)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
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
                R.id.action_insert_default -> viewModel.insertDefaultList()
                R.id.action_add -> navigateToAddPersonFragment()
                R.id.action_clear_room -> showClearRoomConfirmationDialog()
                else -> Unit
            }
            true
        }
        popup.show()
    }

    private fun navigateToAddPersonFragment() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Добавить карточку")

        val layout = LayoutInflater.from(requireContext()).inflate(R.layout.add_person_dialog, null)
        builder.setView(layout)

        val nameEditText = layout.findViewById<EditText>(R.id.nameEditText)
        val ageEditText = layout.findViewById<EditText>(R.id.ageEditText)
        ageEditText.setText("0")

        builder.setPositiveButton("Добавить") { _, _ ->

            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString()

            if (name.isNotEmpty() && age.isNotEmpty()) {
                viewModel.addPerson(name, age = age.toInt())
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

    override fun onZodiacSelected(id: Int, selectedSign: ZodiacSign) {
        viewModel.updateZodiacSign(id, selectedSign)
    }

    override fun onInteractionSelected(id: Int, selectedLevel: InteractionLevel) {
        viewModel.updateInteractionSelected(id, selectedLevel)
    }

    override fun onCrushSelected(id: Int, selectedLevel: CrushLevel) {
        viewModel.updateCrushSelected(id, selectedLevel)
    }

    override fun onLastClickedUpdated(id: Int, currentTime: Long) {
        viewModel.updateLastClicked(id, currentTime)
    }

    override fun onAgeEntered(id: Int, age: Int) {
        viewModel.updateAge(id, age)
    }

    override fun onCommentEntered(id: Int, comment: String) {
        viewModel.updateComment(id, comment)
    }

    override fun onNameEntered(id: Int, personName: String) {
        viewModel.updateName(id, personName)
    }

    override fun onBirthdayEntered(id: Int, date: String) {
        viewModel.updateBirthday(id, date)
    }

    override fun onUrlEntered(id: Int, url: String) {
        viewModel.updatePersonPhotoUrl(id, url)
    }

    override fun onHasPartnerToggled(id: Int, inRelations: Boolean) {
        viewModel.updateInRelations(id, inRelations)
    }

    override fun onFavoriteToggled(id: Int, checked: Boolean) {
        viewModel.updateFavorite(id, checked)
    }

    override fun onPersonDeleted(person: PersonEntity) {
        viewModel.deletePerson(person)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
