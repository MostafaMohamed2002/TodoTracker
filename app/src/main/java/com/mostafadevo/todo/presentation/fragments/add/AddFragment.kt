package com.mostafadevo.todo.presentation.fragments.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.mostafadevo.todo.Utils
import com.mostafadevo.todo.data.model.Todo
import com.mostafadevo.todo.presentation.SharedTodoViewModel
import com.mostafadevo.todo.databinding.FragmentAddBinding
import java.util.Calendar
import java.util.Date
import java.util.UUID


class addFragment : Fragment() {
    private  var dateTime : Date = Date()
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
        (activity as AppCompatActivity).setSupportActionBar(_binding.toolbar)
        _binding.duetimeInputlayout.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                .setMinute(Calendar.getInstance().get(Calendar.MINUTE))
                .setTitleText("Select Time")
                .build()
            timePicker.show(childFragmentManager, "Time Picker")
            timePicker.addOnPositiveButtonClickListener {
                val hour= timePicker.hour
                val minute = timePicker.minute
                dateTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }.time
                Log.d("AddFragment", "date: $dateTime")
                _binding.duetimeInputlayout.editText?.setText("$hour:$minute")
            }
        }
        _binding.duedateInputlayout.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            datePicker.show(childFragmentManager, "Date Picker")
            datePicker.addOnPositiveButtonClickListener {
                dateTime.date = datePicker.headerText.split(" ")[2].toInt()
                Log.d("AddFragment", "date: $dateTime")
                _binding.duedateInputlayout.editText?.setText(datePicker.headerText)
            }
        }
        _binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()

        }
        customizeTheDateTimePickers()
        _binding.addTodoFab.setOnClickListener {
            addTodo()
            //change priority spinner color based on selected item
        }
        changePrioritySpinnerColor()

    }


    private fun customizeTheDateTimePickers() {
        //handle ux for date and time pickers
        _binding.duedateInputlayout.editText?.isFocusable = false
        _binding.duetimeInputlayout.editText?.isFocusable = false
        _binding.duedateInputlayout.editText?.isClickable = true
        _binding.duetimeInputlayout.editText?.isClickable = true


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
        val isNotEmpty = title.isNotEmpty() == true
        if (isNotEmpty) {
            val parsedPriority = Utils.parsePriorityFromStringToEnum(priority)
            val newTodo = Todo(
                id, title, parsedPriority, description, isCompleted = false, dateAndTime = dateTime,deleted = false
            )
            viewModel.insertTodo(newTodo)
            findNavController().navigateUp()
            Toast.makeText(requireContext(), "Todo Added", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(requireContext(), "Fill", Toast.LENGTH_SHORT).show()
    }



}
