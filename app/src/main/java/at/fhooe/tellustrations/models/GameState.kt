package at.fhooe.tellustrations.models

object GameState {
    private lateinit var game: Game

    fun setGame(game: Game) {
        this.game = game
    }

    fun getGame() : Game? {
        if (this::game.isInitialized) {
            return game
        }

        return null
    }

}