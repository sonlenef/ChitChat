package tech.leson.chitchat.ui.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<VH : BaseViewHolder<*>, M>(var data: MutableList<M>) :
    RecyclerView.Adapter<VH>() {
    abstract fun addData(data: MutableList<M>)
    abstract fun clearData()
}
