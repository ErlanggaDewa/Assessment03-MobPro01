package org.d3if1053.hitungzakatpenghasilan.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.d3if1053.hitungzakatpenghasilan.R
import org.d3if1053.hitungzakatpenghasilan.databinding.ItemHistoryBinding
import org.d3if1053.hitungzakatpenghasilan.db.ZakatEntity

class HistoriAdapter : ListAdapter<ZakatEntity, HistoriAdapter.ViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<ZakatEntity>() {
                override fun areItemsTheSame(
                    oldData: ZakatEntity, newData: ZakatEntity
                ): Boolean {
                    return oldData.id == newData.id
                }

                override fun areContentsTheSame(
                    oldData: ZakatEntity, newData: ZakatEntity
                ): Boolean {
                    return oldData == newData
                }
            }
    }

    class ViewHolder(
        private val binding: ItemHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ZakatEntity) = with(binding) {
            itemHistory.text = root.context.getString(R.string.wajib_bayar_zakat, item.totalZakat)
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}