package com.harsh.zivameproduct

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_product_feature.view.*
import kotlinx.android.synthetic.main.layout_product_description.view.*

class ProductFeatureAdapter(private val productFeatures: MutableList<ProductFeature?>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedPosition = -1
    private var descriptionPosition = -1
    private var baseProductFeatures: MutableList<ProductFeature?>? = null

    init {
        baseProductFeatures = mutableListOf()
        if (productFeatures != null) {
            baseProductFeatures?.addAll(productFeatures)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)
        if (viewType == R.layout.layout_product_description) {
            val holder = ProductFeatureDescriptionView(view)
            holder.itemView?.iv_cancel?.setOnClickListener {
                holder.itemView.iv_cancel?.isEnabled = false
                unSelectAll()
            }

            return holder
        }

        val holder = ProductFeatureView(view)
        holder.itemView?.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != -1) {
                val oldPosition = selectedPosition
                selectedPosition = currentPosition

                val actualPosition = baseProductFeatures?.indexOf(productFeatures?.get(currentPosition)) ?: 0
                val actualDescriptionPosition = getProductionDescriptionPosition(actualPosition)

                when (descriptionPosition) {
                    -1 -> {
                        descriptionPosition = actualDescriptionPosition
                        productFeatures?.add(descriptionPosition, null)

                        notifyItemChanged(oldPosition)
                        notifyItemChanged(selectedPosition)
                        notifyItemInserted(descriptionPosition)
                    }
                    actualDescriptionPosition -> {
                        productFeatures?.removeAt(descriptionPosition)
                        productFeatures?.add(actualDescriptionPosition, null)

                        notifyItemChanged(oldPosition)
                        notifyItemChanged(selectedPosition)
                        notifyItemChanged(descriptionPosition)
                        descriptionPosition = actualDescriptionPosition
                    }
                    else -> {
                        productFeatures?.removeAt(descriptionPosition)
                        productFeatures?.add(actualDescriptionPosition, null)

                        selectedPosition = if (actualDescriptionPosition > descriptionPosition)
                            selectedPosition - 1
                        else
                            selectedPosition

                        notifyItemChanged(oldPosition)
                        notifyItemMoved(descriptionPosition, actualDescriptionPosition)
                        notifyItemChanged(actualDescriptionPosition)
                        notifyItemChanged(selectedPosition)
                        descriptionPosition = actualDescriptionPosition
                    }
                }
            }
        }

        return holder
    }

    override fun getItemCount(): Int {
        return productFeatures?.size ?: 0
    }

    private fun getProductionDescriptionPosition(index: Int): Int {
        val rowId = index / ProductDetailActivity.gridSpanCount
        val newIndex = (rowId + 1) * ProductDetailActivity.gridSpanCount
        return if (newIndex > baseProductFeatures?.size ?: 0) baseProductFeatures?.size ?: newIndex else newIndex
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductFeatureView) {
            holder.bindView(productFeatures?.get(position), holder.adapterPosition == selectedPosition)
        } else if (holder is ProductFeatureDescriptionView) {
            if (selectedPosition != -1) {
                val actualPosition = baseProductFeatures?.indexOf(productFeatures?.get(selectedPosition)) ?: 0
                if (actualPosition != -1) {
                    holder.bindView(baseProductFeatures?.get(actualPosition))
                }
            } else {
                holder.bindView(null)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (productFeatures == null || productFeatures.isEmpty()) {
            return 0
        }
        if (productFeatures[position] == null) {
            return R.layout.layout_product_description
        }
        return R.layout.item_product_feature
    }

    private fun unSelectAll() {
        val oldPosition = selectedPosition
        selectedPosition = -1
        notifyItemChanged(oldPosition)

        productFeatures?.removeAt(descriptionPosition)
        notifyItemRemoved(descriptionPosition)
        descriptionPosition = -1
    }
}

class ProductFeatureView(view: View) : RecyclerView.ViewHolder(view) {

    fun bindView(productFeature: ProductFeature?, isSelected: Boolean) {
        itemView?.line?.visibility = if (adapterPosition % ProductDetailActivity.gridSpanCount == 0) {
            View.GONE
        } else {
            View.VISIBLE
        }

        itemView?.tv_product_feature?.text = productFeature?.name
        itemView?.isSelected = isSelected
        val textColor = ContextCompat.getColor(itemView.context
                , if (isSelected) R.color.colorAccent else R.color.colorPurple)
        itemView?.tv_product_feature?.setTextColor(textColor)
    }
}


class ProductFeatureDescriptionView(view: View) : RecyclerView.ViewHolder(view) {

    fun bindView(productFeature: ProductFeature?) {
        itemView?.tv_dscription?.text = productFeature?.description
        itemView?.iv_cancel?.isEnabled = true
    }
}