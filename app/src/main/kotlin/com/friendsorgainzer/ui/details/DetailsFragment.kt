package com.friendsorgainzer.ui.details

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.friendsorgainzer.App
import com.friendsorgainzer.databinding.FragmentDetailsBinding
import com.friendsorgainzer.room.MainRepository
import com.friendsorgainzer.room.PersonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModels {
        DetailsViewModelFactory((requireActivity().application as App).repository)
    }


    private lateinit var activityResult: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResult = getActivityResult()
    }

    private var girlId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val args: DetailsFragmentArgs by navArgs()

        girlId = args.girlId

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getGirlDetails(girlId).collect { girl ->
                binding.nameDetails.text = girl?.name
                girl?.photoLocalUri?.let {
                    binding.imageDetails.setImageURI(Uri.parse(it))
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageDetails.setOnClickListener {
            Toast.makeText(binding.root.context, "Photo will update after RESTART!", Toast.LENGTH_LONG).show()
            activityResult.launch("image/*")
        }
    }

    private fun getActivityResult() =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val targetFile = File(requireContext().filesDir, "super_person_$girlId.jpg")
                copyImageToInternalStorage(uri, targetFile)

                binding.imageDetails.setImageURI(Uri.fromFile(targetFile))

                val args: DetailsFragmentArgs by navArgs()
                val girlId = args.girlId
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.updateGirlPhotoLocalUri(girlId, targetFile.absolutePath)
                }
            }
        }


    private fun copyImageToInternalStorage(sourceUri: Uri, targetFile: File) {
        val inputStream = requireContext().contentResolver.openInputStream(sourceUri)
        val outputStream = FileOutputStream(targetFile)

        inputStream?.use { input ->
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024)
                var read = input.read(buffer)
                while (read != -1) {
                    output.write(buffer, 0, read)
                    read = input.read(buffer)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class DetailsViewModel(private val repository: MainRepository) : ViewModel() {

    fun getGirlDetails(id: Int): Flow<PersonEntity?> {
        return repository.getPersonById(id)
    }

    fun updateGirlPhotoLocalUri(id: Int, photoLocalUri: String) {
        viewModelScope.launch {
            repository.updatePersonPhotoLocalUri(id, photoLocalUri)
        }
    }
}
