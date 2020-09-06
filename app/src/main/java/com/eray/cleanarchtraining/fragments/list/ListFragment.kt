package com.eray.cleanarchtraining.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.eray.cleanarchtraining.R
import com.eray.cleanarchtraining.data.models.TodoData
import com.eray.cleanarchtraining.data.viewmodels.TodoViewModel
import com.eray.cleanarchtraining.databinding.FragmentListBinding
import com.eray.cleanarchtraining.fragments.SharedViewModel
import com.eray.cleanarchtraining.fragments.list.adapter.ListAdapter
import com.eray.cleanarchtraining.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val mTodoViewModel: TodoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.i("TAG", "ALLAh")
        _binding = FragmentListBinding.inflate(inflater,container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel
        // Setup recyclerview
        setupRecyclerView()

        mTodoViewModel.getAllData.observe(viewLifecycleOwner, Observer {data ->
            mSharedViewModel.checkIfNoData(data)
            adapter.setData(data)
        })

        // Set Menu
        setHasOptionsMenu(true)

        // Hide keyboard.
        hideKeyboard(requireActivity())

        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }


        // Setting up swipe to delete.
        swipeToRemove(recyclerView)
    }

    private fun swipeToRemove(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToRemove = adapter.dataList[viewHolder.adapterPosition]
                // Remove/delete item.
                mTodoViewModel.removeData(itemToRemove)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                // Restore removed data.
                restoreDeletedData(viewHolder.itemView, itemToRemove, viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, removedItem: TodoData, position: Int) {
        val snackbar = Snackbar.make(
            view, "Removed todo '${removedItem.title}' ",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo") {
            mTodoViewModel.insertData(removedItem)
        }
        snackbar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_delete_all -> confirmMassRemoval()
            R.id.menu_priority_high -> mTodoViewModel.sortByHighPriority.observe(this, Observer { adapter.setData(it) })
            R.id.menu_priority_low -> mTodoViewModel.sortByLowPriority.observe(this, Observer { adapter.setData(it) })
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        var searchQuery = "%$query%"
        mTodoViewModel.searchDatabase(searchQuery).observe(this, Observer { list ->
            list?.let {
                adapter.setData(it)
            }
        })
    }

    private fun confirmMassRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _,_ ->
            mTodoViewModel.removeAll()
            Snackbar.make(
                requireView(),
                "Successfully removed all todo items.",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.setTitle("Delete Everything")
        builder.setMessage("Are you sure you want to remove all todo data?")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}