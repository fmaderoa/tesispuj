package com.example.marketplacepuj.ui.producto.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marketplacepuj.R
import com.example.marketplacepuj.ui.producto.DetalleProductoActivity
import com.example.marketplacepuj.ui.producto.data.entities.Producto

class ProductosAdapter(private val context: Context) : RecyclerView.Adapter<ProductosAdapter.ViewHolder>() {

    var productos: List<Producto> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = productos[position]

        // Mostrar imagen del producto
        val imageView = holder.itemView.findViewById<ImageView>(R.id.ivProducto)
        Glide.with(context).load(producto.url_imagen).into(imageView)

        // Mostrar título del producto
        val textViewTitulo = holder.itemView.findViewById<TextView>(R.id.textViewTitulo)
        textViewTitulo.text = producto.nombre

        // Mostrar precio del producto
        val textViewPrecio = holder.itemView.findViewById<TextView>(R.id.textViewPrecio)
        textViewPrecio.text = "$${producto.precio}"

        // OnClickListener para abrir la actividad con más información del producto
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetalleProductoActivity::class.java)
            intent.putExtra("productoId", producto.idProducto)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = productos.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
