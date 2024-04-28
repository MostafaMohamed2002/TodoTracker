package com.mostafadevo.noteluxe.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mostafadevo.noteluxe.R
import com.mostafadevo.noteluxe.databinding.FragmentListBinding

class listFragment : Fragment() {
    private lateinit var _binding: FragmentListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        _binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val data = ArrayList<String>()
        data.add("Note 1")
        data.add("Note 2")
        data.add("Note 3")
        data.add("Note 4")
        data.add("Note 5")
        data.add("Note 6")
        data.add("Note 7")
        data.add("Note 8")
        data.add("Note 9")
        data.add("Note 10")
        // This will pass the ArrayList to our Adapter
        val adapter = ListNotesAdapter(data)

        // Setting the Adapter with the recyclerview
        _binding.recyclerView.adapter = adapter
        _binding.createNewNoteFab.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
        return _binding.root
    }

}