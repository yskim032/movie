package com.example.a1202_movieseat.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.a1202_movieseat.Activity.FilmDetailActivity
import com.example.a1202_movieseat.Models.Film
import com.example.a1202_movieseat.databinding.ViewholderFilmBinding


class FilmListAdapter(private val items: ArrayList<Film>): RecyclerView.Adapter<FilmListAdapter.ViewHolder>() {

    private var context: Context?=null

    inner class ViewHolder(private val binding: ViewholderFilmBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Film) {
            binding.nameTxt.text = item.Title // 수정: item 사용
            val requestOptions = RequestOptions() // 수정: RequestOptions 오타 수정
                .transforms(CenterCrop(), RoundedCorners(30))

            Glide.with(context!!)
                .load(item.Poster)
                .apply(requestOptions)
                .into(binding.pic)

            binding.root.setOnClickListener {
                val intent = Intent(context, FilmDetailActivity::class.java)
                intent.putExtra("object", item)
                context!!.startActivity(intent) // 클릭 시 상세 화면으로 이동

            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmListAdapter.ViewHolder {
        context=parent.context
        val binding=ViewholderFilmBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmListAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])

    }

    override fun getItemCount(): Int=items.size


}
