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

        binding.progressbarYourMenu.visibility = View.VISIBLE

        val yourMenus = listOf(
            YourMenu("Breakfast", "Healthy Oatmeal with Fruits", "A nutritious start to your day.", R.drawable.default_food),
            YourMenu("Lunch", "Grilled Chicken Salad", "High-protein, low-calorie meal.", R.drawable.default_food),
            YourMenu("Dinner", "Steamed Fish and Vegetables", "Light and healthy dinner option.", R.drawable.default_food),
            YourMenu("Breakfast", "Scrambled Eggs with Toast", "A classic breakfast to energize your morning.", R.drawable.default_food),
            YourMenu("Lunch", "Vegetable Stir Fry", "A healthy and colorful lunch choice.", R.drawable.default_food),
            YourMenu("Dinner", "Spaghetti with Tomato Sauce", "A hearty meal for dinner.", R.drawable.default_food)
        )

        val breakfast = yourMenus.filter { it.category == "Breakfast" }.firstOrNull()
        val lunch = yourMenus.filter { it.category == "Lunch" }.firstOrNull()
        val dinner = yourMenus.filter { it.category == "Dinner" }.firstOrNull()

        val singleMenu = listOfNotNull(breakfast, lunch, dinner)

        view.postDelayed({
            val layoutManager = LinearLayoutManager(context)
            binding.recyclerviewYourMenu.layoutManager = layoutManager
            binding.recyclerviewYourMenu.setHasFixedSize(true)

            yourMenuAdapter = YourMenuAdapter(singleMenu)
            binding.recyclerviewYourMenu.adapter = yourMenuAdapter

            binding.progressbarYourMenu.visibility = View.GONE
        }, 1000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}