package com.mostafadevo.todo.view.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mostafadevo.todo.Utils
import com.mostafadevo.todo.data.model.Todo
import com.mostafadevo.todo.data.viewmodel.SharedTodoViewModel
import com.mostafadevo.todo.databinding.FragmentAddBinding
import java.util.UUID


class addFragment : Fragment() {
    private lateinit var _binding: FragmentAddBinding
    private val viewModel: SharedTodoViewModel by viewModels()

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
        _binding.addPrioritySpinner.onItemSelectedListener = viewModel.prioritySelectionListener
    }

    private fun addTodo() {
        val id = UUID.randomUUID().toString()
        val title = _binding.addTitleTextinput.editText?.text.toString()
        val description = _binding.addDescriptionTextinput.editText?.text.toString()
        val priority = _binding.addPrioritySpinner.selectedItem.toString()
        //check if those text fields not empty
        val isNotEmpty = title.isNotEmpty() == true && description.isNotEmpty() == true
        if (isNotEmpty) {
            val parsedPriority = Utils.parsePriorityFromStringToEnum(priority)
            val newTodo = Todo(
                id, title, parsedPriority, description
            )
            viewModel.insertTodo(newTodo)
            findNavController().navigateUp()
            Toast.makeText(requireContext(), "Todo Added", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(requireContext(), "Fill", Toast.LENGTH_SHORT).show()
    }

}