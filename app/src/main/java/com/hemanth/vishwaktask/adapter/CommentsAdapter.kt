package com.hemanth.vishwaktask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hemanth.vishwaktask.data.local.entity.CommentEntity
import com.hemanth.vishwaktask.databinding.CommentItemBinding

class CommentsAdapter(private val commnetsList: List<CommentEntity>): RecyclerView.Adapter<CommentsAdapter.CommentHolder>() {

    class CommentHolder(commentItemBinding: CommentItemBinding): RecyclerView.ViewHolder(commentItemBinding.root) {
        val txtCommenterName = commentItemBinding.txtCommenterName
        val txtComment = commentItemBinding.txtComment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        return CommentHolder(CommentItemBinding.inflate(
            LayoutInflater.from(parent.context)
        ))
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        holder.txtCommenterName.text = commnetsList[position].userId
        holder.txtComment.text = commnetsList[position].comment
    }

    override fun getItemCount(): Int = commnetsList.size
}