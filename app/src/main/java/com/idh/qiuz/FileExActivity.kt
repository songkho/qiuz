package com.idh.qiuz

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_file_ex.*
import java.io.FileNotFoundException

class FileExActivity : AppCompatActivity() {


    val filename = "data.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_ex)


        savebutton.setOnClickListener {


            val text = textField.text.toString()

            when{


                TextUtils.isEmpty(text) -> {
                    Toast.makeText(applicationContext,"텍스트가 비어있습니다.", Toast.LENGTH_LONG).show()
                }else -> {
                saveToInnerStroage(text , filename)
            }
            }



        }


        loadbutton.setOnClickListener {

            try {

                // textField의 텍스트를 불러온 텍스트로 설정한다.
                textField.setText(loadFromInnerStorage(filename))

            }catch (e: FileNotFoundException){
                //파일이 없는 경우 에러메세지 보여줌

                Toast.makeText(applicationContext,"저장된 텍스트가 없습니다.", Toast.LENGTH_LONG).show()

            }

        }





    }


    //내부저장소파일의 텍스트를 저장한다.
    fun saveToInnerStroage(text: String, filename:String){

        //내부 저장소의 전달된 파일이름의 파일 출력 스트림을 가져온다.

        val fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE)

        // 파일 출력 스트림에 TEXT를 바이트로 변환하여 WRITE 한다.

        fileOutputStream.write(text.toByteArray())

        //파일 출력 스트림을 닫는다.

        fileOutputStream.close()


    }

    //내부 저장소 파일의 텍스트를 불러온다

    fun loadFromInnerStorage(filename : String):String{

        // 내부저장소의 전달된 파일이름의 파일 입력 스트림을 가져온다.

        val fileInputStream = openFileInput(filename)

        // 파일의 저장된 내용을 읽어 String 형태로 불러온다.
        return fileInputStream.reader().readText()

    }


}