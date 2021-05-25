package com.formationandroid.madrental

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.formationandroid.madrental.db.AppDatabaseHelper
import com.formationandroid.madrental.db.VehicleDTO
import kotlinx.android.synthetic.main.fragment_vehicle.*

class VehicleActivity : AppCompatActivity() {
    private lateinit var currentId: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle)

        // VehicleActivity is needed when we are in portrait.
        // When we are in portrait we need to go to a new Activity (VehicleActivity) and then show the fragment.
        // So we need to pass all the information to the activity then to the fragment

        //Getting the information
        currentId = intent.getStringExtra(getString(R.string.currentId));
        val textViewNameVehicle = intent.getStringExtra(getString(R.string.textViewNameVehicle));
        val textViewDayPriceVehicle = intent.getStringExtra(getString(R.string.textViewDayPriceVehicle));
        val textViewCategoryCO2Vehicle = intent.getStringExtra(getString(R.string.textViewCategoryCO2Vehicle));

        // Setting the information to the fragment
        val fragment = VehicleFragment()
        val bundle = Bundle()
        bundle.putString(getString(R.string.textViewNameVehicle), textViewNameVehicle)
        bundle.putString(getString(R.string.textViewDayPriceVehicle), textViewDayPriceVehicle)
        bundle.putString(getString(R.string.textViewCategoryCO2Vehicle), textViewCategoryCO2Vehicle)
        fragment.arguments = bundle

        //Replacing in the view the component with the id "containter_fragment" with our fragment
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container_fragment, fragment)
        transaction.commit()
    }

    fun setVehicleToFav(view: View) {
        // When we click we check if we have this id in db or not.
        // If we do, then just display a toast
        // else insert it in db then display a toast
        if (AppDatabaseHelper.getDatabase(this).vehiclesDAO().countVehicleById(currentId.toLong()) == 0.toLong()) {
            AppDatabaseHelper.getDatabase(this).vehiclesDAO().insert(VehicleDTO(currentId.toLong(), vehicle_name.text.toString(), "", vehicle_day_price.text.toString(), vehicle_category_co2.text.toString()))
            Toast.makeText(this, R.string.vehicle_fragment_add_to_fav, Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(this, R.string.vehicle_fragment_error_already_added_to_fav, Toast.LENGTH_LONG).show()
        }

        var intent = Intent(view.context, MainActivity::class.java);
        view.context.startActivity(intent);
    }
}