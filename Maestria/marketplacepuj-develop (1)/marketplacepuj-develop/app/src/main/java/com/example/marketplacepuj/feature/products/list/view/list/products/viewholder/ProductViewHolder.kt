package com.example.marketplacepuj.feature.products.list.view.list.products.viewholder

import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.example.marketplacepuj.R
import com.example.marketplacepuj.core.utils.PriceFormat
import com.example.marketplacepuj.databinding.ProductItemLayoutBinding
import com.example.marketplacepuj.feature.products.list.view.list.products.entities.Product
import com.example.marketplacepuj.feature.products.list.view.list.products.viewmodels.ProductViewModel


class ProductViewHolder(private val binding: ProductItemLayoutBinding) :
    AbstractViewHolder<ProductViewModel>(binding),
    View.OnClickListener {

    companion object {
        val LAYOUT: Int = R.layout.product_item_layout
    }

    private lateinit var product: Product

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        listener?.onClickProduct(product)
    }

    override fun bind(viewModel: ProductViewModel) {
        product = viewModel.product
        Glide.with(binding.root.context).load(product.imageUrl).into(binding.ivImage)
        binding.tvPrice.text = PriceFormat.apply(product.price)
        binding.tvProductName.text = product.name
        binding.tvDescription.text = product.description
        val sizes = product.availableSizes.keys.map { it + "\t\t" }.sorted()
        if (sizes.isNotEmpty()) {
            binding.tvSize.text = TextUtils.join("", sizes)
            binding.tvSize.visibility = View.VISIBLE
        } else {
            binding.tvSize.visibility = View.GONE
        }
    }
}
