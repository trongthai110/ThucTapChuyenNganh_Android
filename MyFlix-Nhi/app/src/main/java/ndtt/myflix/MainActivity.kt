package ndtt.myflix

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ndtt.myflix.DAO.DetailDao
import ndtt.myflix.DAO.DetailDb

import ndtt.myflix.Services.API
import ndtt.myflix.Services.MoviesApi
import ndtt.myflix.adapters.MovieItemsAdapter
import ndtt.myflix.models.MovieList
import ndtt.myflix.models.Results


class MainActivity : AppCompatActivity() {

    val movieApi = API.getInstance().create(MoviesApi::class.java)
    private lateinit var detailDao: DetailDao

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //------room------
        val db = Room.databaseBuilder(
            applicationContext,
            DetailDb::class.java, "dsphim"
        ).allowMainThreadQueries().build()

        detailDao = db.detailDao()

        rcvMain.layoutManager = LinearLayoutManager(this)

        val list: ArrayList<Results> = detailDao.getAll() as ArrayList<Results>

        if (isOnline(this)) {
            getListMovie()
        } else {
            populateListToUI(list)
        }
    }

    private fun getListMovie(){
        var movieList: ArrayList<Results> = ArrayList()

        GlobalScope.launch {
            val result = movieApi.getListMovie()
            movieList.addAll(result.body()!!.results)
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                populateListToUI(movieList)
            })
        }
    }

    fun populateListToUI(movieList: ArrayList<Results>) {
        //tạo ra một Trình quản lý bố cục dọc

        rcvMain.layoutManager = LinearLayoutManager(this)

        val adapter = MovieItemsAdapter(this, movieList)
        rcvMain.adapter = adapter//
        adapter.setOnClickListener(object: MovieItemsAdapter.OnClickListener {
            override fun onClick(position: Int, model: Results) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("id", model.id)
                startActivity(intent)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}