package com.example.whiterabbitdemo.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.whiterabbitdemo.model.Address
import com.example.whiterabbitdemo.model.Company
import com.example.whiterabbitdemo.model.EmployeeResponseItem
import com.example.whiterabbitdemo.model.Geo

class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(
        context, DATABASE_NAME,
        null, DATABASE_VERSION
    ) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "detail.db"
        private const val TBL_NAME = "Profile_Master"

        private const val ID = "id"
        private const val NAME = "name"
        private const val USERNAME = "username"
        private const val EMAIL = "email"
        private const val PROFILE_IMAGE = "profile_image"


        private const val ADDRESS_STREET = "street"
        private const val ADDRESS_SUITE = "suite"
        private const val ADDRESS_CITY = "city"
        private const val ADDRESS_ZIP = "zipcode"
        private const val ADDRESS_GEOLAT = "lat"
        private const val ADDRESS_GEOLNG = "lng"

        private const val PHONE = "phone"
        private const val WEBSITE = "website"

        private const val COMPANY_NAME = "company_name"
        private const val COMPANY_BS = "company_bs"
        private const val COMPANY_CATCH = "company_catch"

    }

    override fun onCreate(db: SQLiteDatabase?) {

        val createProfileTable = ("create table " + TBL_NAME +
                " (" + ID + " integer primary key, "
                + NAME + " text, "
                + USERNAME + " text, "
                + EMAIL + " text, "
                + PROFILE_IMAGE + " text, "
                + ADDRESS_STREET + " text, "
                + ADDRESS_SUITE + " text, "
                + ADDRESS_CITY + " text, "
                + ADDRESS_ZIP + " text, "
                + ADDRESS_GEOLAT + " text, "
                + ADDRESS_GEOLNG + " text, "
                + PHONE + " text, "
                + WEBSITE + " text, "
                + COMPANY_NAME + " text, "
                + COMPANY_BS + " text, "
                + COMPANY_CATCH + " text);")


        db?.execSQL(createProfileTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TBL_NAME")
        onCreate(db)
    }

    fun insertDetail(pmt: EmployeeResponseItem): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()

        if (pmt.id == null) {
            contentValues.put(ID, "")
        } else {
            contentValues.put(ID, pmt.id)
        }

        if (pmt.name == null) {
            contentValues.put(NAME, "")
        } else {
            contentValues.put(NAME, pmt.name)
        }

        if (pmt.username == null) {
            contentValues.put(USERNAME, "")
        } else {
            contentValues.put(USERNAME, pmt.username)
        }

        if (pmt.email == null) {
            contentValues.put(EMAIL, "")
        } else {
            contentValues.put(EMAIL, pmt.email)
        }

        if (pmt.profile_image == null) {
            contentValues.put(PROFILE_IMAGE, "")
        } else {
            contentValues.put(PROFILE_IMAGE, pmt.profile_image)
        }

        if (pmt.address == null) {
            contentValues.put(ADDRESS_STREET, "")
            contentValues.put(ADDRESS_CITY, "")
            contentValues.put(ADDRESS_SUITE, "")
            contentValues.put(ADDRESS_ZIP, "")
            contentValues.put(ADDRESS_GEOLAT, "")
            contentValues.put(ADDRESS_GEOLNG, "")
        } else {
            contentValues.put(ADDRESS_STREET, pmt.address.street)
            contentValues.put(ADDRESS_CITY, pmt.address.city)
            contentValues.put(ADDRESS_SUITE, pmt.address.suite)
            contentValues.put(ADDRESS_ZIP, pmt.address.zipcode)
            contentValues.put(ADDRESS_GEOLAT, pmt.address.geo!!.lat)
            contentValues.put(ADDRESS_GEOLNG, pmt.address.geo!!.lng)
        }

        if (pmt.phone == null) {
            contentValues.put(PHONE, "")
        } else {
            contentValues.put(PHONE, pmt.phone)
        }

        if (pmt.website == null) {
            contentValues.put(WEBSITE, "")
        } else {
            contentValues.put(WEBSITE, pmt.website)
        }

        if (pmt.company == null) {
            contentValues.put(COMPANY_NAME, "")
            contentValues.put(COMPANY_BS, "")
            contentValues.put(COMPANY_CATCH, "")
        } else {
            contentValues.put(COMPANY_NAME, pmt.company.name)
            contentValues.put(COMPANY_BS, pmt.company.bs)
            contentValues.put(COMPANY_CATCH,pmt.company.catchPhrase)
        }


        val success = db.insert(TBL_NAME, null, contentValues)
        db.close()

        return success
    }

    @SuppressLint("Recycle", "Range")
    fun getDetail(): ArrayList<EmployeeResponseItem> {
        val pmtList: ArrayList<EmployeeResponseItem> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_NAME"
        val db = this.readableDatabase

        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var username: String
        var email: String
        var profile_image: String
        var street: String
        var suite:String
        var city:String
        var zipcode:String
        var lat:String
        var lng:String
        var phone: String
        var website: String
        var company: String
        var company_bs: String
        var company_catch: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                username = cursor.getString(cursor.getColumnIndex("username"))
                email = cursor.getString(cursor.getColumnIndex("email"))
                profile_image = cursor.getString(cursor.getColumnIndex("profile_image"))

                street = cursor.getString(cursor.getColumnIndex("street"))
                suite = cursor.getString(cursor.getColumnIndex("suite"))
                city = cursor.getString(cursor.getColumnIndex("city"))
                zipcode = cursor.getString(cursor.getColumnIndex("zipcode"))
                lat = cursor.getString(cursor.getColumnIndex("lat"))
                lng = cursor.getString(cursor.getColumnIndex("lng"))

                val addressDetail = Address()
                val geo = Geo()
                geo.lat = lat
                geo.lng = lng
                addressDetail.street = street
                addressDetail.suite = suite
                addressDetail.city = city
                addressDetail.zipcode = zipcode
                addressDetail.geo = geo

                phone = cursor.getString(cursor.getColumnIndex("phone"))
                website = cursor.getString(cursor.getColumnIndex("website"))
                company = cursor.getString(cursor.getColumnIndex("company_name"))
                company_bs = cursor.getString(cursor.getColumnIndex("company_bs"))
                company_catch = cursor.getString(cursor.getColumnIndex("company_catch"))

                val companyDetail = Company()
                companyDetail.name = company
                companyDetail.bs = company_bs
                companyDetail.catchPhrase = company_catch


                val pmt = EmployeeResponseItem(
                    id = id,
                    name = name,
                    profile_image = profile_image,
                    email = email,
                    username = username,
                    phone = phone,
                    address = addressDetail,
                    website = website,
                    company = companyDetail
                )
                pmtList.add(pmt)

            } while (cursor.moveToNext())

        }

        return pmtList
    }
}