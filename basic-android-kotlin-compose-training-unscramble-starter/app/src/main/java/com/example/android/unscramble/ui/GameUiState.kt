package com.example.android.unscramble.ui

data class GameUiState(
    // 現在クイズ中の単語
    val currentScrambledWord: String = "",
    val currentWordCount:Int=1,
    //　推測された単語が間違っている= true
    val isGuessedWordWrong:Boolean = false,
    val score:Int = 0,
    val isGameOver: Boolean=false
) {

}
