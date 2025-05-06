package com.fikrathhassan.shoppingcart.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T: ViewBinding>(
    val bindingFactory: (LayoutInflater) -> T
): AppCompatActivity() {

    private var _binding: T? = null
    protected val binding: T
        get() = _binding as T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = bindingFactory(layoutInflater)
        setContentView(requireNotNull(_binding).root)

        initData()
    }

    abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

}