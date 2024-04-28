package com.mostafadevo.noteluxe.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mostafadevo.noteluxe.R

class ListNotesAdapter(private val mList: List<String>) : RecyclerView.Adapter<ListNotesAdapter.ViewHolder>() {

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
        holder.textView.text = itemInList
        //sets the image to the imageview from our itemHolder class
        holder.imgview.setBackgroundResource(R.drawable.baseline_inbox_24)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imgview = itemView.findViewById<View>(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textView2)
    }
}