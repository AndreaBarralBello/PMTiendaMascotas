package com.example.tiendamascotas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class CatalogoActivity extends AppCompatActivity {

    ExpandableListView selector;
    LinkedHashMap<String, List<Producto>> categoriaProductos = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catalogo);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        selector = findViewById(R.id.selector);
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                "https://abarral.colexio-karbo.com/2025-27/tiendamascotas/tienda_mascotas.php",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject producto = jsonArray.getJSONObject(i);
                                String nombreCategoria = producto.getString("categoria");

                                if (categoriaProductos.get(nombreCategoria) == null) {
                                    categoriaProductos.put(nombreCategoria, new ArrayList<>());
                                }

                                categoriaProductos.get(nombreCategoria).add(new Producto(
                                        producto.getString("nombre"),
                                        producto.getString("descripcion"),
                                        producto.getString("precio"),
                                        producto.optString("stock", "-"),
                                        "https://abarral.colexio-karbo.com/2025-27/tiendamascotas/imagenes/"
                                                + producto.getString("Aid") + ".jpg"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        CatalogoAdaptador adaptador =
                                new CatalogoAdaptador(categoriaProductos, queue);
                        selector.setAdapter(adaptador);

                        selector.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v,
                                                        int groupPosition, int childPosition, long id) {
                                Producto p = (Producto) adaptador.getChild(groupPosition, childPosition);
                                Intent intent = new Intent(CatalogoActivity.this, FichaActivity.class);
                                intent.putExtra("producto", p);
                                startActivity(intent);
                                return true;
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("TIENDA", "Error: " + volleyError.toString());
                    }
                }
        );

        queue.add(jsonArrayRequest);
    }
}