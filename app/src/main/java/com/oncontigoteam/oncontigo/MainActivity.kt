package com.oncontigoteam.oncontigo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.oncontigoteam.oncontigo.home.doctor.DoctorHome
import com.oncontigoteam.oncontigo.home.patient.PatientHome
import com.oncontigoteam.oncontigo.register.RegisterFragment
import com.oncontigoteam.oncontigo.shared.api.ApiWorker
import com.oncontigoteam.oncontigo.shared.api.user.request.UserLoginRequest
import com.oncontigoteam.oncontigo.shared.api.user.response.UserInformationResponse
import com.oncontigoteam.oncontigo.shared.user.UserWrapperSettings
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.loginButton()
        this.goToRegisterButton()
    }

    //	-------------------------------------------
    //			        Functions
    //	-------------------------------------------
    private fun loginButton(): Unit {
        val loginButton: Button = findViewById(R.id.login_button)
        loginButton.setOnClickListener {
            val email: String       = findViewById<EditText>(R.id.login_email_input).text.toString()
            val password: String    = findViewById<EditText>(R.id.login_password_input).text.toString()

            if (!this.checkLoginInput()) return@setOnClickListener

            this.login(email, password)
        }
    }
    private fun login(email: String, password: String): Unit {
        val loginButton: Button     = findViewById(R.id.login_button)
        val client: OkHttpClient    = ApiWorker.client()
        val originalText: String    = loginButton.text.toString()
        loginButton.text            = "Iniciando..."

        var request: Request = ApiWorker.loginUser(UserLoginRequest(password, email))
        client.newCall(request).enqueue(object: Callback {
            public override fun onFailure(call: Call, e: IOException): Unit {
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Error de conexión", Toast.LENGTH_SHORT
                    ).show()
                }
                loginButton.text = originalText
            }

            public override fun onResponse(call: Call, response: Response): Unit {
                response.body?.let {
                    val responseBody: String = it.string()
                    runOnUiThread {
                        try {
                            val json = JSONObject(responseBody)
                            UserWrapperSettings.userId  = json.getLong("id")
                            UserWrapperSettings.token   = json.getString("token")
                            UserWrapperSettings.email   = email

                            this@MainActivity.fetchUser()
                        }
                        catch (e: Exception) {
                            Toast.makeText(
                                this@MainActivity,
                                "Correo o contraseña incorrectos", Toast.LENGTH_SHORT
                            ).show()
                        }

                        loginButton.text = originalText
                    }
                }
            }
        })
    }
    private fun checkLoginInput(): Boolean {
        val email: String       = findViewById<EditText>(R.id.login_email_input).text.toString()
        val password: String    = findViewById<EditText>(R.id.login_password_input).text.toString()

        //  > Inputs
        if (email.isEmpty() || password.isEmpty()) {
            this.makeToast("Por favor, rellene todos los campos")

            return false
        }

        return true
    }
    private fun fetchUser(): Unit {
        val call: retrofit2.Call<UserInformationResponse> = ApiWorker.userInformation()

        call.enqueue(object: retrofit2.Callback<UserInformationResponse> {
            public override fun onResponse(
                call: retrofit2.Call<UserInformationResponse>,
                response: retrofit2.Response<UserInformationResponse>
            ): Unit {
                //  Unknown: always false
                val isPatient: Boolean = false

                if (response.isSuccessful) {
                    val userInformationResponse: UserInformationResponse? = response.body()
                    userInformationResponse?.let {
                        UserWrapperSettings.name        = it.formatAndName()
                        UserWrapperSettings.dni         = it.dni
                        UserWrapperSettings.phone       = it.phone
                        UserWrapperSettings.imageUrl    = it.photo
                    }

                    this@MainActivity.replaceFragment(
                        if (isPatient) PatientHome() else DoctorHome()
                    )

                    Toast.makeText(
                        this@MainActivity,
                        "Inicio de sesión exitoso", Toast.LENGTH_SHORT
                    ).show()
                }

                this@MainActivity.replaceFragment(
                    if (isPatient) PatientHome() else DoctorHome()
                )
            }

            public override fun onFailure(
                call: retrofit2.Call<UserInformationResponse>,
                t: Throwable
            ): Unit {
                val a: Int = 0
            }
        })
    }

    private fun goToRegisterButton(): Unit {
        val registerButton = findViewById<Button>(R.id.go_to_register_button)
        registerButton.setOnClickListener { this.replaceFragment(RegisterFragment()) }
    }

    private fun replaceFragment(fragment: Fragment): Unit {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
    private fun makeToast(message: String): Unit {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}