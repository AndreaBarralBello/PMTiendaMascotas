package com.example.tiendamascotas;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CatalogoAdaptador extends BaseExpandableListAdapter {

    Map<String, List<Producto>> categoriaProductos;
    List<String> categorias;
    RequestQueue cola;

    public CatalogoAdaptador(Map<String, List<Producto>> categoriaProductos,
                             RequestQueue cola) {
        this.categoriaProductos = categoriaProductos;
        this.categorias = new ArrayList<>(categoriaProductos.keySet());
        this.cola = cola;
    }

    @Override public int getGroupCount() { return categorias.size(); }

    @Override public int getChildrenCount(int g) {
        return categoriaProductos.get(categorias.get(g)).size();
    }

    @Override public Object getGroup(int g) { return categorias.get(g); }

    @Override public Object getChild(int g, int c) {
        return categoriaProductos.get(categorias.get(g)).get(c);
    }

    @Override public long getGroupId(int g) { return g; }
    @Override public long getChildId(int g, int c) { return c; }
    @Override public boolean hasStableIds() { return false; }
    @Override public boolean isChildSelectable(int g, int c) { return true; }

    @Override
    public View getGroupView(int g, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.lista_categorias, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.tvCategorias))
                .setText(categorias.get(g));
        return convertView;
    }

    @Override
    public View getChildView(int g, int c, boolean isLast, View convertView, ViewGroup parent) {
        Producto p = (Producto) getChild(g, c);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.lista_productos, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.tvNombre)).setText(p.getNombre());
        ((TextView) convertView.findViewById(R.id.tvPrecio)).setText(p.getPrecio() + " €");

        ImageView iv = convertView.findViewById(R.id.ivFoto);
        iv.setImageBitmap(null);

        ImageRequest peticion = new ImageRequest(
                p.getFoto(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        iv.setImageBitmap(bitmap);
                    }
                },
                0, 0,
                ImageView.ScaleType.CENTER_CROP,
                null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { }
                }
        );
        cola.add(peticion);

        return convertView;
    }
}