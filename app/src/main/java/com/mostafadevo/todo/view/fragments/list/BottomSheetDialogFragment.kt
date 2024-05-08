package com.mostafadevo.todo.view.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mostafadevo.todo.R
import com.mostafadevo.todo.data.viewmodel.SharedTodoViewModel

class SortBottomSheetFragment : BottomSheetDialogFragment() {
    private val mSharedTodoViewModel: SharedTodoViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_sort, container, false)
        val sortOptionsGroup = view.findViewById<RadioGroup>(R.id.sort_options_group)
        sortOptionsGroup.check(
            when (mSharedTodoViewModel._sortType.value) {
                "newest" -> R.id.sort_by_newest
                "oldest" -> R.id.sort_by_oldest
                "high priority" -> R.id.sort_by_high_priority
                "low priority" -> R.id.sort_by_low_priority
                "title A to Z" -> R.id.sort_by_title_az
                "title Z to A" -> R.id.sort_by_title_za
                else -> R.id.sort_by_newest
            }
        )
        sortOptionsGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.sort_by_newest -> handleSort("newest")
                R.id.sort_by_oldest -> handleSort("oldest")
                R.id.sort_by_high_priority -> handleSort("high priority")
                R.id.sort_by_low_priority -> handleSort("low priority")
                R.id.sort_by_title_az -> handleSort("title A to Z")
                R.id.sort_by_title_za -> handleSort("title Z to A")
            }
        }


        return view
    }

    private fun handleSort(sortType: String) {
        mSharedTodoViewModel.setSortType(sortType)
        dismiss()
    }
}

