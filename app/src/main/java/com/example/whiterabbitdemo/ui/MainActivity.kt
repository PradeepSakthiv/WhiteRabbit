package com.example.whiterabbitdemo.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whiterabbitdemo.*
import com.example.whiterabbitdemo.adapter.EmployeeAdapter
import com.example.whiterabbitdemo.model.EmployeeResponseItem
import com.example.whiterabbitdemo.utils.EndPoints
import com.example.whiterabbitdemo.utils.ModelPreferencesManager
import com.example.whiterabbitdemo.utils.SQLiteHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(),
    EmployeeAdapter.ItemClickListener, EmployeeAdapter.SearchValue {

    private lateinit var employeeList: RecyclerView
    private lateinit var progress: LinearLayout
    private lateinit var noData: LinearLayout
    private lateinit var searchEmployee: EditText
    private val endPoints by lazy {
        EndPoints.create()
    }
    var disposable: Disposable? = null
    private lateinit var sqliteHelper: SQLiteHelper
    private lateinit var employeeAdapter: EmployeeAdapter

    var employeeData = ArrayList<EmployeeResponseItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ModelPreferencesManager.with(application)

        employeeList = findViewById(R.id.employee_list)
        searchEmployee = findViewById(R.id.search_employee)
        progress = findViewById(R.id.progress)
        noData = findViewById(R.id.no_data)
        sqliteHelper = SQLiteHelper(this)


        searchEmployee.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString() == "") {
                    getEmployeeListFromUrl()
                } else {
                    employeeAdapter.filter.filter(editable.toString())
                }
            }
        })

        getEmployeeListFromUrl()

    }

    private fun getEmployeeListFromUrl() {
        employeeList.visibility = View.GONE
        noData.visibility = View.GONE
        progress.visibility = View.VISIBLE
        disposable =
            endPoints.getEmployeeList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        insertValueToDatabase(result)
                    },
                    { error ->
                        progress.visibility = View.GONE
                        noData.visibility = View.GONE
                        Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
                    }
                )
    }

    private fun insertValueToDatabase(result: List<EmployeeResponseItem>) {
        for (i in result.indices) {
            sqliteHelper.insertDetail(result[i])
        }
        employeeData = sqliteHelper.getDetail()

        setDetailToList()
    }

    private fun setDetailToList() {
        employeeAdapter = EmployeeAdapter(this, employeeData,this)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        employeeList.layoutManager = mLayoutManager
        employeeList.adapter = employeeAdapter
        employeeList.visibility = View.VISIBLE
        progress.visibility = View.GONE
        noData.visibility = View.GONE
    }

    override fun onItemClick(position: Int, detailsItem: EmployeeResponseItem?) {
        val intent = Intent(this, EmployeeDetailScreen::class.java)
        ModelPreferencesManager.put(detailsItem, "details")
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSearch(type: String, data: ArrayList<EmployeeResponseItem>) {
        employeeData = data
        if (type == "empty") {
            employeeList.visibility = View.GONE
            noData.visibility = View.VISIBLE
        }else{
            noData.visibility = View.GONE
            employeeList.visibility = View.VISIBLE
            setDetailToList()
        }
    }
}
