package com.mostafadevo.todo.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mostafadevo.todo.R
import com.mostafadevo.todo.data.model.Priority
import com.mostafadevo.todo.data.model.Todo

class ListNotesAdapter() : RecyclerView.Adapter<ListNotesAdapter.ViewHolder>() {
    private var mList: List<Todo> = emptyList()
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_note, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemInList = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.titleTextView.text = itemInList.title
        holder.descriptionTextView.text = itemInList.description
        when(itemInList.priority){
            Priority.HIGH->holder.priorityImgView.setImageResource(R.drawable.priority_high)
            Priority.MEDIUM->holder.priorityImgView.setImageResource(R.drawable.priority_mid)
            Priority.LOW->holder.priorityImgView.setImageResource(R.drawable.priority_low)
        }
        holder.layout.setOnClickListener {
            val navigationAction = listFragmentDirections.actionListFragmentToUpdateFragment(itemInList)
            holder.layout.findNavController().navigate(navigationAction)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title_todo_textview)
        val descriptionTextView:TextView = ItemView.findViewById(R.id.description_todo_textview)
        val priorityImgView :ImageView = ItemView.findViewById(R.id.priority_todo_imgview)
        val layout : ConstraintLayout= ItemView.findViewById(R.id.row_item_note_layout)
    }

    fun setData(todoList: List<Todo>){
        mList = todoList
        notifyDataSetChanged()
    }
}