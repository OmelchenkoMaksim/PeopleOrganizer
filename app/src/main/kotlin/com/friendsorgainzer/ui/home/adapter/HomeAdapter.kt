package com.friendsorgainzer.ui.home.adapter

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.friendsorgainzer.databinding.PersonItemBinding
import com.friendsorgainzer.enums.CrushLevel
import com.friendsorgainzer.enums.InteractionLevel
import com.friendsorgainzer.enums.ZodiacSign
import com.friendsorgainzer.room.PersonEntity
import com.friendsorgainzer.ui.home.adapter.PersonDiffCallback.Keys.KEY_AGE
import com.friendsorgainzer.ui.home.adapter.PersonDiffCallback.Keys.KEY_BIRTHDAY
import com.friendsorgainzer.ui.home.adapter.PersonDiffCallback.Keys.KEY_COMMENTS
import com.friendsorgainzer.ui.home.adapter.PersonDiffCallback.Keys.KEY_CRUSH_LEVEL
import com.friendsorgainzer.ui.home.adapter.PersonDiffCallback.Keys.KEY_INTERACTION
import com.friendsorgainzer.ui.home.adapter.PersonDiffCallback.Keys.KEY_IN_RELATIONS
import com.friendsorgainzer.ui.home.adapter.PersonDiffCallback.Keys.KEY_IS_FAVORITE
import com.friendsorgainzer.ui.home.adapter.PersonDiffCallback.Keys.KEY_LAST_CLICKED
import com.friendsorgainzer.ui.home.adapter.PersonDiffCallback.Keys.KEY_NAME
import com.friendsorgainzer.ui.home.adapter.PersonDiffCallback.Keys.KEY_PHOTO_LOCAL_URI
import com.friendsorgainzer.ui.home.adapter.PersonDiffCallback.Keys.KEY_ZODIAC

class HomeAdapter(
    private val fragmentBridge: HomeAdapterBridge
) : ListAdapter<PersonEntity, PersonViewHolder>(PersonDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding = PersonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonViewHolder(binding, fragmentBridge)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.unbind()  // Останавливаем предыдущий Handler, если есть
        holder.bind(getItem(position))
        val positionForView = (position + 1).toString()
        holder.binding.position.text = positionForView
    }

    override fun onBindViewHolder(
        holder: PersonViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val bundle: Bundle = payloads[0] as Bundle
            for (key in bundle.keySet()) {
                when (key) {
                    KEY_NAME -> holder.binding.nameEditText.setText(bundle.getString(KEY_NAME))
                    KEY_AGE -> holder.binding.ageEditText.setText(bundle.getInt(KEY_AGE).toString())
                    KEY_COMMENTS -> holder.binding.commentsEditText.setText(
                        bundle.getString(
                            KEY_COMMENTS
                        )
                    )

                    KEY_BIRTHDAY -> holder.binding.birthdayEditText.setText(
                        bundle.getString(
                            KEY_BIRTHDAY
                        )
                    )

                    KEY_IN_RELATIONS -> holder.binding.hasRelationsCheckBox.isChecked =
                        bundle.getBoolean(KEY_IN_RELATIONS)

                    KEY_LAST_CLICKED -> holder.binding.lastClickedTextView.text =
                        bundle.getLong(KEY_LAST_CLICKED).toString()

                    KEY_IS_FAVORITE -> holder.binding.favoriteCheckBox.isChecked =
                        bundle.getBoolean(KEY_IS_FAVORITE)

                    KEY_ZODIAC -> {
                        val selectedSign =
                            bundle.getString(KEY_ZODIAC)?.let { ZodiacSign.valueOf(it) }
                        if (selectedSign != null) {
                            holder.binding.zodiacSpinner.setSelection(selectedSign.ordinal)
                        }
                    }

                    KEY_INTERACTION -> {
                        val selectedInteraction =
                            bundle.getString(KEY_INTERACTION)?.let { InteractionLevel.valueOf(it) }
                        if (selectedInteraction != null) {
                            holder.binding.interactionSpinner.setSelection(selectedInteraction.ordinal)
                        }
                    }

                    KEY_CRUSH_LEVEL -> {
                        val selectedLevel =
                            bundle.getString(KEY_CRUSH_LEVEL)?.let { CrushLevel.valueOf(it) }
                        if (selectedLevel != null) {
                            holder.binding.crushSpinner.setSelection(selectedLevel.ordinal)
                        }
                    }

                    KEY_PHOTO_LOCAL_URI -> {
                        val uriString = bundle.getString(KEY_PHOTO_LOCAL_URI)
                        if (uriString != null) {
                            val uri = Uri.parse(uriString)
                            holder.binding.picture.setImageURI(uri)
                        }
                    }
                }
            }
        }
    }
}
