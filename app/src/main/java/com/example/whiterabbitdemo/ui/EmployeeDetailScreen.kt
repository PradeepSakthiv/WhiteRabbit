package com.example.whiterabbitdemo.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.whiterabbitdemo.utils.ModelPreferencesManager
import com.example.whiterabbitdemo.R
import com.example.whiterabbitdemo.model.EmployeeResponseItem
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class EmployeeDetailScreen : AppCompatActivity() {

    private val details = ModelPreferencesManager.get<EmployeeResponseItem>("details")

    private lateinit var employeeImage: CircleImageView
    private lateinit var name: TextView
    private lateinit var username: TextView
    private lateinit var email: TextView
    private lateinit var address: TextView
    private lateinit var phone: TextView
    private lateinit var website: TextView
    private lateinit var company: TextView


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_detail_screen)

        employeeImage = findViewById(R.id.employee_image)
        name = findViewById(R.id.name)
        username = findViewById(R.id.username)
        email = findViewById(R.id.email)
        address = findViewById(R.id.address)
        phone = findViewById(R.id.phone)
        website = findViewById(R.id.website)
        company = findViewById(R.id.company)


        if(details!!.profile_image.toString()!=""){
            Picasso.get().load(details.profile_image.toString()).into(employeeImage)
        }

        name.text = details.name.toString()
        username.text = details.username.toString()
        email.text = details.email.toString()

        if(details.address!!.street!=""){
            address.text = details.address.street+
                    "\n"+details.address.suite+
                    "\n"+details.address.city +
                    "\n"+details.address.zipcode +
                    "\n"+"Lat - "+ details.address.geo!!.lat +
                    "\n"+"Lng - "+ details.address.geo!!.lng
        }

        phone.text = details.phone
        website.text = details.website

        if(details.company!!.name!=""){
            company.text = details.company.name+
                    "\n"+details.company.bs+
                    "\n"+details.company.catchPhrase
        }

    }
}