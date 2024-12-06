package com.example.nutrifit.ui.yourMenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutrifit.R
import com.example.nutrifit.adapter.YourMenuAdapter
import com.example.nutrifit.data.YourMenu
import com.example.nutrifit.databinding.FragmentYourMenuBinding
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.InputStreamReader

class YourMenuFragment : Fragment() {

    private var _binding: FragmentYourMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var yourMenuAdapter: YourMenuAdapter

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

        val yourMenus = getMenuData() // Get data from CSV or your source

        yourMenuAdapter = YourMenuAdapter(yourMenus)
        binding.recyclerviewYourMenu.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = yourMenuAdapter
        }

        view.postDelayed({
            binding.tvProgressbar.visibility = View.GONE
        }, 1000)
    }

    private fun getMenuData(): List<YourMenu> {
        val menuList = mutableListOf<YourMenu>()
        try {
            val csvInputStream = requireContext().assets.open("menu.csv")
            val reader = InputStreamReader(csvInputStream)
            val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

            val groupedMenuItems = csvParser.records.groupBy { it.get("category") }

            groupedMenuItems.forEach { (category, records) ->
                val menuItems = records.map {
                    YourMenu.MenuItem(
                        name = it.get("name"),
                        description = it.get("description"),
                        imageRes = R.drawable.default_food // Or map to a real image resource
                    )
                }
                menuList.add(YourMenu(category, menuItems))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return menuList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
