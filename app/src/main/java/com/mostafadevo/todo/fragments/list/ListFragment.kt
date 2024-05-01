package com.mostafadevo.todo.fragments.list

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mostafadevo.todo.R
import com.mostafadevo.todo.data.viewmodel.TodoViewModel
import com.mostafadevo.todo.databinding.FragmentListBinding

class listFragment : Fragment() {
    private lateinit var _binding: FragmentListBinding
    private val viewModel: TodoViewModel by activityViewModels()

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
        setupSearchFunction()
        (activity as AppCompatActivity).setSupportActionBar(_binding.toolbar)
        setHasOptionsMenu(true)
    }

    private fun setupSearchFunction() {
        val madapter = ListNotesAdapter()
        _binding.searchRecyclerView.adapter = madapter
        _binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        _binding.searchView.editText?.addTextChangedListener {
            //if edit text is empty show all notes
            if (it.toString().isEmpty()) {

            } else {
                viewModel.search(it.toString())
                viewModel.searchedTodos.observe(viewLifecycleOwner, Observer {
                    madapter.setData(it)
                })
            }
        }
    }

    private fun setupRecyclerView() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.layout_animation_fall_down)
        val controller = LayoutAnimationController(animation)
        _binding.recyclerView.layoutAnimation = controller
        _binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ListNotesAdapter()
        _binding.recyclerView.adapter = adapter
        viewModel.sortedData.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                _binding.emptyImageView.visibility = View.VISIBLE
                _binding.emptyTextView.visibility= View.VISIBLE
            } else {
                _binding.emptyImageView.visibility = View.GONE
                _binding.emptyTextView.visibility= View.GONE
            }
            adapter.setData(it)
            _binding.recyclerView.scheduleLayoutAnimation()
            Log.d("TodoActivity", "Received todos: $it")
        })


        // Create an instance of ItemTouchHelper.SimpleCallback to handle swipe gestures on RecyclerView items.
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

            // This method is called when an item is moved within the RecyclerView.
            // Since we're not handling drag-and-drop in this case, we simply return false.
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            // This method is called when an item is swiped off the screen.
            // Here, we handle the logic for deleting the swiped item from our data set.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Get the position of the swiped item.
                val position = viewHolder.adapterPosition

                // Retrieve the item to be deleted from the adapter's data set.
                val itemToDelete = adapter.getItems()[position]

                // Notify the adapter that an item has been removed from the data set.
                adapter.notifyItemRemoved(position)

                // Delete the swiped item from the database using the ViewModel.
                viewModel.deleteTodoItem(itemToDelete)

                val deleteTodoSnacbar = Snackbar.make(requireView(), "Note deleted", Snackbar.LENGTH_SHORT)
                deleteTodoSnacbar.setAnchorView(_binding.createNewNoteFab)
                deleteTodoSnacbar.setAction("Undo") {
                    viewModel.insertTodo(itemToDelete)
                }
                deleteTodoSnacbar.show()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                val swipePercentage = dX / viewHolder.itemView.width
                viewHolder.itemView.alpha = 1 - Math.abs(swipePercentage)
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.alpha = 1.0f
            }
        }

        // Create an instance of ItemTouchHelper with the callback we defined above.
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)

        // Attach the ItemTouchHelper to the RecyclerView. This enables swipe-to-delete functionality on the RecyclerView items.
        itemTouchHelper.attachToRecyclerView(_binding.recyclerView)

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
        else if (item.itemId == R.id.sort_by) {
            val sortBottomSheet = SortBottomSheetFragment()
            sortBottomSheet.show(childFragmentManager, sortBottomSheet.tag)
        }
        return super.onOptionsItemSelected(item)
    }
}