package com.friendsorgainzer.ui.home.adapter

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.webkit.URLUtil
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.friendsorgainzer.databinding.PersonItemBinding
import com.friendsorgainzer.enums.CrushLevel
import com.friendsorgainzer.enums.InteractionLevel
import com.friendsorgainzer.enums.ZodiacSign
import com.friendsorgainzer.room.PersonEntity
import java.util.Calendar
import java.util.concurrent.TimeUnit

class PersonViewHolder(
    val binding: PersonItemBinding,
    private val fragmentBridge: HomeAdapterBridge
) : RecyclerView.ViewHolder(binding.root) {
    //      for last connection timer
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    //     textwatchers
    private var currentPerson: PersonEntity? = null

    private val nameTextWatcher = getSimpleTextWatcher { personName ->
        currentPerson?.let {
            if (personName.isNotEmpty()) fragmentBridge.onNameEntered(it.id, personName)
        }
    }

    private val urlTextWatcher = getSimpleTextWatcher { personUrl ->
        currentPerson?.let {
            if (personUrl.isNotEmpty()) fragmentBridge.onUrlEntered(it.id, personUrl)
        }
    }

    private val commentsTextWatcher = getSimpleTextWatcher { personComment ->
        currentPerson?.let {
            if (personComment.isNotEmpty()) fragmentBridge.onCommentEntered(it.id, personComment)
        }
    }

    private val ageTextWatcher = getSimpleTextWatcher { age ->
        currentPerson?.let {
            if (age.isNotEmpty()) fragmentBridge.onAgeEntered(it.id, age.toInt())
        }
    }

    private val NOT_VISITED_TIME = 300000L * 60 * 60 * 1000  // 300000 часов в миллисекундах

    fun bind(personEntity: PersonEntity) {
        currentPerson = personEntity

        with(binding) {
            nameEditText.removeTextChangedListener(nameTextWatcher)
            urlEditText.removeTextChangedListener(urlTextWatcher)
            commentsEditText.removeTextChangedListener(commentsTextWatcher)
            ageEditText.removeTextChangedListener(ageTextWatcher)

            fillFields(personEntity)

            nameEditText.addTextChangedListener(nameTextWatcher)
            urlEditText.addTextChangedListener(urlTextWatcher)
            commentsEditText.addTextChangedListener(commentsTextWatcher)
            ageEditText.addTextChangedListener(ageTextWatcher)
        }

        setupOpenUrlButton(personEntity)
        setupHasPartnerCheckBox(personEntity)
        setupUpdateWrittenTo(personEntity)
        setupUpdateReceivedReply(personEntity)
        setupFavoriteCheckBox(personEntity)
        setupPhotosClick(personEntity)
        setupDeleteButton(personEntity)
        setupImage(personEntity)
        setupCrushSpinner(personEntity)
        setupZodiacSpinner(personEntity)
        setupInteractionSpinner(personEntity)
        setupBirthdayEditTextWithPicker(personEntity)
//      for last connection timer
        updateLastConnectionTime(personEntity)
    }

    fun unbind() {
        runnable?.let { handler.removeCallbacks(it) }
    }

    private fun fillFields(person: PersonEntity) {
        binding.apply {
            idPresenter.text = person.id.toString()
            nameEditText.setText(person.name)
            birthdayEditText.setText(person.birthday)
            urlEditText.setText(person.url)
            lastClickedTextView.text = person.lastClicked.toString()
            commentsEditText.setText(person.comments)
            ageEditText.setText(person.age.toString())
            hasRelationsCheckBox.isChecked = person.inRelations == true
            favoriteCheckBox.isChecked = person.isFavorite == true

            val isProfileVisited = System.currentTimeMillis() - person.lastClicked < NOT_VISITED_TIME
            binding.checkedProfile.isChecked = isProfileVisited

            openInfoButton.setOnClickListener {
                if (detailsContainer.visibility == View.GONE) {
                    detailsContainer.visibility = View.VISIBLE
                    openInfoButton.text = "Hide Info" // Можно изменить текст кнопки на "Hide Info" при раскрытии
                } else {
                    detailsContainer.visibility = View.GONE
                    openInfoButton.text = "Info" // Меняем текст обратно на "Info", когда информация скрыта
                }
            }
        }
    }

    private fun updateLastConnectionTime(personEntity: PersonEntity) {
        runnable = object : Runnable {
            override fun run() {
                val currentTime = System.currentTimeMillis()
                val diff = currentTime - personEntity.lastClicked
                val diffInHours = TimeUnit.MILLISECONDS.toHours(diff)
                val date = "Last clicked: $diffInHours hours ago"
                binding.lastClickedTextView.text = date

                handler.postDelayed(this, 3600000) // Обновляем каждый час
            }
        }
        handler.post(runnable!!)
    }

    private fun setupImage(person: PersonEntity) {
        person.photoLocalUri?.let {
            val uri = Uri.parse(it)
            binding.picture.setImageURI(uri)
        }
    }

    private fun setupDeleteButton(person: PersonEntity) {
        binding.deleteButton.setOnClickListener {
            val dialog = AlertDialog.Builder(binding.root.context)
                .setTitle("Подтвердить удаление")
                .setMessage("Ты действительно хочешь удалить этот элемент?")
                .setPositiveButton("Удалить") { _, _ ->
                    fragmentBridge.onPersonDeleted(person)
                }
                .setNegativeButton("Отмена", null)
                .create()
            dialog.show()
        }
    }


    private fun setupPhotosClick(person: PersonEntity) {
        binding.picture.setOnClickListener {
            fragmentBridge.onPhotoClick(person.id)
        }
    }

    private fun setupHasPartnerCheckBox(person: PersonEntity) {
        binding.hasRelationsCheckBox.setOnCheckedChangeListener { _, isChecked ->
            fragmentBridge.onHasPartnerToggled(person.id, isChecked)
        }
    }

    private fun setupUpdateWrittenTo(person: PersonEntity) {
        binding.wroteCheckBox.setOnCheckedChangeListener { _, isChecked ->
            fragmentBridge.updateWrittenTo(person.id, isChecked)
        }
    }

    private fun setupUpdateReceivedReply(person: PersonEntity) {
        binding.gotReplyCheckBox.setOnCheckedChangeListener { _, isChecked ->
            fragmentBridge.updateReceivedReply(person.id, isChecked)
        }
    }

    private fun setupFavoriteCheckBox(person: PersonEntity) {
        binding.favoriteCheckBox.setOnCheckedChangeListener { _, isChecked ->
            fragmentBridge.onFavoriteToggled(person.id, isChecked)
        }
    }

    private fun setupOpenUrlButton(person: PersonEntity) {
        binding.openUrlButton.setOnClickListener {
            val url = binding.urlEditText.text.toString()

            // Показываем диалог с подтверждением перед открытием URL
            val dialog = AlertDialog.Builder(binding.root.context)
                .setTitle("Открыть ссылку")
                .setMessage("Ты действительно хочешь открыть эту ссылку?")
                .setPositiveButton("Открыть") { _, _ ->
                    if (URLUtil.isValidUrl(url)) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        binding.root.context.startActivity(intent)
                        val currentTime = System.currentTimeMillis()
                        person.lastClicked = currentTime
                        fragmentBridge.onLastClickedUpdated(person.id, currentTime)
                    } else {
                        Toast.makeText(binding.root.context, "URL не действителен!", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Отмена", null)
                .create()
            dialog.show()
        }
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

    private fun setupCrushSpinner(person: PersonEntity) {
        val crushAdapter = ArrayAdapter(
            binding.root.context,
            android.R.layout.simple_spinner_item,
            CrushLevel.values().map { it.level }
        )
        setupSpinner(
            binding.crushSpinner,
            crushAdapter,
            person.crushLevel.ordinal
        ) { position ->
            val selectedLevel = CrushLevel.values()[position]
            fragmentBridge.onCrushSelected(person.id, selectedLevel)
        }
    }

    private fun setupBirthdayEditTextWithPicker(person: PersonEntity) {
        binding.birthdayEditText.setOnClickListener {
            showDatePickerDialog { date ->
                binding.birthdayEditText.setText(date)
                fragmentBridge.onBirthdayEntered(person.id, date)
            }
        }
    }

    private fun showDatePickerDialog(onDateSet: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            binding.root.context,
            { _, pickedYear, pickedMonth, pickedDay ->
                val date = "$pickedDay/${pickedMonth + 1}/$pickedYear"
                onDateSet(date)
            },
            year, month, day
        ).show()
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
