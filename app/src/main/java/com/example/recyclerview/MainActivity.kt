package com.example.recyclerview

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val _arWayang = ArrayList<dcWayang>()
    private var _nama: Array<String> = arrayOf()
    private var _karakter: Array<String> = arrayOf()
    private var _deskripsi: Array<String> = arrayOf()
    private var _gambar: Array<String> = arrayOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        siapkanData()
        tambahData()
        tampilkanData()

        binding.btnToggleLayout.setOnCheckedChangeListener { _, _ ->
            tampilkanData()
        }
    }
    private fun siapkanData() {
        _nama = resources.getStringArray(R.array.namaWayang)
        _karakter = resources.getStringArray(R.array.karakterUtamaWayang)
        _deskripsi = resources.getStringArray(R.array.deskripsiWayang)
        _gambar = resources.getStringArray(R.array.gambarWayang)
    }
    private fun tambahData() {
        _arWayang.clear()
        for (i in _nama.indices) {
            val wayang = dcWayang(
                _gambar[i],
                _nama[i],
                _karakter[i],
                _deskripsi[i]
            )
            _arWayang.add(wayang)
        }
    }
    private fun tampilkanData() {
        if (binding.btnToggleLayout.isChecked) {
            binding.rvWayang.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvWayang.layoutManager = LinearLayoutManager(this)
        }

        val adapterWayang = AdapterRecView(_arWayang)
        adapterWayang.setOnItemClickCallback(object : AdapterRecView.OnItemClickCallback {
            override fun onItemClicked(data: dcWayang) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                    putExtra("nama", data.nama)
                    putExtra("karakter", data.karakter)
                    putExtra("deskripsi", data.deskripsi)
                    putExtra("foto", data.foto)
                }
                startActivity(intent)
            }

            override fun delData(pos: Int) {
                val item = _arWayang[pos]
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Konfirmasi HAPUS DATA")
                    .setMessage("Apakah Benar Data ${item.nama} akan dihapus?")
                    .setPositiveButton("Hapus") { _, _ ->
                        _arWayang.removeAt(pos)
                        adapterWayang.notifyItemRemoved(pos)
                        adapterWayang.notifyItemRangeChanged(pos, _arWayang.size - pos)
                        Toast.makeText(
                            this@MainActivity,
                            "${item.nama} berhasil dihapus",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        })

        binding.rvWayang.adapter = adapterWayang

        binding.rvWayang.itemAnimator = DefaultItemAnimator().apply {
            removeDuration = 400L
        }
    }
}