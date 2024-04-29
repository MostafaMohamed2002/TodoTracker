package com.mostafadevo.todo.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mostafadevo.todo.R
import com.mostafadevo.todo.data.viewmodel.TodoViewModel
import com.mostafadevo.todo.databinding.FragmentListBinding

class listFragment : Fragment() {
    private lateinit var _binding: FragmentListBinding
    private val viewModel: TodoViewModel by viewModels()

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
//        val factory = TodoViewModel.TodoViewModelFactory(requireActivity().application)
//        viewModel = ViewModelProvider(this, factory).get(TodoViewModel::class.java)
        setupRecyclerView()
        _binding.createNewNoteFab.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
        (activity as AppCompatActivity).setSupportActionBar(_binding.toolbar)
        setHasOptionsMenu(true)
    }

    private fun setupRecyclerView() {
        _binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getAllTodos().observe(viewLifecycleOwner, Observer {
            val adapter = ListNotesAdapter(it)
            _binding.recyclerView.adapter = adapter
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteall) {
            viewModel.deleteAllTodos()
            val snackbar=Snackbar.make(requireView(), "All notes deleted", Snackbar.LENGTH_SHORT)
            snackbar.setAnchorView(_binding.createNewNoteFab)
            snackbar.show()
        }
        return super.onOptionsItemSelected(item)
    }


}