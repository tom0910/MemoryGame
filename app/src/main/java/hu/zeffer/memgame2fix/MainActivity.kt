package hu.zeffer.memgame2fix

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableStartButton()
        initMsgs()
        disableButton(findViewById(R.id.RestartBut))
    }

    val tileArray = listOf(R.id.A0,R.id.A1,R.id.A2,R.id.A3,R.id.A4,R.id.B0,R.id.B1,R.id.B2,R.id.B3,R.id.B4,R.id.C0,R.id.C1,R.id.C2,R.id.C3,R.id.C4,R.id.D0,R.id.D1,R.id.D2,R.id.D3,R.id.D4,R.id.E0,R.id.E1,R.id.E2,R.id.E3,R.id.E4)
    var tiles =ArrayList<Button>()
    var playedTilesIds=ArrayList<Int>()
    val randomizeBomb = Random()
    var good: Int = 0
    var bad: Int = 0
    var numTiles: Int = 0
    var totalEarnedPoints:Int = 0
    var gameNumber:Int = 0
    var gameLevel:Long = 500

    public fun NewGameClicked(view: View) {
        val startButton = view as Button
        disableButton(startButton)
        enableButton(findViewById(R.id.RestartBut))
        newGame()
    }
    public fun TileCicked(view:View){
        val buttonSelected: Button = view as Button
        markSteps(buttonSelected)
    }
    public fun RestartEntireGameClicked(view:View){
        val buttonSelected: Button = view as Button
        initRestart(buttonSelected)
    }

    public fun GameLevOneClicked (view:View){
        val buttonSelected: Button = view as Button
        gameLevel(1)
    }
    public fun GameLevTwoClicked (view:View){
        val buttonSelected: Button = view as Button
        gameLevel(2)
    }
    public fun GameLevThreeClicked (view:View){
        val buttonSelected: Button = view as Button
        gameLevel(3)
    }

    fun gameLevel(level:Int) {
        when(level) {
            1-> gameLevel =  500
            2-> gameLevel = 150
            3-> gameLevel = 30
            else -> {
                gameLevel  = 500
            }
        }
    }

    fun newGame(){
        val handler = Handler()
        gameNumber++
        delMsgTotal()
        initButtonsColor()
        initVars()
        initMsgs()
        genRandTiles()
        showTilesToBeFound()
        numTiles = tiles.size
        handler.postDelayed({ clearFields() }, gameLevel)
        disableButsAtStart()
    }

    fun initButtonsColor() {
        for(butId:Int in tileArray) {
            var but : Button = findViewById(butId)
            but.setBackgroundColor(Color.BLUE)
            but.setText("")
        }
    }
    fun initVars() {
        good = 0
        bad = 0
        numTiles = 0
        tiles = arrayListOf()
    }
    fun initMsgs() {
        msgTop()
    }
    fun initRestart(restartGameButton: Button) {
        disableButton(restartGameButton)
        disableTiles()
        totalEarnedPoints =0;
        gameNumber =1;
        delMsgResult()
        initButtonsColor()
        initVars()
        initMsgs()
        delMsgTotal()
        enableStartButton()
        enableButsAtStart()
    }
    fun msgTop() {
        val topMsg: TextView = findViewById(R.id.msgTop)
        val strToSet: String = "GAME: $gameNumber"
        topMsg.setText(strToSet)
    }
    fun genRandTiles(){
        val indexes=ArrayList<Int>()
        val rnd = Random()
        do{
            val r:Int = rnd.nextInt(25)
            if ( !indexes.contains(r)) {
                indexes.add(r)
            }
        }while(indexes.size != 5)

        for(i in 0..indexes.size-1) {
            val but : Button = findViewById(tileArray.get(indexes[i]));
            tiles.add(but);
        }
    }
    fun showTilesToBeFound(){
        for( b: Button in tiles) {
            b.setBackgroundColor(Color.GREEN)
        }
    }
    fun clearFields() {
        for(but in tiles) {
            but.setBackgroundColor(Color.BLUE)
        }
        enableTiles()
    }
    fun markSteps(buttonStepped: Button) {
        disableButton(buttonStepped)
        playedTilesIds.add(buttonStepped.id)
        if (tiles.contains(buttonStepped)) {
            buttonStepped.setBackgroundColor(Color.GREEN)
            buttonStepped.setTextColor(Color.WHITE)
            buttonStepped.text = "\u2705"
            good++;
        } else {
            buttonStepped.setBackgroundColor(Color.RED)
            buttonStepped.setTextColor(Color.WHITE)
            buttonStepped.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            buttonStepped.text = "\u2639"
            bad++
        }
        msgResults(good, numTiles,bad)
        if(good.equals(tiles.size)) {
            endOfGame()
        }
    }
    fun endOfGame() {
        val points: Int = good-bad
        totalEarnedPoints += points
        msgEndOfGame(points)
        msgTotal(totalEarnedPoints,gameNumber)
        enableStartButton()
        enableButsAtStart()
        disableTiles()
    }
    fun msgEndOfGame(points: Int) {
        val msg: TextView = findViewById(R.id.messageRes)
        val strToSet : String = "You earned $points points. Press New Game to continue"
        msg.setText(strToSet)
    }
    fun delEndofGame() {
        val msg: TextView = findViewById(R.id.messageRes)
        msg.setText("")
    }
    fun msgResults(good: Int, numTiles:Int, bad: Int) {
        val msg: TextView = findViewById(R.id.messageRes)
        val strToSet : String = "You found $good / $numTiles. Missed total of $bad"
        msg.setText(strToSet)
    }
    fun delMsgResult() {
        val msg: TextView = findViewById(R.id.messageRes)
        msg.setText("")
    }
    fun msgTotal(totalEarned: Int, numGames:Int) {
        val msg: TextView = findViewById(R.id.msgTotal)
        val strSetText: String = "TOTAL: $totalEarnedPoints out of $gameNumber games"
        msg.setText(strSetText)
    }
    fun delMsgTotal() {
        val msg: TextView = findViewById(R.id.msgTotal)
        msg.setText("")
    }
    private fun enableStartButton() {
        val startButton: Button = findViewById(R.id.startGame);
        enableButton(startButton)
        startButton.setText("New Game")
    }
    private fun enableButton(but: Button) {
        but.isEnabled=true
    }
    private fun disableButton(but: Button) {
        but.isEnabled=false
    }

    fun disableButsAtStart() {
        disableButton(findViewById(R.id.lev1))
        disableButton(findViewById(R.id.lev2))
        disableButton(findViewById(R.id.lev3))
    }
    fun enableButsAtStart() {
        enableButton(findViewById(R.id.lev1))
        enableButton(findViewById(R.id.lev2))
        enableButton(findViewById(R.id.lev3))
    }
    fun disableTiles() {
        for(butId:Int in tileArray) {
            var but : Button = findViewById(butId)
            disableButton(but)
        }
    }
    fun enableTiles() {
        for(butId:Int in tileArray) {
            var but : Button = findViewById(butId)
            enableButton(but)
        }
    }
}
