package com.example.newhabit.presentation.habitList

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newhabit.databinding.HabitItemBinding
import com.example.newhabit.domain.model.Habit

/**
 * RecyclerView adapter for displaying a list of Habits.
 *
 * The UI is based on the [HabitItemBinding].
 * We use the [Habit] as a model for the binding.
 */
class HabitListAdapter(
    private val viewModel: HabitListViewModel
) : ListAdapter<Habit, HabitListAdapter.HabitViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HabitItemBinding.inflate(layoutInflater, parent, false)
        return HabitViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HabitViewHolder(
        private val binding: HabitItemBinding,
        private val viewModel: HabitListViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(habit: Habit) {
            val textView = binding.titleTextView
            val checkBox = binding.completeCheckBox

            textView.text = habit.title
            checkBox.isChecked = habit.isCompleted
            setStrikeText(habit.isCompleted)


            checkBox.setOnClickListener {
                val isChecked = checkBox.isChecked
                setStrikeText(isChecked)
                viewModel.toggleHabitCompleted(habit.id)
            }
        }

        fun setStrikeText(isChecked: Boolean) {
            val textView = binding.titleTextView
            if (isChecked) {
                textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
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