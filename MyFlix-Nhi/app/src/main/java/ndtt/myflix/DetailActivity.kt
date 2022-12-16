package ndtt.myflix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.substring
import android.util.Log
import androidx.room.Room
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ndtt.myflix.DAO.DetailDao
import ndtt.myflix.DAO.DetailDb
import ndtt.myflix.Services.API
import ndtt.myflix.Services.MoviesApi
import ndtt.myflix.models.Results
import kotlin.properties.Delegates

class DetailActivity : AppCompatActivity() {

    private var movieId by Delegates.notNull<Int>()
    val movieApi = API.getInstance().create(MoviesApi::class.java)
    private lateinit var detailDao: DetailDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //------room------
        val db = Room.databaseBuilder(
            applicationContext,
            DetailDb::class.java, "movieList"
        ).allowMainThreadQueries().build()

        detailDao = db.detailDao()

        setUpActionBar()

        movieId = intent.getIntExtra("id", 0)

        getDetailMovie(movieId)
    }

    private fun getDetailMovie(movieId: Int) {
        var detailMovie: ArrayList<Results> = ArrayList()
        val list: ArrayList<Results> = detailDao.getDetail(movieId) as ArrayList<Results>

        if (list.size == 0) {
            GlobalScope.launch {
                val detail = movieApi.getDetailMovie(movieId)
                detailMovie.addAll(listOf(detail.body()!!))
                detailDao.insertDetail(detailMovie)

                this@DetailActivity.runOnUiThread(java.lang.Runnable {
                    //tvId.text = detailMovie[0].id.toString()
                    tvTitle.text = detailMovie[0].title
                    tvOverview.text = detailMovie[0].overview
                    tvRD.text = "Date released: " + setReleaseDate(detailMovie[0].release_date)
                    Glide
                        .with(this@DetailActivity)
                        .load("https://image.tmdb.org/t/p/w500" + detailMovie[0].poster_path)
                        .centerCrop()
                        .into(imgView)
                })
            }
        } else if (list[0].id == movieId) {

            //tvId.text = list[0].id.toString()
            tvTitle.text = list[0].title
            tvOverview.text = list[0].overview
            tvRD.text = "Date released: " + setReleaseDate(list[0].release_date)
            Glide
                .with(this@DetailActivity)
                .load("https://image.tmdb.org/t/p/w500" + list[0].poster_path)
                .centerCrop()
                .into(imgView)
        }
    }


    private fun setReleaseDate(releaseDate: String): String{
        val date = releaseDate.substring(8,10)
        val month = releaseDate.substring(5,7)
        val year = releaseDate.substring(0,4)
        val newRD = date + "-" + month + "-" + year
        return newRD
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbarMyProfileActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbarMyProfileActivity.setNavigationOnClickListener { onBackPressed() }
    }

}
