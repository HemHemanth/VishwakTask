package com.hemanth.vishwaktask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hemanth.vishwaktask.databinding.SocialDetailsItemBinding

class UserDetailsAdapter(private val webUrlList: ArrayList<String>): RecyclerView.Adapter<UserDetailsAdapter.UserSocialDetailsHolder>() {

    class UserSocialDetailsHolder(socialDetailsItemBinding: SocialDetailsItemBinding)
        : RecyclerView.ViewHolder(socialDetailsItemBinding.root) {
            val webViewSocialItem = socialDetailsItemBinding.webViewSocialItem
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSocialDetailsHolder =
        UserSocialDetailsHolder(SocialDetailsItemBinding.inflate(
            LayoutInflater.from(parent.context)
        ))

    override fun onBindViewHolder(holder: UserSocialDetailsHolder, position: Int) {
        holder.webViewSocialItem.loadUrl(webUrlList[position])
    }

    override fun getItemCount(): Int = webUrlList.size
}