package com.apex.codeassesment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.databinding.ItemUserBinding

class UserAdapter(
    private val userList: MutableList<User>,
    private val clickListener: OnItemClickListener
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(user: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user, clickListener)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, clickListener: OnItemClickListener) {
            binding.user = user
            binding.root.setOnClickListener {
                clickListener.onItemClick(user)
            }
            binding.executePendingBindings()
        }
    }

    fun updateUserList(newList: List<User>) {
        userList.clear()
        userList.addAll(newList)
        notifyDataSetChanged()
    }
}