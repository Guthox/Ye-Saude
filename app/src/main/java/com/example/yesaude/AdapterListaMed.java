package com.example.yesaude;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterListaMed extends RecyclerView.Adapter<ViewList> {

    Context context;
    List<ListaDeMedicamentos> itens;
    private ViewList.OnItemClickListener listener;

    public AdapterListaMed(Context context, List<ListaDeMedicamentos> itens, ViewList.OnItemClickListener listener) {
        this.context = context;
        this.itens = itens;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewList(LayoutInflater.from(context).inflate(R.layout.item_med, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewList holder, int position) {
        holder.bind(itens.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }
}
