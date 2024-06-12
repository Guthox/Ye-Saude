package com.example.yesaude;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Map<String, List<String>> childColecao;
    private List<String> groupList;
    private ExpandableListView expandableListView;
    private  Activity atividade;
    public MyExpandableListAdapter(Context context, List<String> groupList,
                                   Map<String, List<String>> childColecao, ExpandableListView expandableListView, Activity atividade){
        this.context = context;
        this.childColecao = childColecao;
        this.groupList = groupList;
        this.expandableListView = expandableListView;
        this.atividade = atividade;
    }

    @Override
    public int getGroupCount() {
        return childColecao.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childColecao.get(groupList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childColecao.get(groupList.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String mobileName = getGroup(i).toString();
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.groupitem_imc, null);
        }

        TextView item = view.findViewById(R.id.mobile);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(mobileName);

        ImageView delete = view.findViewById(R.id.deleteGroup);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Quer deletar esse item?");
                builder.setCancelable(true);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        if (atividade.getClass() == IMC_EL.class){
                            BancoIMC imc = new BancoIMC(v.getContext());
                            imc.remover(Info.getIdEscolhido());
                        }
                        else if(atividade.getClass() == TelaPressao.class){
                            BancoPressao bd = new BancoPressao(v.getContext());
                            bd.remover(Info.getIdEscolhido());
                        }
                        else if (atividade.getClass() == TelaGlicose.class){
                            BancoGlicose bd = new BancoGlicose(v.getContext());
                            bd.remover(Info.getIdEscolhido());
                        }
                        else if (atividade.getClass() == TelaConsulta.class){
                            BancoConsultas bd = new BancoConsultas(v.getContext());
                            bd.remover(Info.getIdEscolhido());
                        }
                        else if (atividade.getClass() == TelaExames.class){
                            BancoExames bd = new BancoExames(v.getContext());
                            bd.remover(Info.getIdEscolhido());
                        }
                        notifyDataSetChanged();
                        expandableListView.collapseGroup(i);
                        atividade.recreate();
                    }
                });
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(getChild(i, 0).toString().substring(4)); // id da medida
                Info.setIdEscolhido(id);
                if (expandableListView.isGroupExpanded(i)){
                    expandableListView.collapseGroup(i);
                    delete.setVisibility(View.INVISIBLE);
                }
                else{
                    expandableListView.expandGroup(i);
                    delete.setVisibility(View.VISIBLE);
                }

            }
        });
        return view;
    }
    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        String model = getChild(i, i1).toString();
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.childitem_imc, null);
        }

        TextView item = view.findViewById(R.id.model);
        item.setText(model);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(atividade.getClass() == TelaExames.class && i1 == 3){
                        BancoExames bd = new BancoExames(v.getContext());
                        byte[] exame = bd.pegarExame(Info.getUsername(), Info.getIdEscolhido());
                        Info.setBytes(exame);
                        Intent intent = new Intent(v.getContext(), ImagemZoom.class);
                        v.getContext().startActivity(intent);
                }
                else if (atividade.getClass() == TelaConsulta.class && i1 == 3){
                    BancoConsultas bd = new BancoConsultas(v.getContext());
                    String exame = bd.pegarExame(Info.getUsername(), Info.getIdEscolhido());
                    if (!exame.isEmpty()){
                        Intent intent = new Intent(v.getContext(), ImagemZoom.class);
                        v.getContext().startActivity(intent);
                    }
                }
                else if (atividade.getClass() == TelaConsulta.class && i1 != 3){
                    Info.setEditarConsulta(true);
                    Intent intent = new Intent(v.getContext(), TelaConsultasRealizadas.class);
                    v.getContext().startActivity(intent);
                }

            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}