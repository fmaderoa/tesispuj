package com.example.marketplacepuj.ui.producto

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.marketplacepuj.R
import com.example.marketplacepuj.ui.producto.data.entities.Producto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class DetalleProductoActivity : AppCompatActivity() {

    private lateinit var producto: Producto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_producto)

        // Obtener ID del producto
        val productoId = intent.getStringExtra("productoId")

        // Obtener referencia a la tabla "productos"
        val database = FirebaseDatabase.getInstance()
        val productosRef = database.getReference("productos")

        // Leer datos del producto
        productosRef.child(productoId!!).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                producto = (dataSnapshot.getValue<Producto>() as Producto?)!!

                // Mostrar información del producto
                val imageView = findViewById<ImageView>(R.id.ivProducto)
                Glide.with(applicationContext).load(producto.url_imagen).into(imageView)

                val textViewTitulo = findViewById<TextView>(R.id.textViewTitulo)
                textViewTitulo.text = producto.nombre

                val textViewDescripcion = findViewById<TextView>(R.id.textViewDescripcion)
                textViewDescripcion.text = producto.descripcion

                val textViewPrecio = findViewById<TextView>(R.id.textViewPrecio)
                textViewPrecio.text = "$${producto.precio}"

                // TODO: Implementar lógica para botón de compra
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar el error
            }
        })
    }
}
