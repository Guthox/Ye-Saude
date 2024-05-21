package com.example.yesaude;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import java.util.List;

public class AdapterListaMed extends RecyclerView.Adapter<ViewList> {

    Context context;
    List<ListaDeMedicamentos> itens;

    public AdapterListaMed(Context context, List<ListaDeMedicamentos> itens) {
        this.context = context;
        this.itens = itens;
    }

    @NonNull
    @Override
    public ViewList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewList(LayoutInflater.from(context).inflate(R.layout.item_med, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewList holder, int position) {
        holder.med.setText(itens.get(position).getMeds());
        holder.horario.setText(itens.get(position).getHorario());
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }
}
