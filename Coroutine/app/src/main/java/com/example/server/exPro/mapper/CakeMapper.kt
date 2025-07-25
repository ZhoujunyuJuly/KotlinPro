package com.example.server.exPro.mapper

interface CakeMapper<I,O>{
    fun convert(int:I):O
}