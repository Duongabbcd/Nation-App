package com.example.nationapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.nationapp.R
import com.example.nationapp.databinding.ItemCountryInformationBinding
import com.example.nationapp.local.entity.LocalNation
import com.example.nationapp.presentation.viewmodel.NationViewModel
import java.io.File

class NationAdapter(private val viewModel: NationViewModel) : RecyclerView.Adapter<NationAdapter.ContractNationViewHolder>() {
    private var nationList = emptyList<LocalNation>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContractNationViewHolder {

        val itemBinding = ItemCountryInformationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContractNationViewHolder(itemBinding)

    }

    override fun getItemCount(): Int {
        return nationList.size
    }

    override fun onBindViewHolder(holder: ContractNationViewHolder, position: Int) {
        val localNation = nationList[position]
        holder.bind(localNation)
        holder.itemView.setOnClickListener {
            viewModel.setCurrentFragment(localNation)
        }
    }

    fun submitList(newItems: List<LocalNation>) {
        val diffCallback = ItemDiffCallback(nationList, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        nationList = newItems
        diffResult.dispatchUpdatesTo(this)
    }


    class ContractNationViewHolder(private val itemBinding: ItemCountryInformationBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        // Specify the folder and file name for the main image
        fun bind(localNation: LocalNation) {
            Glide.with(itemBinding.root)
                .load(localNation.png).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .apply(
                    RequestOptions()
                    .placeholder(R.drawable.background)
                    .error(R.drawable.ic_launcher_background))
                .into(itemBinding.nationFlag)
        }

    }

    class ItemDiffCallback(
        private val oldList: List<LocalNation>,
        private val newList: List<LocalNation>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].nationalId == newList[newItemPosition].nationalId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}