package com.example.newhabit.presentation.habitList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newhabit.R
import com.example.newhabit.data.local.database.AppDatabase
import com.example.newhabit.data.repository.HabitProgressRepositoryImpl
import com.example.newhabit.data.repository.HabitRepositoryImpl
import com.example.newhabit.databinding.FragmentHabitListBinding
import com.example.newhabit.domain.usecase.GetHabitsForTodayUseCaseImpl
import com.example.newhabit.domain.usecase.ToggleProgressUseCaseImpl
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HabitListFragment : Fragment() {
    private var _binding: FragmentHabitListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HabitListAdapter
    private lateinit var viewModel: HabitListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HabitListViewModel::class.java]
        adapter = HabitListAdapter(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set the adapter
        binding.habitRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.habitRecyclerView.adapter = adapter

        // Adding decorations to our recycler view
        addingDividerDecoration()

        // Observer UI State for changes.
        viewModel.uiState.observe(viewLifecycleOwner) {
            // Submit the new list to the adapter
            adapter.submitList(it.habitItemList)
        }

        // Set Navigation Fab
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_habitList_to_habitForm)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addingDividerDecoration() {
        // Create a divider
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)

        // Adding the line at the end of the list
        divider.isLastItemDecorated = false

        val resources = requireContext().resources

        // Adding padding to the start and end of the line
        divider.dividerInsetStart = resources.getDimensionPixelSize(R.dimen.horizontal_margin)
        divider.dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.horizontal_margin)

        // Defining size of the line
        divider.dividerThickness = resources.getDimensionPixelSize(R.dimen.divider_height)

        // Defining color of the line
        divider.dividerColor = ContextCompat.getColor(requireContext(), R.color.primary_200)

        // Set the divider to the RecyclerView
        binding.habitRecyclerView.addItemDecoration(divider)
    }
}