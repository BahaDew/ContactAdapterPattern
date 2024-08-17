package com.example.contactadapterpattern.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isGone
import androidx.core.view.size
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.contactadapterpattern.R
import com.example.contactadapterpattern.data.model.ContactUIData
import com.example.contactadapterpattern.databinding.ItemContactBinding
import com.example.contactadapterpattern.utils.myApply
import com.example.contactadapterpattern.utils.myLogger

class ContactListAdapter : ListAdapter<ContactUIData, ContactListAdapter.MyHolder>(MyDiffUtil) {

    private var onClickEdit: ((ContactUIData) -> Unit)? = null
    private var onClickDelete: ((ContactUIData) -> Unit)? = null
    private var onClickCancel: ((ContactUIData) -> Unit)? = null
    private lateinit var popupMenu: PopupMenu
    private var itemCount = 0

    inner class MyHolder(private val binding: ItemContactBinding) : ViewHolder(binding.root) {
        init {
            binding.btnOption.setOnClickListener {
                popupMenu = PopupMenu(binding.root.context, binding.btnOption)
                popupMenu.menuInflater.inflate(R.menu.my_popup_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener {
                    myLogger("myMenu ${it.itemId} ${R.id.edit == it.itemId} size ${popupMenu.menu.size}")
                    when (it.itemId) {
                        R.id.edit -> onClickEdit?.invoke(getItem(adapterPosition))
                        R.id.delete -> onClickDelete?.invoke(getItem(adapterPosition))
                        else -> onClickCancel?.invoke(getItem(adapterPosition))
                    }
                    return@setOnMenuItemClickListener true
                }
                popupMenu.show()
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind() {
            val item = getItem(adapterPosition)
            binding.myApply {
                fullName.text = item.firstName + " " + item.lastName
                phone.text = getPhoneFormat(item.phone)
                binding.voidNess.isGone = adapterPosition != itemCount - 1
            }
        }
    }

    object MyDiffUtil : DiffUtil.ItemCallback<ContactUIData>() {
        override fun areItemsTheSame(oldItem: ContactUIData, newItem: ContactUIData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ContactUIData, newItem: ContactUIData): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind()
    }

    fun setOnClickEdit(onClickEdit: (ContactUIData) -> Unit) {
        this.onClickEdit = onClickEdit
    }

    fun onClickDelete(onClickDelete: (ContactUIData) -> Unit) {
        this.onClickDelete = onClickDelete
    }

    fun onClickCancel(onClickCancel: (ContactUIData) -> Unit) {
        this.onClickCancel = onClickCancel
    }

    private fun getPhoneFormat(phone: String): String {
        // +998 91 157 09 64
        return "${phone.substring(0, 4)} " +
                "${phone.substring(4, 6)} " +
                "${phone.substring(6, 9)} " +
                "${phone.substring(9, 11)} " +
                phone.substring(11)
    }

    fun submitList2(list: List<ContactUIData>) {
        submitList(list)
        if (list.size > 1) {
            notifyItemChanged(list.size - 2)
        }
        itemCount = list.size
    }
}