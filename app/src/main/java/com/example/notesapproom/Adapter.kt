package com.example.notesapproom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapproom.Database.Notes
import com.example.notesapproom.databinding.ItemRowBinding

class Adapter (private val activity: MainActivity,private var items : ArrayList<Notes>): RecyclerView.Adapter<Adapter.ItemViewHolder>() {
  class ItemViewHolder(val binding: ItemRowBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
      return ItemViewHolder(
          ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = items[position]
            holder.binding.apply {
                tv.text = item.note
                ibUpdate.setOnClickListener {
                    activity.raiseDialog(item.id,item.note)
                }
                ibDelete.setOnClickListener {
                    activity.deleteDialog(item)
                }
            }
    }
    override fun getItemCount() = items.size
}