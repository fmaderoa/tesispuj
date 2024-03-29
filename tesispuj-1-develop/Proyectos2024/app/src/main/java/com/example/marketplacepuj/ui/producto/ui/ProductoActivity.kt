package com.example.marketplacepuj.ui.producto.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplacepuj.R
import com.example.marketplacepuj.ui.producto.data.entities.Producto
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class ProductoActivity : AppCompatActivity() {

    private lateinit var productosAdapter: ProductosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)

        // Obtener referencia a la tabla "productos"
        FirebaseApp.initializeApp(applicationContext)
        val database = FirebaseDatabase.getInstance()
        val productosRef = database.getReference("productos")

        // Configurar RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productosAdapter = ProductosAdapter(this)
        recyclerView.adapter = productosAdapter

        // Leer datos de la tabla "productos"
        productosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Lista para almacenar los productos
                val productos = mutableListOf<Producto>()

                // Recorrer los datos y agregarlos a la lista
                for (productoSnapshot in dataSnapshot.children) {
                    val producto = productoSnapshot.getValue<Producto>()
                    productos.add(producto!!)
                }

                // Actualizar el adaptador con la lista de productos
                productosAdapter.productos = productos
                productosAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar el error
            }
        })
    }
}
