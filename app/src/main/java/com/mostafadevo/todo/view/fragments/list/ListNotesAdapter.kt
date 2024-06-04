package com.mostafadevo.todo.view.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.mostafadevo.todo.R
import com.mostafadevo.todo.Utils
import com.mostafadevo.todo.data.model.Priority
import com.mostafadevo.todo.data.model.Todo

class ListNotesAdapter() : RecyclerView.Adapter<ListNotesAdapter.ViewHolder>() {

    var onItemClick :((Todo) -> Unit)? = null
    var onCheckBoxClick :((Todo) -> Unit)? = null

    private var mList: List<Todo> = emptyList()

    //get the list of items
    fun getItems(): List<Todo> {
        return mList
    }

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
        when (itemInList.priority) {
            //set border color based on priority
            Priority.HIGH -> holder.todoCardView.setStrokeColor(
                holder.itemView.context.resources.getColor(
                    R.color.priority_high
                )
            )

            Priority.MEDIUM -> holder.todoCardView.setStrokeColor(
                holder.itemView.context.resources.getColor(
                    R.color.priority_medium
                )
            )

            Priority.LOW -> holder.todoCardView.setStrokeColor(
                holder.itemView.context.resources.getColor(
                    R.color.priority_low
                )
            )
        }
        holder.layout.setOnClickListener {
            onItemClick?.invoke(itemInList)
        }

        holder.checkbox.setOnCheckedChangeListener(null) //fix the bug of checkbox listener being called multiple times
        holder.checkbox.isChecked = itemInList.isCompleted
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            itemInList.isCompleted = isChecked
            onCheckBoxClick?.invoke(itemInList)
            // Notify item changed with payload to prevent rebinding and flashing.
            notifyItemChanged(position, itemInList)
        }
        holder.dueDate.text = Utils.formatDate(itemInList.dateAndTime)
        holder.dueTime.text = Utils.formatTime(itemInList.dateAndTime)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title_todo_textview)
        val descriptionTextView: TextView = ItemView.findViewById(R.id.description_todo_textview)
        val todoCardView: MaterialCardView = ItemView.findViewById(R.id.todo_cardview)
        val layout: ConstraintLayout = ItemView.findViewById(R.id.row_item_note_layout)
        val checkbox : MaterialCheckBox = ItemView.findViewById(R.id.todo_checkbox)
        val dueDate : TextView = ItemView.findViewById(R.id.due_date_textview)
        val dueTime : TextView = ItemView.findViewById(R.id.duetime_textview)
    }

    fun setData(todoList: List<Todo>) {
        mList = todoList
        notifyDataSetChanged()
    }
}