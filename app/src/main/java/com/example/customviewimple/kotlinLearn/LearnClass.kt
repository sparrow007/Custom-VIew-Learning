package com.example.customviewimple.kotlinLearn

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LearnClass(name : String) {
    val your = name.also (::println)
    init {
        Log.e("MY TAG", "WE ARE INIT ${name}")
    }
    val fav = "Second val init and name length = ${name.length}".also (::println)

    init {
        println("This is second block of init and length = ${name.length}")
    }



    fun test() {

        GlobalScope.launch {
            println("a")
            delay(1000)
            println("b")
        }

        print("c")

    }
}