package     com.oncontigoteam.oncontigo.register

import      android.os.Bundle
import      android.view.LayoutInflater
import      android.view.View
import      android.view.ViewGroup
import      android.widget.Button
import      android.widget.EditText
import      android.widget.ImageButton
import      android.widget.RadioButton
import      android.widget.RadioGroup
import      android.widget.Toast
import      androidx.fragment.app.Fragment
import      com.oncontigoteam.oncontigo.R
import      com.oncontigoteam.oncontigo.shared.api.ApiWorker
import      com.oncontigoteam.oncontigo.shared.api.user.request.UserRegisterProfileRequest
import      com.oncontigoteam.oncontigo.shared.api.user.request.UserRegisterRequest
import      com.oncontigoteam.oncontigo.shared.tools.Functions
import      com.oncontigoteam.oncontigo.shared.user.UserConstraints
import      com.oncontigoteam.oncontigo.shared.user.UserWrapperSettings
import      okhttp3.Call
import      okhttp3.Callback
import      okhttp3.OkHttpClient
import      okhttp3.Request
import      okhttp3.Response
import      okio.IOException
import      org.json.JSONObject

/**
 * A simple [androidx.fragment.app.Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
public final class RegisterFragment : Fragment() {
//	|-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-|
//			        Functions and Methods
//	|-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-|

    //	-------------------------------------------
    //			    Loading Functions
    //	-------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.registerButton(view)
        this.changeToMain(view)
    }

    //	-------------------------------------------
    //			        Functions
    //	-------------------------------------------
    private fun registerButton(view: View): Unit {
        val registerButton: Button = view.findViewById(R.id.register_button)
        registerButton.setOnClickListener {
            val company: String     = view.findViewById<EditText>(R.id.user_name_input).text.toString()
            val email: String       = view.findViewById<EditText>(R.id.email_input_register).text.toString()
            val password: String    = view.findViewById<EditText>(R.id.password_register_input).text.toString()
            val phoneNumber: String = view.findViewById<EditText>(R.id.phone_register_input).text.toString()
            val age: String         = view.findViewById<EditText>(R.id.age_register_input).text.toString()
            val dni: String         = view.findViewById<EditText>(R.id.dni_register_input).text.toString()
            val opt: RadioGroup     = view.findViewById<RadioGroup>(R.id.opt_register)

            if (!this.checkRegisterInputs(view)) return@setOnClickListener

            val optSelected: String = view.findViewById<RadioButton>(opt.checkedRadioButtonId).text.toString()
            this.registerUser(
                view, company, email, phoneNumber, password, age, dni,
                when (opt.checkedRadioButtonId) {
                    R.id.opt_patient -> "ROLE_PATIENT"
                    R.id.opt_doctor -> "ROLE_DOCTOR"

                    else -> "0"
                }
            )
        }
    }
    private fun registerUser(
        view: View,
        name: String, email: String, phoneNumber: String, password: String,
        age: String, dni: String, opt: String
    ): Unit {
        val registerButton: Button  = view.findViewById(R.id.register_button)
        val request: Request        = ApiWorker.registerUser(
            UserRegisterRequest(email, password, listOf("ROLE_USER", opt))
        )
        val client: OkHttpClient    = ApiWorker.client()

        val originalText: String    = registerButton.text.toString()
        //registerButton.text         = "Registrando..."

        client.newCall(request).enqueue(object: Callback {
            public override fun onFailure(call: Call, e: IOException): Unit {
                this@RegisterFragment.requireActivity().runOnUiThread {
                    Toast.makeText(
                        this@RegisterFragment.requireActivity(),
                        "Error de conexión", Toast.LENGTH_SHORT
                    ).show()
                }
                //registerButton.text = originalText
            }

            public override fun onResponse(call: Call, response: Response): Unit {
                response.body?.let {
                    val json = JSONObject(it.string())
                    UserWrapperSettings.userId = json.getLong("id")
                    //UserWrapperSettings.token  = json.getString("token")

                    this@RegisterFragment.requireActivity().runOnUiThread {
                        this@RegisterFragment.changeToMain(view)

                        Toast.makeText(
                            this@RegisterFragment.requireActivity(),
                            "Usuario registrado", Toast.LENGTH_SHORT
                        ).show()

                        this@RegisterFragment.registerUserProfile(view, name, phoneNumber, dni)
                    }
                }
                //registerButton.text = originalText
            }
        })
    }
    private fun registerUserProfile(
        view: View, name: String, phoneNumber: String, dni: String
    ): Unit {
        val registerButton: Button  = view.findViewById(R.id.register_button)
        val request: Request        = ApiWorker.registerUserProfile(
            UserRegisterProfileRequest(
                name, "", "Lima", "Peru",
                "04/03/1994", "I am a person.", "https://thispersondoesnotexist.com/",
                100, dni, phoneNumber
            )
        )
        val client: OkHttpClient    = ApiWorker.client()

        val originalText: String    = registerButton.text.toString()
        //registerButton.text         = "Creando..."

        client.newCall(request).enqueue(object: Callback {
            public override fun onFailure(call: Call, e: IOException): Unit {
                this@RegisterFragment.requireActivity().runOnUiThread {
                    Toast.makeText(
                        this@RegisterFragment.requireActivity(),
                        "Error de conexión", Toast.LENGTH_SHORT
                    ).show()
                }
                //registerButton.text = originalText
            }

            public override fun onResponse(call: Call, response: Response): Unit {
                response.body?.let {
                    this@RegisterFragment.requireActivity().runOnUiThread {
                        this@RegisterFragment.changeToMain(view)

                        Toast.makeText(
                            this@RegisterFragment.requireActivity(),
                            "Perfil registrado", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                //registerButton.text = originalText
            }
        })
    }
    private fun checkRegisterInputs(view: View): Boolean {
        val name: String        = view.findViewById<EditText>(R.id.user_name_input).text.toString()
        val email: String       = view.findViewById<EditText>(R.id.email_input_register).text.toString()
        val phone: String       = view.findViewById<EditText>(R.id.phone_register_input).text.toString()
        val password: String    = view.findViewById<EditText>(R.id.password_register_input).text.toString()
        val age: String         = view.findViewById<EditText>(R.id.age_register_input).text.toString()
        val dni: String         = view.findViewById<EditText>(R.id.dni_register_input).text.toString()
        val opt: RadioGroup     = view.findViewById<RadioGroup>(R.id.opt_register)

        //  > Inputs
        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty() || age.isEmpty() || dni.isEmpty() || (opt.checkedRadioButtonId == -1)) {
            this.makeToast("Por favor, complete todos los campos")
            return false
        }
        //  > Name
        if (
            (name.length < UserConstraints.minUserNameCharacter) ||
            (name.length > UserConstraints.maxUserNameCharacter)
        ) {
            this.makeToast("Su nombre debe tener entre ${UserConstraints.maxUserNameCharacter} y ${UserConstraints.maxUserNameCharacter} caracteres")
            return false
        }
        //  > Email
        if (!Functions.isValidEmail(email)) {
            this.makeToast("Correo electrónico inválido")
            return false
        }
        //  > Phone
        if (phone.length != UserConstraints.userPhoneNumberCharacter) {
            this.makeToast("Su número de teléfono debe tener ${UserConstraints.userPhoneNumberCharacter} caracteres")
            return false
        }
        //  > Password
        if (password.length < UserConstraints.minUserPasswordCharacter) {
            this.makeToast("La contraseña debe tener al menos ${UserConstraints.minUserPasswordCharacter} caracteres")
            return false
        }
        //  > DNI
        if (dni.length != UserConstraints.userDniCharacter) {
            this.makeToast("Su dni debe tener ${UserConstraints.userDniCharacter} caracteres")
            return false
        }

        return true
    }

    private fun changeToMain(view: View): Unit {
        val backButton: ImageButton = view.findViewById<ImageButton>(R.id.doctor_menu_back_button)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun makeToast(message: String): Unit {
        Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show()
    }
}