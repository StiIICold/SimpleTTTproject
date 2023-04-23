package com.example.simpletic_tac_toe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.simpletic_tac_toe.databinding.ActivityMainBinding
import android.widget.Toast
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity()
{
    enum class Turn
    {
        NOUGHT,
        CROSS
    }

    private var firstTurn = Turn.CROSS
    private var currentTurn = Turn.CROSS

    private var crossesScore = 0
    private var noughtsScore = 0

    private var boardList = mutableListOf<Button>()

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titletext = findViewById<TextView>(R.id.title_text)
        val pvpButton = findViewById<Button>(R.id.pvp_button)
        val pvcButton = findViewById<Button>(R.id.pvc_button)


        //PVP poga
        pvpButton.setOnClickListener {

            binding.PlayerInputLayout.visibility = View.VISIBLE
            titletext.visibility = View.INVISIBLE
            pvpButton.visibility = View.INVISIBLE
            pvcButton.visibility = View.INVISIBLE
            val playerName1 = findViewById<EditText>(R.id.player1_name_input)
            val playerName2 = findViewById<EditText>(R.id.player2_name_input)
            val submitButton = findViewById<Button>(R.id.submit_button)

            submitButton.setOnClickListener {
                val playerName1 = playerName1.text.toString().trim()
                val playerName2 = playerName2.text.toString().trim()

                if (playerName1.isNotBlank() && playerName2.isNotBlank()) {
                    Toast.makeText(
                        this,
                        "Hello, $playerName1 and $playerName2!",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.boardLayout.visibility = View.VISIBLE
                    binding.player1NameInput.visibility = View.INVISIBLE
                    binding.player2NameInput.visibility = View.INVISIBLE
                    binding.submitButton.visibility = View.INVISIBLE
                    binding.enterText1.visibility = View.INVISIBLE
                    binding.enterText2.visibility = View.INVISIBLE
                    binding.turnTV.visibility = View.VISIBLE
                    initBoard()
                } else {
                    Toast.makeText(this, "Please enter both players' names.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        //PVC poga
        pvcButton.setOnClickListener {
            Toast.makeText(this, "Player vs Computer game mode selected", Toast.LENGTH_SHORT).show()
            binding.PlayerInputLayout.visibility = View.VISIBLE
            binding.player2NameInput.visibility = View.INVISIBLE
            binding.enterText2.visibility = View.INVISIBLE
            binding.submitButton.visibility = View.VISIBLE
            titletext.visibility = View.INVISIBLE
            pvpButton.visibility = View.INVISIBLE
            pvcButton.visibility = View.INVISIBLE
            val playerName1 = findViewById<EditText>(R.id.player1_name_input)
            val submitButton = findViewById<Button>(R.id.submit_button)
            val update_needed = findViewById<TextView>(R.id.feature_not_implemented)

            submitButton.setOnClickListener {
                val playerName1 = playerName1.text.toString().trim()
                if (playerName1.isNotBlank()) {
                    Toast.makeText(
                        this,
                        "Hello, $playerName1!",
                        Toast.LENGTH_SHORT
                    ).show()
                    update_needed.visibility = View.VISIBLE

                }
                //TODO pabeigt pvc versiju...
                //TODO update. Diemžēl neaprēķināju laiku, kurš būs vajadzīgs šim projektam un savas spējas to pilnībā pabeigt
            }
        }
    }


    //izveidot laukumu
    //laukuma izveide un tās pamati tika apgūti apskatot vairākas youtube pamācības un skaidrojumus, koda piemērus
    private fun initBoard()
    {
        boardList.add(binding.a1)
        boardList.add(binding.a2)
        boardList.add(binding.a3)
        boardList.add(binding.b1)
        boardList.add(binding.b2)
        boardList.add(binding.b3)
        boardList.add(binding.c1)
        boardList.add(binding.c2)
        boardList.add(binding.c3)
    }

    //spēles laukuma nospiešanas darbības
    fun boardTapped(view: View)
    {
        if(view !is Button)
            return
        addToBoard(view)

        if(checkForVictory(NOUGHT))
        {
            noughtsScore++
            result("Noughts Win!")
        }
        else if(checkForVictory(CROSS))
        {
            crossesScore++
            result("Crosses Win!")
        }

        if(fullBoard())
        {
            result("Draw")
        }

    }

    //pārbaudīt uzvaras nosacījumus
    private fun checkForVictory(s: String): Boolean
    {
        //Horizontal Victory
        if(match(binding.a1,s) && match(binding.a2,s) && match(binding.a3,s))
            return true
        if(match(binding.b1,s) && match(binding.b2,s) && match(binding.b3,s))
            return true
        if(match(binding.c1,s) && match(binding.c2,s) && match(binding.c3,s))
            return true

        //Vertical Victory
        if(match(binding.a1,s) && match(binding.b1,s) && match(binding.c1,s))
            return true
        if(match(binding.a2,s) && match(binding.b2,s) && match(binding.c2,s))
            return true
        if(match(binding.a3,s) && match(binding.b3,s) && match(binding.c3,s))
            return true

        //Diagonal Victory
        if(match(binding.a1,s) && match(binding.b2,s) && match(binding.c3,s))
            return true
        if(match(binding.a3,s) && match(binding.b2,s) && match(binding.c1,s))
            return true

        return false
    }

    private fun match(button: Button, symbol : String): Boolean = button.text == symbol

    // rezultāta paziņošana
    private fun result(title: String)
    {
        val message = "\nNoughts $noughtsScore\n\nCrosses $crossesScore"
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Reset")
            { _,_ ->
                resetBoard()
            }
            .setCancelable(false)
            .show()
    }

    // pārstatīt spēles laukumu
    private fun resetBoard()
    {
        for(button in boardList)
        {
            button.text = ""
        }

        if(firstTurn == Turn.NOUGHT)
            firstTurn = Turn.CROSS
        else if(firstTurn == Turn.CROSS)
            firstTurn = Turn.NOUGHT

        currentTurn = firstTurn
        setTurnLabel()
    }

    //pārbaude laukuma "pilnumam"
    private fun fullBoard(): Boolean
    {
        for(button in boardList)
        {
            if(button.text == "")
                return false
        }
        return true
    }

    //pievienot simbolus X un O laukumam
    private fun addToBoard(button: Button)
    {
        if(button.text != "")
            return

        if(currentTurn == Turn.NOUGHT)
        {
            button.text = NOUGHT
            currentTurn = Turn.CROSS
        }
        else if(currentTurn == Turn.CROSS)
        {
            button.text = CROSS
            currentTurn = Turn.NOUGHT
        }
        setTurnLabel()
    }

    //mainīt gājienu X un O
    private fun setTurnLabel()
    {
        var turnText = ""
        if(currentTurn == Turn.CROSS)
            turnText = "Turn $CROSS"
        else if(currentTurn == Turn.NOUGHT)
            turnText = "Turn $NOUGHT"

        binding.turnTV.text = turnText
    }

    companion object
    {
        const val NOUGHT = "O"
        const val CROSS = "X"
    }

}