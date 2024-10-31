package com.example.marketplacepuj.feature.profile.view.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplacepuj.feature.profile.view.adapter.factory.ManageTerminalTypeFactoryForList
import com.example.marketplacepuj.feature.profile.view.adapter.factory.MenuOptionTypeFactory
import com.example.marketplacepuj.feature.profile.view.adapter.viewholder.AbstractViewHolder
import com.example.marketplacepuj.feature.profile.view.adapter.viewmodels.AbstractViewModel
import kotlin.properties.Delegates

class ProfileOptionsAdapter :
    RecyclerView.Adapter<AbstractViewHolder<AbstractViewModel>>(),
    AbstractViewHolder.OnItemClickedListener {

    private var listener: ManageTerminalAdapterListener? = null
    private lateinit var context: Context
    private val typeFactory: MenuOptionTypeFactory = ManageTerminalTypeFactoryForList()

    var items: List<AbstractViewModel> by Delegates.observable(ArrayList()) { _, _, _ ->
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: AbstractViewHolder<AbstractViewModel>, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.setOnItemClickListener(this)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AbstractViewHolder<AbstractViewModel> {
        context = parent.context
        val binding = typeFactory.createViewBinding(parent, viewType)
        return typeFactory.createViewHolder(
            binding,
            viewType
        ) as AbstractViewHolder<AbstractViewModel>
    }

    override fun getItemViewType(position: Int): Int = items[position].type(typeFactory)

    override fun getItemCount() = items.count()

    fun addItem(item: AbstractViewModel) {
        addItem(item, 0)
    }

    fun addItem(item: AbstractViewModel, position: Int) {
        if (!items.contains(item)) {
            (items as ArrayList).add(position, item)
            notifyItemInserted(position)
        }
    }

    override fun onItemClicked(position: Int) {

    }

    fun setListener(listener: ManageTerminalAdapterListener) {
        this.listener = listener
    }

    override fun onClickCloseSession() {
        listener?.onClickCloseSession()
    }

    override fun onClickChangePassword() {
        listener?.onClickChangePassword()
    }

    override fun onClickEditProfile() {
        listener?.onClickEditProfile()
    }

    override fun onClickOrderHistory() {
        listener?.onClickOrderHistory()
    }

    interface ManageTerminalAdapterListener {
        fun onClickChangePassword()
        fun onClickEditProfile()
        fun onClickOrderHistory()
        fun onClickCloseSession()
    }
}

