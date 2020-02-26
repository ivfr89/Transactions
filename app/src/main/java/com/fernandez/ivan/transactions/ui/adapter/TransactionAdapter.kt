package com.fernandez.ivan.transactions.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.recyclerview.widget.RecyclerView
import com.fernandez.ivan.transactions.R
import com.fernandez.ivan.transactions.framework.basicDiffUtil
import com.fernandez.ivan.transactions.ui.models.UITransaction
import kotlinx.android.synthetic.main.item_transaction.view.*

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    var items: List<UITransaction> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.id == new.id }
    )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(item: UITransaction) = with(itemView)
        {

            backgroundTintList = ColorStateList.valueOf(if (item.totalAmount() > 0)
                ContextCompat.getColor(
                context,
                R.color.colorGreen
            ) else ContextCompat.getColor(context, R.color.colorRed))

            txtDate.text = item.formatDate()
            txtAmount.text = buildSpannedString {
                appendln(context.getString(R.string.amount))
                bold{
                    append("%.2f".format(item.totalAmount())+context.getString(R.string.currency))
                }
            }
            txtFee.text = context.getString(
                R.string.fees,
                if (item.fee != null && item.fee != 0f)
                    item.fee.toString()+context.getString(R.string.currency)
                else context.getString(R.string.no_fees)
            )
            txtDescription.text =
                if (!item.description.isNullOrEmpty()) item.description else context.getString(R.string.no_description)
        }
    }


}