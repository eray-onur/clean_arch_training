package com.eray.cleanarchtraining.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.renderscript.RenderScript
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.eray.cleanarchtraining.R
import com.eray.cleanarchtraining.data.models.Priority
import com.eray.cleanarchtraining.data.models.TodoData
import com.eray.cleanarchtraining.data.viewmodels.TodoViewModel
import com.eray.cleanarchtraining.databinding.FragmentUpdateBinding
import com.eray.cleanarchtraining.fragments.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import kotlinx.android.synthetic.main.row_layout.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mTodoViewModel: TodoViewModel by viewModels()

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.args = args

        setHasOptionsMenu(true)

        binding.currentSpinner.onItemSelectedListener = mSharedViewModel.listener


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> removeItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateItem() {
        val title = edtTextTitleUpdate.text.toString()
        val description = edtTextDescriptionUpdate.text.toString()
        val priorityStr = currentSpinner.selectedItem.toString()
        val validation = mSharedViewModel.validateData(title, description)

        if(validation) {
            // Update current item
            val updatedItem = TodoData(
                args.currentItem.id,
                title,
                mSharedViewModel.parsePriority(priorityStr),
                description
            )

            mTodoViewModel.updateData(updatedItem)
            Snackbar.make(requireView(), "Successfully updated!", Snackbar.LENGTH_SHORT).show()
            // Navigate back after update.
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        else {
            Snackbar.make(requireView(), "Please enter the data in a proper format.", Snackbar.LENGTH_SHORT).show()
           }
    }

    // Inquire from the user whether if they want to delete the todo.
    private fun removeItem() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _,_ ->
            mTodoViewModel.removeData(args.currentItem)
            Snackbar.make(
                requireView(),
                "Successfully removed the todo: ${args.currentItem.title}",
                Snackbar.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.setTitle("Delete '${args.currentItem.title}'")
        builder.setMessage("Are you sure you want to remove '${args.currentItem.title}'?")
        builder.create().show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}