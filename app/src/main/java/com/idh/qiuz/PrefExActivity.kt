package com.idh.qiuz

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_pref_ex.*

class PrefExActivity : AppCompatActivity() {


    val nameFiledKey = "nameField"

    val pushCheckBoxKey = "pushCheckBox"

    //SharedPreferences 를 사용하려면 getSharedPreferences()함수를 사용

    val preference by lazy { getSharedPreferences("PrefExActivity", Context.MODE_PRIVATE) }

    //getSharedPreferences(String name, int mode)


    // shared preference 객체는 액티비티 초기화 이후에 사용해야 하기 때문에 lazy 위임을 사용
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pref_ex)





        saveButton.setOnClickListener {

            preference.edit().putString(nameFiledKey, nameField.text.toString()).apply()


            preference.edit().putBoolean(pushCheckBoxKey,pushCheckBox.isChecked).apply()
        }


        loadButton.setOnClickListener {

            nameField.setText(preference.getString(nameFiledKey,""))

            pushCheckBox.isChecked = preference.getBoolean(pushCheckBoxKey,false)

        }



    }





}
