package com.example.coroutine.exPro.mapper

interface CakeMapper<I,O>{
    fun convert(int:I):O
}