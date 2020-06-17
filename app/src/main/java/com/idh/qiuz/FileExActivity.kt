package com.idh.qiuz

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_file_ex.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.jar.Manifest

class FileExActivity : AppCompatActivity() {


    val filename = "data.txt"


    var granted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_ex)

        //외부저장소의 권한을 동적으로 체크하는 함수를 호출

        checkPermission()


        savebutton.setOnClickListener {

            //20200616f

            val text = textField.text.toString()

            when {


                TextUtils.isEmpty(text) -> {
                    Toast.makeText(applicationContext, "텍스트가 비어있습니다.", Toast.LENGTH_LONG).show()
                }

                !isExternalStorageWritable() -> {
                    Toast.makeText(applicationContext, "외부 저장장치가 없습니다.", Toast.LENGTH_LONG).show()
                }

                else -> {
                    saveToInnerStroage(text, filename)
                }
            }


        }


        loadbutton.setOnClickListener {

            try {

                // textField의 텍스트를 불러온 텍스트로 설정한다.
                textField.setText(loadFromInnerStorage(filename))

            } catch (e: FileNotFoundException) {
                //파일이 없는 경우 에러메세지 보여줌

                Toast.makeText(applicationContext, "저장된 텍스트가 없습니다.", Toast.LENGTH_LONG).show()

            }

        }


    }


    //내부저장소파일의 텍스트를 저장한다.
    fun saveToInnerStroage(text: String, filename: String) {

        //내부 저장소의 전달된 파일이름의 파일 출력 스트림을 가져온다.

        val fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE)

        // 파일 출력 스트림에 TEXT를 바이트로 변환하여 WRITE 한다.

        fileOutputStream.write(text.toByteArray())

        //파일 출력 스트림을 닫는다.

        fileOutputStream.close()


    }

    //내부 저장소 파일의 텍스트를 불러온다

    fun loadFromInnerStorage(filename: String): String {

        // 내부저장소의 전달된 파일이름의 파일 입력 스트림을 가져온다.

        val fileInputStream = openFileInput(filename)

        // 파일의 저장된 내용을 읽어 String 형태로 불러온다.
        return fileInputStream.reader().readText()

    }


    fun isExternalStorageWritable(): Boolean {


        //외부저장장치가 현재 사용가능 확인하는 함수 외부저장장치느 탈착이 가능한 겨우가 있다.

        when {

            Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED -> return true
            else -> return false

        }

    }


    fun getAppDataFileFromExternalStorage(filename: String): File {


        val dir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

        } else {
            File(Environment.getExternalStorageDirectory().absolutePath + "/Documents")
        }

        dir?.mkdirs()
        return File("${dir.absolutePath}${File.separator}${filename}")


    }


    fun saveToExternalStrage(text: String, filename: String) {

        val fileOutputStream = FileOutputStream(getAppDataFileFromExternalStorage(filename))
        fileOutputStream.write(text.toByteArray())
        fileOutputStream.close()


    }

    fun loadFromExternalStorage(filename: String): String {
        return FileInputStream(getAppDataFileFromExternalStorage(filename)).reader().readText()
    }


    val MY_PERMISSION_REQUEST = 999
    fun checkPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this@FileExActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        when {
            permissionCheck != PackageManager.PERMISSION_GRANTED -> {

                ActivityCompat.requestPermissions(

                    this@FileExActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSION_REQUEST


                )

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {


        when (requestCode) {
            MY_PERMISSION_REQUEST -> {
                when {
                    grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                        granted = true
                    }
                    else -> {
                        granted = false
                    }
                }
            }
        }


    }


    fun saveToExternalCustomDirectory(text: String, filepath: String = "/sdcard/data.txt"){
        when{


            granted -> {

                val fileOutputStream = FileOutputStream(File(filepath))

                fileOutputStream.write(text.toByteArray())
                fileOutputStream.close()

            }else->{
            Toast.makeText(applicationContext,"권한이 없습니다.", Toast.LENGTH_LONG).show()
        }

        }
    }

    //임의으 경로에 파일에서 데이터를 읽는 함수

    fun loadFromExternalCustomDirectory(filepath: String = "/sdcard/data.txt"): String{

        when{

            //권한이 있는 경우

            granted ->{
                return FileInputStream(File(filepath)).reader().readText()
            }

            else->{
                Toast.makeText(applicationContext,"권한이 없습니다.", Toast.LENGTH_LONG).show()
                return ""
            }

        }

    }



}
