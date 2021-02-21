package com.hemanth.vishwaktask.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hemanth.vishwaktask.data.model.UserProfile
import com.hemanth.vishwaktask.databinding.FragmentLoginDialogBinding
import com.hemanth.vishwaktask.interfaces.ILoginDialog

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginDialogFragment : DialogFragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentLoginDialogBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var iLoginDialog: ILoginDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        iLoginDialog = context as ILoginDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        binding = FragmentLoginDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val emailId = binding.edtEmailId.text.toString()
            val password = binding.edtPassword.text.toString()
            auth.fetchSignInMethodsForEmail(emailId).addOnCompleteListener {
                val isNewUser = it.result?.signInMethods?.isEmpty() ?: false
                if (isNewUser) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "User Not available contact Admin",
                        Toast.LENGTH_LONG
                    ).show()
                } else {

                    auth.signInWithEmailAndPassword(emailId, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                binding.progressBar.visibility = View.GONE
                                auth.currentUser?.let { it1 -> iLoginDialog.onLoginSuccessful(it1) }
                                dismiss()
                            } else {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    context,
                                    "Wrong password",
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}