package com.example.noteapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.Model.Note
import com.example.noteapp.R
//Adapter is what provides a connection between the data source and view
//database - Adapter - recycler view
class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(){

    var notes : List<Note> = ArrayList();

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val textViewTitle : TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewDescription : TextView = itemView.findViewById(R.id.textViewDescription)
        val cardView : CardView = itemView.findViewById(R.id.cardView)
    }

    //Create a holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)

        return NoteViewHolder(view);
    }

    // transfer data from object to noteViewHolder
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        var currentNote : Note = notes[position]

        holder.textViewTitle.text = currentNote.title
        holder.textViewDescription.text = currentNote.description

    }

    override fun getItemCount(): Int {
        return notes.size
    }
    //pass notes to adapter class but we have observed data from database so use method below:
    fun setNote(myNotes : List<Note>){
        this.notes = myNotes
        notifyDataSetChanged()
    }

    fun getNote(position: Int) : Note{

        return notes[position]
    }


}