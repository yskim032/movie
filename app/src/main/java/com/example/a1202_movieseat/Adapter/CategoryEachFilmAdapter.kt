
package com.example.a1202_movieseat.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a1202_movieseat.databinding.ViewholderCategoryBinding

class CategoryEachFilmAdapter(private val items: List<String>) :
    RecyclerView.Adapter<CategoryEachFilmAdapter.ViewHolder>() {

    // ViewHolder 클래스
    class ViewHolder(val binding: ViewholderCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    // onCreateViewHolder는 Adapter 클래스에 정의해야 함
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryEachFilmAdapter.ViewHolder {
        val binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryEachFilmAdapter.ViewHolder, position: Int) {
        holder.binding.titleTxt.text = items[position]
    }

    override fun getItemCount(): Int = items.size


}
