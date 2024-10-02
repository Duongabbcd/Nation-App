package com.example.nationapp.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.nationapp.databinding.FragmentNationInfoBinding
import com.example.nationapp.local.entity.LocalNation
import com.example.nationapp.remote.Constant.typeWriter

class NationInfoDialog : DialogFragment() {
    private var commonName: String? = null
    private var officialName: String? = null
    private var svg: String? = null
    private var flagMeaning: String? = null
    private lateinit var binding: FragmentNationInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            commonName = it.getString(NATION_COMMON_NAME)
            officialName = it.getString(NATION_OFFICIAL_NAME)
            svg = it.getString(NATION_PNG_IMAGE)
            flagMeaning = it.getString(NATION_FLAG_MEANING)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNationInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commonName?.let { binding.commonName.typeWriter(lifecycleOwner = this, it, 10L) }
        officialName?.let { binding.officialName.typeWriter(lifecycleOwner = this, it, 10L)}
        (if (flagMeaning.isNullOrEmpty()) "We haven't got info about this flag's meaning!" else flagMeaning)?.let {
            binding.flagMeaning.typeWriter(
                lifecycleOwner = this,
                it, 30L
            )
        }

        svg?.let {
            Glide.with(this)
                .load(it)
                .into(binding.flagSvg)
        }
    }


    companion object {
        private const val NATION_COMMON_NAME = "commonName"
        private const val NATION_OFFICIAL_NAME = "officialName"
        private const val NATION_PNG_IMAGE = "svg"
        private const val NATION_FLAG_MEANING = "flagMeaning"

        fun newInstance(localNation: LocalNation): NationInfoDialog {
            val dialog = NationInfoDialog()
            val args = Bundle().apply {
                putString(NATION_COMMON_NAME, localNation.common)
                putString(NATION_OFFICIAL_NAME, localNation.official)
                putString(NATION_PNG_IMAGE, localNation.png)
                putString(NATION_FLAG_MEANING, localNation.alt)
            }
            dialog.arguments = args
            return dialog
        }
    }
}