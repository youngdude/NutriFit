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
            YourMenu("Breakfast", "Avocado Toast", "A healthy and tasty breakfast.", R.drawable.default_food),
            YourMenu("Breakfast", "Scrambled Eggs with Toast", "A classic breakfast to energize your morning.", R.drawable.default_food),
            YourMenu("Lunch", "Grilled Chicken Salad", "High-protein, low-calorie meal.", R.drawable.default_food),
            YourMenu("Lunch", "Vegetable Stir Fry", "A healthy and colorful lunch choice.", R.drawable.default_food),
            YourMenu("Lunch", "Chicken Wrap", "A quick and nutritious meal.", R.drawable.default_food),
            YourMenu("Dinner", "Steamed Fish and Vegetables", "Light and healthy dinner option.", R.drawable.default_food),
            YourMenu("Dinner", "Spaghetti with Tomato Sauce", "A hearty meal for dinner.", R.drawable.default_food),
            YourMenu("Dinner", "Grilled Salmon with Lemon", "A perfect dinner with omega-3.", R.drawable.default_food)
        )

        val groupedMenus = yourMenus.groupBy { it.category }

        val limitedMenus = groupedMenus.flatMap { entry ->
            entry.value.take(3)
        }

        if (yourMenus.isNotEmpty()) {
            binding.labelCategory.text = yourMenus[0].category
        }

        yourMenuAdapter = YourMenuAdapter(yourMenus)
        binding.recyclerviewYourMenu.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = yourMenuAdapter
        }

        view.postDelayed({
            val layoutManager = LinearLayoutManager(context)
            binding.recyclerviewYourMenu.layoutManager = layoutManager
            binding.recyclerviewYourMenu.setHasFixedSize(true)

            yourMenuAdapter = YourMenuAdapter(limitedMenus)
            binding.recyclerviewYourMenu.adapter = yourMenuAdapter

            binding.progressbarYourMenu.visibility = View.GONE
        }, 1000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}