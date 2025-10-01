package com.example.newhabit.presentation.habitBacklog

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newhabit.R
import com.example.newhabit.databinding.HabitBacklogItemBinding
import com.example.newhabit.domain.model.Habit
import com.example.newhabit.domain.model.HabitCategory

/**
 * RecyclerView adapter for displaying a list of Habits.
 *
 * The UI is based on the [HabitBacklogItemBinding].
 * We use the [Habit] as a model for the binding.
 */
class HabitBacklogListAdapter(
    private val onItemClick: (Habit) -> Unit
) : ListAdapter<Habit, HabitBacklogListAdapter.HabitBacklogViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitBacklogViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HabitBacklogItemBinding.inflate(layoutInflater, parent, false)
        return HabitBacklogViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: HabitBacklogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HabitBacklogViewHolder(
        private val binding: HabitBacklogItemBinding,
        private val onItemClick: (Habit) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(habit: Habit) {
            binding.backlogTitleTextView.text = habit.title
            val chip = binding.backlogChipView

            chip.text = habit.category.label

            val context = chip.context
            val bgColor = ContextCompat.getColor(context, habit.category.getColorRes())
            val textColor = ContextCompat.getColor(context, habit.category.getOnColorRes())

            chip.chipBackgroundColor = ColorStateList.valueOf(bgColor)
            chip.setTextColor(textColor)
            chip.setChipIconTintResource(habit.category.getOnColorRes())
            chip.chipIcon = ContextCompat.getDrawable(context, habit.category.getIconRes())

            binding.root.setOnClickListener {
                onItemClick(habit)
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Habit>() {

        override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem == newItem
        }
    }
}