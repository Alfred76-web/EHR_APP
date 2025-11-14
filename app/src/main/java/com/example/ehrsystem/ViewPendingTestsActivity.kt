package com.example.ehrsystem

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ehrsystem.database.LabTest
import com.example.ehrsystem.viewmodel.LabTestViewModel

class ViewPendingTestsActivity : AppCompatActivity() {

    private lateinit var labTestViewModel: LabTestViewModel
    private lateinit var rvPendingTests: RecyclerView
    private lateinit var tvTestCount: TextView
    private lateinit var layoutEmpty: LinearLayout
    private lateinit var btnBack: Button
    private lateinit var labTestAdapter: LabTestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pending_tests)

        // Initialize views
        rvPendingTests = findViewById(R.id.rvPendingTests)
        tvTestCount = findViewById(R.id.tvTestCount)
        layoutEmpty = findViewById(R.id.layoutEmpty)
        btnBack = findViewById(R.id.btnBack)

        // Initialize ViewModel
        labTestViewModel = ViewModelProvider(this)[LabTestViewModel::class.java]

        // Setup RecyclerView
        setupRecyclerView()

        // Observe pending tests
        observePendingTests()

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        labTestAdapter = LabTestAdapter(emptyList()) { labTest ->
            openEnterResultsActivity(labTest)
        }
        rvPendingTests.apply {
            layoutManager = LinearLayoutManager(this@ViewPendingTestsActivity)
            adapter = labTestAdapter
        }
    }

    private fun observePendingTests() {
        labTestViewModel.pendingLabTests.observe(this) { tests ->
            tests?.let {
                labTestAdapter.updateLabTests(it)
                updateUI(it)
            }
        }
    }

    private fun updateUI(tests: List<LabTest>) {
        if (tests.isEmpty()) {
            rvPendingTests.visibility = View.GONE
            layoutEmpty.visibility = View.VISIBLE
            tvTestCount.text = "Pending Tests: 0"
        } else {
            rvPendingTests.visibility = View.VISIBLE
            layoutEmpty.visibility = View.GONE
            tvTestCount.text = "Pending Tests: ${tests.size}"
        }
    }

    private fun openEnterResultsActivity(labTest: LabTest) {
        val intent = Intent(this, EnterTestResultsActivity::class.java)
        intent.putExtra("TEST_ID", labTest.testId)
        startActivity(intent)
    }
}