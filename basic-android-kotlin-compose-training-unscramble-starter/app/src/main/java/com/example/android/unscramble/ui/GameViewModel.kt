package com.example.android.unscramble.ui

import androidx.compose.runtime.ScopeUpdateScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.android.unscramble.data.MAX_NO_OF_WORDS
import com.example.android.unscramble.data.SCORE_INCREASE
import com.example.android.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.CyclicBarrier

class GameViewModel : ViewModel(){

    // composeの監視対象にする。
    var userGuess by mutableStateOf("")
        private set

    // Game UI state GameViewModel内のバッキングフィールド
    private val _uiState = MutableStateFlow(GameUiState())

    //asStateFlow()で読み取り専用になる。
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    //　lateinit　遅延初期化 呼ばれたときに初めて初期化される。
    private lateinit var currentWord: String

    //そのゲームで使用済みの単語を格納するための可変のSet(重複した要素は最初の要素しか格納できない。エラーにはならない)
    private var usedWords: MutableSet<String> = mutableSetOf()

    //コンストラクター処理
    init{
        resetGame()
    }

    /**
     * mutableStateOfを更新=compose更新
     */
    fun updateUserGuess(guessedWord:String){
        userGuess = guessedWord
    }

    /**
     *入力データをチェックして結果をuiStateに反映
     */
    fun checkUserGuess(){
        // ignoreCase= true大文字小文字を無視して比較する
        if(userGuess.equals(currentWord,ignoreCase = true)){
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        }else{
            //ユーザーの推測が違う場合はエラー
            // MutableStateFlow<T>.update()でMutableStateFlow.value を更新
            // .copy()とすると、一部のプロパティを変更し、他のプロパティはそのままにする
            _uiState.update {currentState->
                currentState.copy(isGuessedWordWrong = true) }
        }
        // ユーザーの推測した単語をクリア
        updateUserGuess("")
    }

    /**
     * 正解した場合にUIの状態を変更する
     */
    private fun updateGameState(updatedScore: Int){
        if(usedWords.size == MAX_NO_OF_WORDS){
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        }else {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentScrambledWord = pickRandomWordAndShuffle(),
                    // .inc() インクリメント
                    currentWordCount = currentState.currentWordCount.inc(),
                    score = updatedScore
                )
            }
        }
    }
    fun skipWord(){
        updateGameState(_uiState.value.score)
        updateUserGuess("")
    }
    fun resetGame(){
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    /**
     * 単語リストの中からランダムに文字を選択し、シャッフルした文字を返します。
     */
    private fun pickRandomWordAndShuffle(): String{
        currentWord = allWords.random()
        if(usedWords.contains(currentWord)){
            return pickRandomWordAndShuffle()
        }else{
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }

    /**
     * シャッフルした文字を返します。
     */
    private fun shuffleCurrentWord(word: String): String {
        var tempWord = word.toCharArray()
        tempWord.shuffle()
        while(String(tempWord) == word){
            tempWord.shuffle()
        }
        return String(tempWord)
    }

}