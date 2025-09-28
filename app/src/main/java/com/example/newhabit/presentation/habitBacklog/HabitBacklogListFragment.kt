package com.example.newhabit.presentation.habitBacklog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newhabit.databinding.FragmentHabitBacklogListBinding
import com.example.newhabit.presentation.form.HabitFormFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HabitBacklogListFragment : Fragment() {

    private var _binding: FragmentHabitBacklogListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HabitBacklogListAdapter
    private lateinit var viewModel: HabitBacklogListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HabitBacklogListViewModel::class.java]
        adapter = HabitBacklogListAdapter { habit ->
            val action = HabitFormFragmentDirections
                .actionHabitListToHabitForm(habitId = habit.id)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitBacklogListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set the adapter
        binding.habitBacklogRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.habitBacklogRecyclerView.adapter = adapter

        // Observer UI State for changes.
        viewModel.uiState.observe(viewLifecycleOwner) {
            // Submit the new list to the adapter
            adapter.submitList(it.habitItemList)
        }

        parentFragmentManager.setFragmentResultListener("habit_updated", viewLifecycleOwner) { _, _ ->
            viewModel.refresh() // faz o fetch manual
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
}