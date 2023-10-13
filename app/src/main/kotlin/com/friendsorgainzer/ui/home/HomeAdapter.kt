package com.friendsorgainzer.ui.home

import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.friendsorgainzer.databinding.GirlItemBinding
import com.friendsorgainzer.enums.CrashLevel
import com.friendsorgainzer.enums.InteractionLevel
import com.friendsorgainzer.enums.ZodiacSign
import com.friendsorgainzer.room.PersonEntity

interface HomeAdapterToFragmentBridge {
    fun onAgeEntered(girlId: Int, age: Int)
    fun onCommentEntered(girlId: Int, comment: String)
    fun onUrlEntered(id: Int, url: String)
    fun onHasBoyfriendToggled(girlId: Int, hasBoyfriend: Boolean)
    fun onPhotoClick(id: Int)
    fun onGirlDeleted(person: PersonEntity)
    fun onZodiacSelected(girlId: Int, selectedSign: ZodiacSign)
    fun onInteractionSelected(id: Int, selectedLevel: InteractionLevel)
    fun onCrashSelected(id: Int, selectedLevel: CrashLevel)

}

class HomeAdapter(
    private val fragmentBridge: HomeAdapterToFragmentBridge
) : ListAdapter<PersonEntity, HomeAdapter.PersonViewHolder>(GirlDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding = GirlItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PersonViewHolder(private val binding: GirlItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(personEntity: PersonEntity) {
            fillFields(personEntity)
            setupAgeEditText(personEntity)
            setupCommentsEditText(personEntity)
            setupUrlEditText(personEntity)
            setupOpenUrlButton()
            setupHasBoyfriendCheckBox(personEntity)
            setupPhotosClick(personEntity)
            setupDeleteButton(personEntity)
            setupImage(personEntity)
            setupCrashSpinner(personEntity)
            setupZodiacSpinner(personEntity)
            setupInteractionSpinner(personEntity)
        }

        private fun fillFields(person: PersonEntity) {
            binding.apply {
                nameTextView.text = person.name
                urlEditText.setText(person.url)
                binding.commentsEditText.setText(person.comments)
                binding.ageEditText.setText(person.age.toString())
                binding.hasBoyfriendCheckBox.isChecked = person.inRelations == true
                binding.favoriteCheckBox.isChecked = person.isFavorite == true
            }
        }

        private fun setupImage(person: PersonEntity) {
            person.photoLocalUri?.let {
                val uri = Uri.parse(it)
                binding.picture.setImageURI(uri)
            }
        }

        private fun setupDeleteButton(person: PersonEntity) {
            binding.deleteButton.setOnClickListener {
                fragmentBridge.onGirlDeleted(person)
            }
        }

        private fun setupPhotosClick(person: PersonEntity) {
            binding.picture.setOnClickListener {
                fragmentBridge.onPhotoClick(person.id)
            }
        }

        private fun setupHasBoyfriendCheckBox(person: PersonEntity) {
            binding.hasBoyfriendCheckBox.setOnCheckedChangeListener { _, isChecked ->
                fragmentBridge.onHasBoyfriendToggled(person.id, isChecked)
            }
        }

        private fun setupOpenUrlButton() {
            binding.openUrlButton.setOnClickListener {
                val url = binding.urlEditText.text.toString()
                if (URLUtil.isValidUrl(url)) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    binding.root.context.startActivity(intent)
                } else {
                    Toast.makeText(binding.root.context, "URL not valid!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun setupUrlEditText(person: PersonEntity) {
            binding.ageEditText.addTextChangedListener(getSimpleTextWatcher { personUrl ->
                if (personUrl.isNotEmpty()) fragmentBridge.onUrlEntered(person.id, personUrl)
            })
        }


        private fun setupCommentsEditText(person: PersonEntity) {
            binding.ageEditText.addTextChangedListener(getSimpleTextWatcher { personComment ->
                if (personComment.isNotEmpty())
                    fragmentBridge.onCommentEntered(person.id, personComment)
            })
        }

        private fun setupAgeEditText(person: PersonEntity) {
            binding.ageEditText.addTextChangedListener(getSimpleTextWatcher { age ->
                if (age.isNotEmpty()) fragmentBridge.onAgeEntered(person.id, age.toInt())
            })
        }

        private fun getSimpleTextWatcher(afterTextChanged: (String) -> Unit) =
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    s?.toString()?.let(afterTextChanged)
                }
            }

        private fun setupZodiacSpinner(person: PersonEntity) {
            val zodiacAdapter = ArrayAdapter(
                binding.root.context,
                android.R.layout.simple_spinner_item,
                ZodiacSign.values().map { it.sign }
            )
            setupSpinner(
                binding.zodiacSpinner,
                zodiacAdapter,
                person.zodiac.ordinal
            ) { position ->
                val selectedSign = ZodiacSign.values()[position]
                fragmentBridge.onZodiacSelected(person.id, selectedSign)
            }
        }

        private fun setupInteractionSpinner(person: PersonEntity) {
            val interactionAdapter = ArrayAdapter(
                binding.root.context,
                android.R.layout.simple_spinner_item,
                InteractionLevel.values().map { it.level }
            )
            setupSpinner(
                binding.interactionSpinner,
                interactionAdapter,
                person.interaction.ordinal
            ) { position ->
                val selectedInteraction = InteractionLevel.values()[position]
                fragmentBridge.onInteractionSelected(person.id, selectedInteraction)
            }
        }

        private fun setupCrashSpinner(person: PersonEntity) {
            val crashAdapter = ArrayAdapter(
                binding.root.context,
                android.R.layout.simple_spinner_item,
                CrashLevel.values().map { it.level }
            )
            setupSpinner(
                binding.crashSpinner,
                crashAdapter,
                person.crashLevel.ordinal
            ) { position ->
                val selectedLevel = CrashLevel.values()[position]
                fragmentBridge.onCrashSelected(person.id, selectedLevel)
            }
        }


        /**
         * Хорош для ENUM + дроп-даун списков
         */
        private fun setupSpinner(
            spinner: Spinner,
            adapter: ArrayAdapter<String>,
            selectedItemPosition: Int,
            onItemSelected: (Int) -> Unit
        ) {
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.setSelection(selectedItemPosition)
            spinner.onItemSelectedListener = getSimpleItemSelectedListener(onItemSelected)
        }

        /**
         * Хорош для ENUM + дроп-даун списков
         */
        private fun getSimpleItemSelectedListener(onItemSelected: (Int) -> Unit) =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    onItemSelected(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

    }

    class GirlDiffCallback : DiffUtil.ItemCallback<PersonEntity>() {
        override fun areItemsTheSame(oldItem: PersonEntity, newItem: PersonEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PersonEntity, newItem: PersonEntity): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

