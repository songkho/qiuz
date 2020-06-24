package com.idh.qiuz

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.MultiSelectListPreference
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {


    val fragment = PrefFragmentActivity.MyPreFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager.beginTransaction().replace(R.id.preferenceContent, fragment).commit()
    }



    class MyPreferenceFragment : PreferenceFragment(){
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            //환경설정 리로스 파일 적용

            addPreferencesFromResource(R.xml.pref)

            // 퀴즈 종류 요약정보에, 현재 선택된 항목을 보여주는 코드


            val categoryPref = findPreference("category") as MultiSelectListPreference
            categoryPref.summary = categoryPref.values.joinToString(",")

            // 환경설정 정보값이 변경될때에도 요약정보를 변경하도록 리스너 등록

            categoryPref.setOnPreferenceChangeListener { preference, newValue ->

                val newValueSet = newValue as? HashSet<*>
                    ?: return@setOnPreferenceChangeListener true


                //선택된 퀴즈종류로 요약정보 보여줌

                categoryPref.summary = newValue.joinToString(",")
                true

            }


            val useLockScreenPref = findPreference("useLockScreen") as SwitchPreference

            //클릭되었을때 이벤트 리스너 코드 작성

            useLockScreenPref.setOnPreferenceClickListener {


                when {
                    useLockScreenPref.isChecked -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)run {
                            activity.startForegroundService(
                                Intent(
                                    activity,
                                    LockScreenService::class.java))
                        }else{
                            activity.startService(Intent(activity, LockScreenService::class.java))
                        }
                    }

                    else -> activity.stopService(Intent(activity, LockScreenService::class.java))
                }

              true

            }

            if (useLockScreenPref.isChecked){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    activity.startForegroundService(Intent(activity, LockScreenService::class.java))
                }else{
                    activity.startService(Intent(activity, LockScreenService::class.java))
                }
            }


        }
    }

}
