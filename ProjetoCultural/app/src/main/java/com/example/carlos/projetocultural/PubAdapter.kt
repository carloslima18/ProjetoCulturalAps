package com.example.carlos.projetocultural

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_row_main.view.*
// Define o construtor que recebe (carros,onClick)
class PubAdapter(val carros: List<ModelPub>) : RecyclerView.Adapter<PubAdapter.CarrosViewHolder>() {
    // Retorna a quantidade de carros na lista
    override fun getItemCount(): Int {
        return this.carros.size
    }
    // Infla o layout do adapter e retorna o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarrosViewHolder {
        // Infla a view do adapter
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_row_main, parent, false)
        // Retorna o ViewHolder que contém todas as views
        val holder = CarrosViewHolder(view)
        return holder
    }
    // Faz o bind para atualizar o valor das views com os dados do Carro
    override fun onBindViewHolder(holder: CarrosViewHolder, position: Int) {
        // Recupera o objeto carro
        val carro = carros[position]
        val view = holder.itemView
        with(view) {
            // Atualiza os dados do carro
          //  textViewnome.text = carro.nome
            // Faz o download da foto e mostra o ProgressBar
            //img.loadUrl(carro.urlFoto, progress)
            // Adiciona o evento de clique na linha
          //  setOnClickListener { onClick(carro) }
        }
    }
    // ViewHolder fica vazio pois usamos o import do Android Kotlin Extensions
    class CarrosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }
}