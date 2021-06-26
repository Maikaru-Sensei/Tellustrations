package at.fhooe.tellustrations.models

class Game (val players: Int, val cards: ArrayList<Card>?) {

    fun getCards(): List<Card>? {
        return cards
    }
}