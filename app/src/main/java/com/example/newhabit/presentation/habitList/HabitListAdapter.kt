package com.example.newhabit.presentation.habitList

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newhabit.databinding.HabitItemBinding
import com.example.newhabit.domain.model.Habit

/**
 * RecyclerView adapter for displaying a list of Habits.
 *
 * The UI is based on the [HabitItemBinding].
 * We use the [HabitItem] as a model for the binding.
 */
class HabitListAdapter(
    private val viewModel: HabitListViewModel
) : RecyclerView.Adapter<HabitListAdapter.HabitViewHolder>() {

    private val asyncListDiffer: AsyncListDiffer<Habit> = AsyncListDiffer(this, DiffCallback)
    var habits: List<Habit>
        get() = asyncListDiffer.currentList
        set(value) = asyncListDiffer.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HabitItemBinding.inflate(layoutInflater, parent, false)
        return HabitViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount(): Int = habits.size

    class HabitViewHolder(
        private val binding: HabitItemBinding,
        private val viewModel: HabitListViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(habit: Habit) {
            binding.titleTextView.text = habit.title
            binding.completeCheckBox.isChecked = habit.isCompleted

            binding.completeCheckBox.setOnClickListener {
                Toast.makeText(binding.root.context, "Hábito concluído!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Habit>() {

        override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem.isCompleted == newItem.isCompleted
        }
    }
}