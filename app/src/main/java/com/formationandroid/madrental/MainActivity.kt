package com.formationandroid.madrental

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.formationandroid.madrental.db.AppDatabaseHelper
import com.formationandroid.madrental.db.VehicleDTO
import com.formationandroid.madrental.ws.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_vehicle.*
import kotlinx.android.synthetic.main.fragment_vehicle.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val serviceRetrofit = RetrofitSingleton.retrofit.create(WSInterface::class.java)
    private var vehiclesAdapter: VehiclesAdapter? = null
    private var switchState: Boolean = false;

    companion object {
        lateinit var currentId: String
        lateinit var textViewNameVehicle: String
        lateinit var textViewDayPriceVehicle: String
        lateinit var textViewCategoryCO2Vehicle: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // If we saved something then we get the switch state
        // We do that because when we get the db information in portrait mode and go in landscape mode
        // It doesn't work and show us the WS list without changing the switch state
        // With that if you select db list in portrait and go in landscape mode it will keep the db list
        if (savedInstanceState != null)      {
            switchState = savedInstanceState.getBoolean(getString(R.string.main_switch_state))
        }

        // Set the vehicle depending on the switch button value (should be false by default)
        this.setVehicles(switchState)
    }

    private fun setVehicles(needDb: Boolean) {
        if (needDb) {
            this.setVehiclesFromInternalDb();
        }
        else {
            this.setVehiclesFromWS();
        }
    }

    // When we switch portrait to landscape we register the state of the switch
    override fun onSaveInstanceState(outState: Bundle)   {
        outState.putBoolean(getString(R.string.main_switch_state), switch_button_ws_db.isChecked)
        super.onSaveInstanceState(outState)
    }

    private fun setVehiclesFromInternalDb() {
        val layoutManager = LinearLayoutManager(this@MainActivity)
        list_vehicles.layoutManager = layoutManager

        // Getting the list of vehicles from the db
        val list = AppDatabaseHelper.getDatabase(this).vehiclesDAO().getListVehicles().toMutableList()

        // Set the list of VehicleDTO to a MutableList of ReturnWSVehicle
        var vehiclesWS: MutableList<ReturnWSVehicle> = this.setVehiclesDTOToVehiclesWS(list);

        // Setting the adapter
        vehiclesAdapter = VehiclesAdapter(vehiclesWS, getString(R.string.main_day_price), getString(R.string.main_category_co2), this@MainActivity, true)
        list_vehicles.adapter = vehiclesAdapter

        // Notify the view that the list changed
        vehiclesAdapter?.notifyDataSetChanged()
    }

    // Allow us to work with the same object than in the WS
    private fun setVehiclesDTOToVehiclesWS(listVehicles: MutableList<VehicleDTO>): MutableList<ReturnWSVehicle> {
        val newList: MutableList<ReturnWSVehicle> = ArrayList();

        for (vehicle in listVehicles) {
            newList.add(ReturnWSVehicle(vehicle.id.toString(), vehicle.nom, vehicle.image, vehicle.prixjournalierbase, vehicle.categorieco2))
        }

        return newList;
    }

    private fun setVehiclesFromWS() {
        // If the user is not connected to internet we display him a toast of error
        if (!NetworkHelper.isConnectedToNetwork(this))
        {
            Toast.makeText(this, R.string.main_error_internet_connection, Toast.LENGTH_LONG).show()
        }
        else {
            // Make the spinner visible so we see it when we are calling the WS
            progress_bar.visibility = View.VISIBLE

            // Calling the Web Service
            this.callingWebService()
        }
    }

    private fun callingWebService() {
        val call = serviceRetrofit.getVehicles()
        call.enqueue(object : Callback<MutableList<ReturnWSVehicle>>
        {
            override fun onResponse(
                call: Call<MutableList<ReturnWSVehicle>>,
                response: Response<MutableList<ReturnWSVehicle>>
            ) {
                // If the http request is successful
                if (response.isSuccessful)
                {
                    // Reading the body
                    val returnWSVehicles = response.body()

                    val layoutManager = LinearLayoutManager(this@MainActivity)
                    list_vehicles.layoutManager = layoutManager

                    // Settings the list of values
                    vehiclesAdapter = returnWSVehicles?.let { VehiclesAdapter(it, getString(R.string.main_day_price), getString(R.string.main_category_co2), this@MainActivity, false) }
                    list_vehicles.adapter = vehiclesAdapter

                    // Notify the view to say the list changed (doesn't refresh without this)
                    vehiclesAdapter?.notifyDataSetChanged()

                    // Remove the progress bar since we've set the list
                    progress_bar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<MutableList<ReturnWSVehicle>>, t: Throwable)
            {
                // Since it goes wrong we set a toast to inform the user and then we remove the progress bar since we are done
                Toast.makeText(this@MainActivity, R.string.main_error_connection_ws, Toast.LENGTH_LONG).show()
                progress_bar.visibility = View.GONE
            }
        })
    }

    fun getVehiclesFromWSOrInternalDb(view: View) {
        // OnClick method called when you click on the switch button
        this.setVehicles(view.switch_button_ws_db.isChecked)
    }

    fun setVehicleToFav(view: View) {
        // When we click we check if we have this id in db or not.
        // If we do, then just display a toast
        // else insert it in db then display a toast
        if (AppDatabaseHelper.getDatabase(this).vehiclesDAO().countVehicleById(currentId.toLong()) == 0.toLong()) {
            // I did it with static object because i had a bug when you try to add multiple object in landscape mode
            AppDatabaseHelper.getDatabase(this).vehiclesDAO().insert(VehicleDTO(currentId.toLong(), textViewNameVehicle, "", textViewDayPriceVehicle, textViewCategoryCO2Vehicle))
            Toast.makeText(this, R.string.vehicle_fragment_add_to_fav, Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(this, R.string.vehicle_fragment_error_already_added_to_fav, Toast.LENGTH_LONG).show()
        }
    }
}