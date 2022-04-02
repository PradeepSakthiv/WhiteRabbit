package com.example.whiterabbitdemo.model

import java.io.Serializable


data class EmployeeResponseItem (
	val profile_image: String? = null,
	val website: String? = null,
	val address: Address? = null,
	val phone: String? = null,
	val name: String? = null,
	val company: Company? = null,
	val id: Int? = null,
	val email: String? = null,
	val username: String? = null
) : Serializable

data class Address(
	var zipcode: String? = null,
	var geo: Geo? = null,
	var suite: String? = null,
	var city: String? = null,
	var street: String? = null
)

data class Company(
	var bs: String? = null,
	var catchPhrase: String? = null,
	var name: String? = null
)

data class Geo(
	var lng: String? = null,
	var lat: String? = null
)

