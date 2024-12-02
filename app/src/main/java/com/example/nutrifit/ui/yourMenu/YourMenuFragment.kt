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

        binding.tvProgressbar.visibility = View.VISIBLE

        val yourMenus = listOf(
            YourMenu.MenuItem("Breakfast", "Healthy Oatmeal with Fruits", "A nutritious start to your day.", R.drawable.default_food),
            YourMenu.MenuItem("Breakfast", "Avocado Toast", "A healthy and tasty breakfast.", R.drawable.default_food),
            YourMenu.MenuItem("Breakfast", "Scrambled Eggs with Toast", "A classic breakfast to energize your morning.", R.drawable.default_food),

            YourMenu.MenuItem("Lunch", "Grilled Chicken Salad", "High-protein, low-calorie meal.", R.drawable.default_food),
            YourMenu.MenuItem("Lunch", "Vegetable Stir Fry", "A healthy and colorful lunch choice.", R.drawable.default_food),
            YourMenu.MenuItem("Lunch", "Chicken Wrap", "A quick and nutritious meal.", R.drawable.default_food),

            YourMenu.MenuItem("Dinner", "Steamed Fish and Vegetables", "Light and healthy dinner option.", R.drawable.default_food),
            YourMenu.MenuItem("Dinner", "Spaghetti with Tomato Sauce", "A hearty meal for dinner.", R.drawable.default_food),
            YourMenu.MenuItem("Dinner", "Grilled Salmon with Lemon", "A perfect dinner with omega-3.", R.drawable.default_food)
        )

        val limitedMenus = mutableListOf<YourMenu>()
        val categories = listOf("Breakfast", "Lunch", "Dinner")

        categories.forEach { category ->
            limitedMenus.add(YourMenu.CategoryLabel(category))

            yourMenus.filterIsInstance<YourMenu.MenuItem>()
                .filter { menuItem -> menuItem.category == category }
                .take(2)
                .forEach { limitedMenus.add(it) }
        }

        yourMenuAdapter = YourMenuAdapter(limitedMenus)
        binding.recyclerviewYourMenu.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = yourMenuAdapter
        }

        view.postDelayed({
            binding.tvProgressbar.visibility = View.GONE
        }, 1000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}