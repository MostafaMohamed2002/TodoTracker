package com.mostafadevo.todo.fragments.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mostafadevo.todo.R
import com.mostafadevo.todo.data.model.Priority
import com.mostafadevo.todo.data.model.Todo
import com.mostafadevo.todo.data.viewmodel.TodoViewModel
import com.mostafadevo.todo.databinding.FragmentAddBinding


class addFragment : Fragment() {
    private lateinit var _binding: FragmentAddBinding
    private val viewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.addNoteButton.setOnClickListener {
            addTodo()
            //change priority spinner color based on selected item
        }
        changePrioritySpinnerColor()
    }

    private fun changePrioritySpinnerColor() {
        _binding.addPrioritySpinner.onItemSelectedListener=viewModel.listner
    }

    private fun addTodo() {
        val title = _binding.addTitleTextinput.editText?.text.toString()
        val description = _binding.addDescriptionTextinput.editText?.text.toString()
        val priority = _binding.addPrioritySpinner.selectedItem.toString()
        //check if those text fields not empty
        val isNotEmpty = title.isNotEmpty() == true && description.isNotEmpty() == true
        if (isNotEmpty) {
            val parsedPriority = parsePriority(priority)
            val newTodo = Todo(
                0, title, parsedPriority, description
            )
            viewModel.insertTodo(newTodo)
            Toast.makeText(requireContext(), "added", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(requireContext(), "Fill", Toast.LENGTH_SHORT).show()
    }

    private fun parsePriority(priority: String): Priority {
        return when (priority) {
            "High" -> Priority.HIGH
            "Medium" -> Priority.MEDIUM
            "Low" -> Priority.LOW
            else -> {
                Priority.LOW
            }
        }
    }

}