package com.mostafadevo.todo.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mostafadevo.todo.R
import com.mostafadevo.todo.databinding.FragmentListBinding

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

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        (activity as AppCompatActivity).setSupportActionBar(_binding.toolbar)
        setHasOptionsMenu(true)
    }

    private fun setupRecyclerView() {
        _binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val data = ArrayList<String>()
        for (i in 0..100) {
            data.add("Note $i")
        }
        val adapter = ListNotesAdapter(data)
        _binding.recyclerView.adapter = adapter
        _binding.createNewNoteFab.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


}