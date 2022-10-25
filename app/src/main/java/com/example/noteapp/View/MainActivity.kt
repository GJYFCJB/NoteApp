package com.example.noteapp.View

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.Adapters.NoteAdapter
import com.example.noteapp.Model.Note
import com.example.noteapp.NoteApplication
import com.example.noteapp.R
import com.example.noteapp.ViewModel.NoteViewModel
import com.example.noteapp.ViewModel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var noteViewModel: NoteViewModel

    lateinit var addActivityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModelFactory = NoteViewModelFactory((application as NoteApplication).repository)

        noteViewModel = ViewModelProvider(this, viewModelFactory).get(NoteViewModel::class.java);
        val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val noteAdapter = NoteAdapter();
        recyclerView.adapter = noteAdapter;

        //register activity for result
        registerActivityResultLauncher()

        //Live data to observe changes in database
        noteViewModel.myAllNotes.observe(this, Observer { notes ->

            //update UI
            noteAdapter.setNote(notes)

        })

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.delete(noteAdapter.getNote(viewHolder.adapterPosition))
            }
        }).attachToRecyclerView(recyclerView)//connect touch to recyclerview
    }

    fun registerActivityResultLauncher(){

        addActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), {
            resultAddNote ->

                val resultCode = resultAddNote.resultCode
            // Note title and note description that we send using intent will be in the data object
            // Here we will be able to get the title and description using data object
                val data = resultAddNote.data

            if(resultCode == RESULT_OK && data != null){

                val noteTitle : String = data.getStringExtra("title").toString()
                val noteDescription : String = data.getStringExtra("description").toString()

                val note = Note(noteTitle, noteDescription)
                noteViewModel.insert(note)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.item_add_note -> {
                val intent = Intent(this, NoteAddActivity::class.java)
                addActivityResultLauncher.launch(intent)
            }

            R.id.item_delete_all_notes -> showDialogMessage()
        }
        return true
    }

    fun showDialogMessage(){

        val dialogMessage = AlertDialog.Builder(this)
        dialogMessage.setTitle("Delete All Notes")
        dialogMessage.setMessage("If click , del all")
        dialogMessage.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.cancel()
        } )

        dialogMessage.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
            noteViewModel.deleteAllNotes()
        })

        dialogMessage.create().show()

    }


}