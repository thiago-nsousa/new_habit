package com.example.newhabit.presentation.form

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.newhabit.R
import com.example.newhabit.databinding.FragmentHabitFormBinding
import com.example.newhabit.domain.model.Habit
import com.example.newhabit.domain.model.HabitCategory
import com.google.android.material.chip.Chip
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint

/**
 * A [Fragment] that displays a list of habits.
 */
@AndroidEntryPoint
class HabitFormFragment : DialogFragment() {
    private var _binding: FragmentHabitFormBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HabitFormViewModel
    private var reminderHour: Int = 8 // Valor padrão: 08:00
    private var reminderMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
        viewModel = ViewModelProvider(this)[HabitFormViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val habitId = arguments?.getString("habitId")

        if (habitId != null) {
            viewModel.fetchHabitById(habitId)
            binding.deleteButton.visibility = View.VISIBLE
            binding.toolbar.title = "Editar Hábito"
        } else {
            binding.deleteButton.visibility = View.GONE
        }

        // Observer UI State for changes.
        viewModel.uiState.observe(viewLifecycleOwner) {
            // Submit the new list to the adapter
            if (habitId != null){
                binding.titleTextInput.editText?.setText(it.habit.title)
                binding.categoryDropdown.setText(it.habit.category.label, false)
                binding.reminderSwitch.isChecked = it.habit.reminderEnabled
                reminderHour = it.habit.reminderHour
                reminderMinute = it.habit.reminderMinute
                updateReminderTimeText()
                for (day in it.habit.daysOfWeek) {
                    val chip = binding.daysChipGroup.getChildAt(day - 1) as Chip
                    chip.isChecked = true
                }
            }

            setupReminderSection()
        }

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
                HabitCategory.entries.map { it.label }
            )

        val dropdown = view.findViewById<AutoCompleteTextView>(R.id.categoryDropdown)
        dropdown.setAdapter(adapter)

        dialog?.window?.let { window ->
            WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        }

        binding.saveButton.setOnClickListener {
            if (validateForm()) {
                onSave()
            }
        }

        binding.deleteButton.setOnClickListener { onDelete() }

        binding.toolbar.setNavigationOnClickListener {
            // Volta para a tela anterior
            findNavController().popBackStack()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        parentFragmentManager.setFragmentResult("habit_updated", bundleOf())
    }

    private fun setupReminderSection() {
        // Listener para o switch de lembrete
        binding.reminderSwitch.setOnCheckedChangeListener { _, isChecked ->
            binding.timePickerLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        // Listener para o campo de texto da hora, que abre o seletor de horas
        binding.timePickerEditText.setOnClickListener {
            showTimePicker()
        }

        // Definir a hora padrão inicial no campo de texto
        updateReminderTimeText()
    }

    private fun showTimePicker() {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(reminderHour)
            .setMinute(reminderMinute)
            .setTitleText("Selecione a hora do lembrete")
            .build()

        picker.addOnPositiveButtonClickListener {
            // Atualizar as nossas variáveis com a hora selecionada
            reminderHour = picker.hour
            reminderMinute = picker.minute
            // Atualizar o texto no campo de texto
            updateReminderTimeText()
        }

        picker.show(childFragmentManager, "time_picker_tag")
    }

    private fun updateReminderTimeText() {
        // Formatar a hora e o minuto para exibição (ex: 08:00)
        val formattedTime = String.format("%02d:%02d", reminderHour, reminderMinute)
        binding.timePickerEditText.setText(formattedTime)
    }

    private fun onDelete() {
        // Get value from the input to save
        val habitId = arguments?.getString("habitId") ?: return

        // Use ViewModel to add the new Habit
        viewModel.deleteHabit()

        Toast.makeText(requireContext(), "Hábito removido com sucesso!", Toast.LENGTH_SHORT).show()

        // Navigate Up in the navigation tree, meaning: goes back
        findNavController().navigateUp()
    }

    private fun onSave() {

        // Get value from the input to save
        val habitName = binding.titleTextInput.editText?.text.toString()
        val category = binding.categoryDropdown.text.toString()
        val isReminderEnabled = binding.reminderSwitch.isChecked

        // Get period selected: where 1 is Monday and 7 is Sunday.
        val habitDaysSelected = mutableListOf<Int>()
        for (id in binding.daysChipGroup.checkedChipIds) {
            val chip = binding.daysChipGroup.findViewById<Chip>(id)
            val position = binding.daysChipGroup.indexOfChild(chip)
            habitDaysSelected.add(position + 1)
        }

        val habitId = arguments?.getString("habitId")

        if (habitId != null) {
            viewModel.updateHabit(
                requireContext(),
                Habit(
                    id = habitId,
                    title = habitName,
                    daysOfWeek = habitDaysSelected,
                    isCompleted = viewModel.uiState.value?.habit?.isCompleted ?: false,
                    category = HabitCategory.fromLabel(category),
                    reminderEnabled = isReminderEnabled,
                    reminderHour = reminderHour,
                    reminderMinute = reminderMinute
                )
            )
        } else {
            // Use ViewModel to add the new Habit
            viewModel.addHabit(
                requireContext(),
                habitName,
                habitDaysSelected,
                HabitCategory.fromLabel(category),
                isReminderEnabled,
                reminderHour,
                reminderMinute
            )
        }

        // Navigate Up in the navigation tree, meaning: goes back
        findNavController().navigateUp()
    }

    private fun validateForm(): Boolean {
        var isValid = true

        // TexInputTitle
        val title = binding.titleTextInput.editText?.text?.toString()?.trim()
        if (title.isNullOrEmpty()) {
            binding.titleTextInput.error = "Informe o título do hábito"
            isValid = false
        } else {
            binding.titleTextInput.error = null
        }

        // Dropdown Category
        val category = binding.categoryDropdown.text?.toString()?.trim()
        if (category.isNullOrEmpty()) {
            binding.categoryDropdownLayout.error = "Selecione uma categoria"
            isValid = false
        } else {
            binding.categoryDropdownLayout.error = null
        }

        // Chips Days of Week
        val selectedDays = binding.daysChipGroup.checkedChipIds
        if (selectedDays.isEmpty()) {
            binding.daysErrorTextView.visibility = View.VISIBLE
            binding.daysErrorTextView.text = "Selecione pelo menos um dia"
            isValid = false

        } else {
            binding.daysErrorTextView.visibility = View.GONE
        }

        return isValid
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}