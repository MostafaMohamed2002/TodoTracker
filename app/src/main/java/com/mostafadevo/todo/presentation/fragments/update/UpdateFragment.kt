package com.mostafadevo.todo.presentation.fragments.update

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.mostafadevo.todo.R
import com.mostafadevo.todo.Utils
import com.mostafadevo.todo.data.model.Todo
import com.mostafadevo.todo.presentation.SharedTodoViewModel
import com.mostafadevo.todo.databinding.FragmentUpdateBinding
import java.util.Calendar
import java.util.Date


class updateFragment : Fragment() {
    private lateinit var _binding: FragmentUpdateBinding
    private val mSharedTodoViewModel: SharedTodoViewModel by viewModels()
    private val safeArgsData by navArgs<updateFragmentArgs>()
    private lateinit var mDate: Date
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDate = safeArgsData.currentTodo.dateAndTime
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
        _binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        logSafeArgsData()
        retreiveDataFromSafeArgs()
        saveUpdatedTodo()
        handleDateAndTimePicker()
    }

    private fun handleDateAndTimePicker() {
        _binding.updateDuedateInputlayout.editText?.isFocusable = false
        _binding.updateDuetimeInputlayout.editText?.isFocusable = false
        _binding.updateDuedateInputlayout.editText?.isClickable = true
        _binding.updateDuetimeInputlayout.editText?.isClickable = true

        _binding.updateDuedateInputlayout.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(mDate.time)
                .build()
            datePicker.show(childFragmentManager, "Date Picker")
            datePicker.addOnPositiveButtonClickListener {
                mDate.date = datePicker.headerText.split(" ")[2].toInt()
                Log.d("AddFragment", "date: $mDate")
                _binding.updateDuedateInputlayout.editText?.setText(datePicker.headerText)
            }

        }
        _binding.updateDuetimeInputlayout.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(mDate.hours)
                .setMinute(mDate.minutes)
                .setTitleText("Select Time")
                .build()
            timePicker.show(childFragmentManager, "Time Picker")
            timePicker.addOnPositiveButtonClickListener {
                val hour = timePicker.hour
                val minute = timePicker.minute
                mDate = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }.time
                Log.d("AddFragment", "date: $mDate")
                _binding.updateDuetimeInputlayout.editText?.setText("$hour:$minute")

            }

        }
    }

    private fun saveUpdatedTodo() {
        _binding.updateNoteButton.setOnClickListener {
            val title = _binding.updateTitleTextinput.editText?.text.toString()
            val description = _binding.updateDescriptionTextinput.editText?.text.toString()
            val priority =
                Utils.parsePriorityFromStringToEnum(_binding.updatePrioritySpinner.selectedItem.toString())
            val id = safeArgsData.currentTodo.id
            val isCompleted = safeArgsData.currentTodo.isCompleted
            val updatedTodo: Todo = Todo(
                id,
                title,
                priority,
                description,
                isCompleted,
                mDate,deleted = false
            )
            // TODO: Validate Date Before updating
            mSharedTodoViewModel.updateTodo(updatedTodo)
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            Toast.makeText(requireContext(), "Todo Updated", Toast.LENGTH_SHORT)
                .show()

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun retreiveDataFromSafeArgs() {
        _binding.apply {
            updateTitleTextinput.editText?.setText(safeArgsData.currentTodo.title)
            updateDescriptionTextinput.editText?.setText(safeArgsData.currentTodo.description)
            updatePrioritySpinner.setSelection(Utils.parsePriorityToInt(safeArgsData.currentTodo.priority))
            updateDuedateInputlayout.editText?.setText(Utils.formatDate(safeArgsData.currentTodo.dateAndTime))
            updateDuetimeInputlayout.editText?.setText(Utils.formatTime(safeArgsData.currentTodo.dateAndTime))
        }
    }

    private fun logSafeArgsData() {
        Log.i("updateFragment", safeArgsData.currentTodo.id.toString())
        Log.i("updateFragment", safeArgsData.currentTodo.title)
        Log.i("updateFragment", safeArgsData.currentTodo.priority.toString())
        Log.i("updateFragment", safeArgsData.currentTodo.description)
        Log.i("updateFragment", safeArgsData.currentTodo.isCompleted.toString())
        Log.i("updateFragment", safeArgsData.currentTodo.dateAndTime.toString())
    }

}