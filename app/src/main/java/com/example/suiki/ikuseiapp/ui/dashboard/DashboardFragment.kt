package com.example.suiki.ikuseiapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suiki.ikuseiapp.CustomInputViewAdapter
import com.example.suiki.ikuseiapp.IndexWakeAndSleep
import com.example.suiki.ikuseiapp.R
import com.example.suiki.ikuseiapp.databinding.FragmentDashboardBinding
import io.realm.Realm
import io.realm.Sort

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var realm: Realm
    private lateinit var inputAdapter: CustomInputViewAdapter
    private lateinit var inputLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        binding.inputButton.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_inputFragment)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val realmResults = realm.where(IndexWakeAndSleep::class.java)
            .findAll()
            .sort("id", Sort.DESCENDING)

        inputLayoutManager = LinearLayoutManager(requireContext())
        inputAdapter = CustomInputViewAdapter(
            realmResults,
            object: CustomInputViewAdapter.OnButtonClickListener {
                override fun onDelete(id: Int?) {
                    deleteRecord(id)
                    findNavController().navigate(R.id.action_dashboardFragment_to_self)
                    Toast.makeText(context, "削除しました", Toast.LENGTH_SHORT).show()
                }
            }
        )

        binding.inputRecyclerView.layoutManager = inputLayoutManager
        binding.inputRecyclerView.adapter = inputAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun deleteRecord(id: Int?) {
        val record = realm.where(IndexWakeAndSleep::class.java)
            .equalTo("id", id)
            ?.findFirst()

        realm.executeTransaction {
            if (record != null) {
                record.deleteFromRealm()
            }
        }
    }
}
