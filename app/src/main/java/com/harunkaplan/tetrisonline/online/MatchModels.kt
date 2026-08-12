package com.harunkaplan.tetrisonline.online

data class MatchRoom(
    val roomCode: String = "",
    val hostId: String = "",
    val guestId: String = "",
    val status: String = "waiting",
    val hostScore: Int = 0,
    val guestScore: Int = 0,
    val winnerId: String = ""
)

data class PlayerState(
    val playerId: String = "",
    val playerName: String = "Oyuncu",
    val score: Int = 0,
    val lines: Int = 0,
    val level: Int = 1,
    val alive: Boolean = true
)
