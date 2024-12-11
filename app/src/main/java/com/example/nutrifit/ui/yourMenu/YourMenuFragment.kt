package com.example.nutrifit.ui.yourMenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutrifit.adapter.YourMenuAdapter
import com.example.nutrifit.data.YourMenu
import com.example.nutrifit.databinding.FragmentYourMenuBinding
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.InputStreamReader

class YourMenuFragment : Fragment() {

    private var _binding: FragmentYourMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var yourMenuAdapterBreakfast: YourMenuAdapter
    private lateinit var yourMenuAdapterLunch: YourMenuAdapter
    private lateinit var yourMenuAdapterDinner: YourMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYourMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvProgressbar.visibility = View.VISIBLE
        setupRecyclerViews()

        val allMenuItems = loadMenuDataFromCsv()

        val menuLookup = allMenuItems.associateBy { it.title }

        val pagi = arguments?.getStringArrayList("pagi")
        val siang = arguments?.getStringArrayList("siang")
        val malam = arguments?.getStringArrayList("malam")

        val breakfastItems = pagi?.mapNotNull { menuLookup[it] } ?: emptyList()
        val lunchItems = siang?.mapNotNull { menuLookup[it] } ?: emptyList()
        val dinnerItems = malam?.mapNotNull { menuLookup[it] } ?: emptyList()

        yourMenuAdapterBreakfast.updateData(breakfastItems)
        yourMenuAdapterLunch.updateData(lunchItems)
        yourMenuAdapterDinner.updateData(dinnerItems)

        binding.tvProgressbar.visibility = View.GONE
    }

    private fun setupRecyclerViews() {
        yourMenuAdapterBreakfast = YourMenuAdapter(emptyList())
        yourMenuAdapterLunch = YourMenuAdapter(emptyList())
        yourMenuAdapterDinner = YourMenuAdapter(emptyList())

        binding.rvBreakfast.layoutManager = LinearLayoutManager(context)
        binding.rvLunch.layoutManager = LinearLayoutManager(context)
        binding.rvDinner.layoutManager = LinearLayoutManager(context)

        binding.rvBreakfast.adapter = yourMenuAdapterBreakfast
        binding.rvLunch.adapter = yourMenuAdapterLunch
        binding.rvDinner.adapter = yourMenuAdapterDinner
    }

    private fun loadMenuDataFromCsv(): List<YourMenu.MenuItem> {
        val menuItems = mutableListOf<YourMenu.MenuItem>()
        val inputStream = requireContext().assets.open("resep.csv")
        val reader = InputStreamReader(inputStream)
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

        csvParser.forEach { record ->
            val title = record.get("nama_makanan")
            val type = record.get("jenis")
            val imageUrl = record.get("image")
            menuItems.add(YourMenu.MenuItem(title, type, imageUrl))
        }

        csvParser.close()
        return menuItems
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
