package com.idh.qiuz

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.WindowManager
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_quiz_locker.*
import org.json.JSONArray
import org.json.JSONObject

@Suppress("DEPRECATION")
class QuizLockerActivity : AppCompatActivity() {


    var quiz : JSONObject? = null


    //정답횟수 저장

    val wrongAnswerPref by lazy { getSharedPreferences("wrongAnswer", Context.MODE_PRIVATE) }

    val correctAnswerPref by lazy { getSharedPreferences("correctAnswer", Context.MODE_PRIVATE) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){


            setShowWhenLocked(true)

            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)

        }else{


            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)


        }


        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_quiz_locker)


        val json = assets.open("capital.json").reader().readText()
        val quizArray = JSONArray(json)

        quiz = quizArray.getJSONObject(java.util.Random().nextInt(quizArray.length()))

        quizLabel.text = quiz?.getString("question")
        choice1.text = quiz?.getString("choice1")
        choice2.text = quiz?.getString("choice2")


        //정답 횟수 오답횟수를 보여준다.

        val id = quiz?.getInt("id").toString() ?: ""
        correctCountLabel.text = "정ㄷ바횟수:${correctAnswerPref.getInt(id,0)}"

        wrongCountLabel.text = "오답횟수:${wrongAnswerPref.getInt(id,0)}"



        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                when {
                    progress > 95 -> {
                        leftimageView.setImageResource(R.drawable.padlock)

                        rightimageView.setImageResource(R.drawable.unlock)
                    }


                    progress < 5 -> {
                        leftimageView.setImageResource(R.drawable.unlock)
                        rightimageView.setImageResource(R.drawable.padlock)
                    }


                    else->{

                        leftimageView.setImageResource(R.drawable.padlock)
                        rightimageView.setImageResource(R.drawable.padlock)

                    }

                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

                val progress = seekBar?.progress?:50

                when{

                    progress > 95 -> checkChoice(quiz?.getString("choice2")?:"")
                    progress > 5 -> checkChoice(quiz?.getString("choice1")?:"")

                    else -> seekBar?.progress = 50

                }


            }

        })

    }

    fun checkChoice(choice: String){
        quiz?.let {
            when{
                choice == it.getString("answer") -> finish()
                else -> {
                    leftimageView.setImageResource(R.drawable.padlock)
                    rightimageView.setImageResource(R.drawable.padlock)
                    seekBar?.progress = 50

                    //정답이 아닌경우 진동알림 추가


                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator


                    if (Build.VERSION.SDK_INT >= 26){

                        vibrator.vibrate(VibrationEffect.createOneShot(1000, 100))

                    }else{

                        vibrator.vibrate(1000)
                    }


                }
            }
        }
    }






}
