package com.formationandroid.madrental

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_vehicle.*

class VehicleFragment : Fragment() {
    private lateinit var currentId: String;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val arguments = requireArguments()

        // Getting the values
        currentId = arguments.getString(getString(R.string.currentId)).toString()
        val textViewNameVehicle = arguments.getString(getString(R.string.textViewNameVehicle))
        val textViewDayPriceVehicle = arguments.getString(getString(R.string.textViewDayPriceVehicle))
        val textViewCategoryCO2Vehicle = arguments.getString(getString(R.string.textViewCategoryCO2Vehicle))

        // Setting text with these values
        vehicle_name.text = textViewNameVehicle
        vehicle_day_price.text = textViewDayPriceVehicle
        vehicle_category_co2.text = textViewCategoryCO2Vehicle
    }
}