package com.example.saveapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class NoteAdaptor(val listener: OnClickListener) :
    ListAdapter<Note, NoteAdaptor.NoteViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.noteTableID == newItem.noteTableID
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: NoteViewHolder,
        position: Int
    ) {
        val currentItem = getItem(position)
        holder.textTitle.text = currentItem.title
        holder.textContent.text = currentItem.content
        holder.textPriority.text = currentItem.priority.toString()
    }


    fun getNoteAt(position: Int) = getItem(position)

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textTitle: TextView = itemView.findViewById(R.id.title)
        var textContent: TextView = itemView.findViewById(R.id.content)
        var textPriority: TextView = itemView.findViewById(R.id.priority)

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(bindingAdapterPosition))
                }
            }
        }
    }

    interface OnClickListener {
        fun onItemClick(note: Note)
    }

}