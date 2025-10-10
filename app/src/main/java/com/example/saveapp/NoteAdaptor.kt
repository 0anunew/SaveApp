package com.example.saveapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class NoteAdaptor() : RecyclerView.Adapter<NoteAdaptor.NoteViewHolder>() {
    private var notesList = mutableListOf<Note>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {
        return NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false))
    }

    override fun onBindViewHolder(
        holder: NoteViewHolder,
        position: Int
    ) {
        holder.textTitle.text = notesList[position].title
        holder.textContent.text = notesList[position].content
        holder.textPriority.text = notesList[position].priority.toString()
    }

    override fun getItemCount() = notesList.size

    fun setNotes(notes:MutableList<Note>){
        notesList = notes
        notifyDataSetChanged()
    }

    fun getNoteAt(position: Int) = notesList[position]

    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var textTitle: TextView = itemView.findViewById(R.id.title)
        var textContent: TextView = itemView.findViewById(R.id.content)
        var textPriority: TextView = itemView.findViewById(R.id.priority)

    }
}