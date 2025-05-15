package     com.oncontigoteam.oncontigo.profile

import      android.os.Bundle
import      android.view.LayoutInflater
import      android.view.View
import      android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import      androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import      com.oncontigoteam.oncontigo.R
import com.oncontigoteam.oncontigo.shared.user.UserWrapperSettings


public final class Profile : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.openMenu(view)

        this.assign(view)
    }

    private fun openMenu(view: View): Unit {
        val backButton: ImageButton = view.findViewById<ImageButton>(R.id.profile_back_button)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
    private fun assign(view: View): Unit {
        val image: ImageView = view.findViewById<ImageView>(R.id.profile_image)
        Glide.with(this)
            .load("https://thispersondoesnotexist.com/")
            .into(image)

        val email: TextView = view.findViewById<TextView>(R.id.profile_email_text)
        email.text = UserWrapperSettings.email
    }

    private fun replaceFragment(fragment: Fragment): Unit {
        val fragmentManager: FragmentManager = this.requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}