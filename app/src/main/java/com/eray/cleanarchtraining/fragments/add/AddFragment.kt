package com.eray.cleanarchtraining.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eray.cleanarchtraining.R
import com.eray.cleanarchtraining.data.models.Priority
import com.eray.cleanarchtraining.data.models.TodoData
import com.eray.cleanarchtraining.data.viewmodels.TodoViewModel
import com.eray.cleanarchtraining.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*


class AddFragment : Fragment() {

    private val mTodoViewModel: TodoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)


        setHasOptionsMenu(true)

        view.spinnerPriority.onItemSelectedListener = mSharedViewModel.listener

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add) {
            insertToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertToDb() {
        val mTitle = edtTextTitle.text.toString()
        val mPriority = spinnerPriority.selectedItem.toString()
        val mDescription = edtTextDescription.text.toString()

        val isValidated = mSharedViewModel.validateData(mTitle, mDescription)

        if(isValidated) {

            Log.d("[VALIDATION]", "Entered Todo Data is in valid format.")

            val newData = TodoData(
                0,
                mTitle,
                mSharedViewModel.parsePriority(mPriority),
                mDescription
            )

            mTodoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Successfully added Todo item!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)

        } else {
            Toast.makeText(requireContext(), "Please fill out all the fields properly.", Toast.LENGTH_LONG).show()

        }
    }



}