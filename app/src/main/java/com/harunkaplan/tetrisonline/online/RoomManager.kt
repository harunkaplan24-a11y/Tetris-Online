package com.harunkaplan.tetrisonline.online

import kotlin.random.Random

/**
 * Online match API boundary.
 * Firebase/Firestore implementation can be plugged in without changing the game engine.
 */
class RoomManager {
    fun createRoom(playerId: String): MatchRoom {
        val code = buildString {
            repeat(6) { append("ABCDEFGHJKLMNPQRSTUVWXYZ23456789".random()) }
        }
        return MatchRoom(roomCode = code, hostId = playerId)
    }

    fun joinRoom(room: MatchRoom, playerId: String): MatchRoom {
        require(room.guestId.isEmpty()) { "Oda dolu" }
        return room.copy(guestId = playerId, status = "playing")
    }

    fun winner(room: MatchRoom): String = when {
        room.hostScore > room.guestScore -> room.hostId
        room.guestScore > room.hostScore -> room.guestId
        else -> "draw"
    }
}
