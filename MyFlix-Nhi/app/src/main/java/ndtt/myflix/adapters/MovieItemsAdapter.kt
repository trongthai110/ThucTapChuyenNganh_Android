package ndtt.myflix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.item_movie.view.*
import ndtt.myflix.R
import ndtt.myflix.models.MovieList
import ndtt.myflix.models.Results

open class MovieItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Results>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_movie,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
            Glide
                .with(context.applicationContext)
                .load("https://image.tmdb.org/t/p/w500" + model.poster_path)
                .centerCrop()
                .into(holder.itemView.ivItemMovie)

            holder.itemView.tvTitle.text = model.title

            holder.itemView.tvRD.text = setReleaseDate(model.release_date)

            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    private fun setReleaseDate(releaseDate: String): String{
        val date = releaseDate.substring(8,10)
        val month = releaseDate.substring(5,7)
        val year = releaseDate.substring(0,4)
        val newRD = date + "-" + month + "-" + year
        return newRD
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int,model: Results)
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}