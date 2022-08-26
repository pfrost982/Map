package ru.gb.map.ui.markers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.gb.map.ui.MainViewModel
import ru.gb.map.ViewModelSaver
import ru.gb.map.databinding.DialogNewMarkBinding
import ru.gb.map.databinding.FragmentMarkersBinding

class MarkersFragment : Fragment() {

    private var _binding: FragmentMarkersBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = (requireActivity() as ViewModelSaver).getViewModel()
        _binding = FragmentMarkersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager
                .VERTICAL, false
        )
        val adapter = Adapter(
            { placeMark ->
                viewModel.deletePlaceMark(placeMark)
            },
            { position ->
                editMark(position)
            }
        )
        recyclerView.adapter = adapter
        viewModel.liveDataPlaceMarkersList.observe(viewLifecycleOwner) { adapter.setData(it) }
    }

    private fun editMark(position: Int) {
        val dialogView = DialogNewMarkBinding.inflate(layoutInflater)
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        dialogBuilder.setView(dialogView.root)
        val inputName: EditText = dialogView.dialogInputName
        val inputDescription: EditText = dialogView.dialogInputDescription
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("Ok") { _, _ ->
                viewModel.updatePlaceMark(
                    position,
                    inputName.text.toString(),
                    inputDescription.text.toString()
                )
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}