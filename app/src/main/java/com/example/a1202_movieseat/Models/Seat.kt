package com.example.a1202_movieseat.Models

data class Seat(var status: SeatStatus, var name: String) {

    enum class SeatStatus {
        AVAILABLE,
        SELECTED,
        UNAVAILABLE
    }
}