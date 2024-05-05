package com.mostafadevo.todo.view.fragments.update

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mostafadevo.todo.R
import com.mostafadevo.todo.Utils
import com.mostafadevo.todo.data.model.Todo
import com.mostafadevo.todo.data.viewmodel.SharedTodoViewModel
import com.mostafadevo.todo.databinding.FragmentUpdateBinding


class updateFragment : Fragment() {
    private lateinit var _binding: FragmentUpdateBinding
    private val mSharedTodoViewModel: SharedTodoViewModel by viewModels()
    private val safeArgsData by navArgs<updateFragmentArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.updatePrioritySpinner.onItemSelectedListener =
            mSharedTodoViewModel.prioritySelectionListener
        logSafeArgsData()
        retreiveDataFromSafeArgs()
        saveUpdatedTodo()
    }

    private fun saveUpdatedTodo() {
        _binding.updateNoteButton.setOnClickListener {
            val title = _binding.updateTitleTextinput.editText?.text.toString()
            val description = _binding.updateDescriptionTextinput.editText?.text.toString()
            val priority =
                Utils.parsePriorityFromStringToEnum(_binding.updatePrioritySpinner.selectedItem.toString())
            val id = safeArgsData.currentTodo.id
            val updatedTodo: Todo = Todo(
                id,
                title,
                priority,
                description
            )
            // TODO: Validate Date Before updating
            mSharedTodoViewModel.updateTodo(updatedTodo)
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            Toast.makeText(requireContext(), "Todo Updated", Toast.LENGTH_SHORT)
                .show()

        }
    }

    private fun retreiveDataFromSafeArgs() {
        _binding.apply {
            updateTitleTextinput.editText?.setText(safeArgsData.currentTodo.title)
            updateDescriptionTextinput.editText?.setText(safeArgsData.currentTodo.description)
            updatePrioritySpinner.setSelection(Utils.parsePriorityToInt(safeArgsData.currentTodo.priority))
        }
    }

    private fun logSafeArgsData() {
        Log.i("updateFragment", safeArgsData.currentTodo.id.toString())
        Log.i("updateFragment", safeArgsData.currentTodo.title)
        Log.i("updateFragment", safeArgsData.currentTodo.priority.toString())
        Log.i("updateFragment", safeArgsData.currentTodo.description)
    }

}