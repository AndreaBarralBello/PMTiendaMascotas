package com.example.tiendamascotas;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class FichaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha);

        Producto p = (Producto) getIntent().getSerializableExtra("producto");
        if (p == null) { finish(); return; }

        setTitle(p.getNombre());
        ((TextView) findViewById(R.id.tvNombre)).setText(p.getNombre());
        ((TextView) findViewById(R.id.tvPrecio)).setText(p.getPrecio() + " €");
        ((TextView) findViewById(R.id.tvStock)).setText("Stock: " + p.getStock() + " unidades");
        ((TextView) findViewById(R.id.tvDescripcion)).setText(p.getDescripcion());

        ImageView foto = findViewById(R.id.ivFotoGrande);
        ImageRequest peticion = new ImageRequest(
                p.getFoto(),
                bitmap -> foto.setImageBitmap(bitmap),
                0, 0,
                ImageView.ScaleType.FIT_CENTER,
                null,
                error -> { }
        );
        Volley.newRequestQueue(this).add(peticion);
        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());
    }
}