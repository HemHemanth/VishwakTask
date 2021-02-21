package com.hemanth.vishwaktask

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hemanth.vishwaktask.adapter.CommentsAdapter
import com.hemanth.vishwaktask.adapter.UserDetailsAdapter
import com.hemanth.vishwaktask.data.local.VishwakDatabase
import com.hemanth.vishwaktask.data.local.entity.CommentEntity
import com.hemanth.vishwaktask.data.model.UserProfile
import com.hemanth.vishwaktask.databinding.ActivityMainBinding
import com.hemanth.vishwaktask.fragments.LoginDialogFragment
import com.hemanth.vishwaktask.interfaces.ILoginDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), ILoginDialog {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var vishwakDb: VishwakDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        vishwakDb = VishwakDatabase.getDbHandler(this)

        binding.imgComment.setOnClickListener {
            writeComment()
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            supportFragmentManager.beginTransaction()
            .replace(R.id.container, LoginDialogFragment.newInstance("", ""))
            .commit()
        } else {
            binding.progressBar.visibility = View.VISIBLE
            showUserDetails(currentUser)
        }
    }

    private fun showUserDetails(currentUser: FirebaseUser) {
        binding.progressBar.visibility = View.VISIBLE
        val database = Firebase.database
        val userRef = database.getReference("users").child(currentUser.uid)
        userRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userProfile = snapshot.getValue(UserProfile::class.java)
                Log.d("UserProfile", userProfile.toString())
                userProfile?.let {
                    binding.txtUserName.text = userProfile.userName
                    Glide.with(this@MainActivity)
                        .load(userProfile.profileImage)
                        .into(binding.imgProfile)
                    binding.txtEmail.text = currentUser.email
                    binding.txtCompany.movementMethod = LinkMovementMethod.getInstance()
                    binding.txtCompany.text = Html.fromHtml("<a href='${userProfile.companySiteURL}'>${userProfile.companySiteURL}</a>")

                    val socialList: ArrayList<String> = arrayListOf(
                        userProfile.youTubeURL,
                        userProfile.facebookURL,
                        userProfile.instagramURL
                    )

                    binding.layoutUserDetails.visibility = View.VISIBLE
                    binding.recyclerViewUserDetails.apply {
                        adapter = UserDetailsAdapter(socialList)
                        layoutManager = LinearLayoutManager(this@MainActivity)
                    }
                }
                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progressBar.visibility = View.GONE
            }

        })
    }

    override fun onLoginSuccessful(currentUser: FirebaseUser) {
        showUserDetails(currentUser)
    }

    private fun writeComment() {
        val currentUser = auth.currentUser
        val view = LayoutInflater.from(this).inflate(R.layout.comment_layout, null, false)
        CoroutineScope(Dispatchers.IO).launch {
            val comments: List<CommentEntity> =
                    currentUser?.uid?.let { vishwakDb.getCommentDao().getAllComments(it) } ?: emptyList()
            withContext(Dispatchers.Main) {
                view.findViewById<RecyclerView>(R.id.recyclerViewComments).apply {
                    adapter = CommentsAdapter(comments)
                    layoutManager = LinearLayoutManager(context)
                }
            }
        }
        val edtComment = view.findViewById<EditText>(R.id.edtComment)
        val btnSubmit = view.findViewById<AppCompatButton>(R.id.btnSubmit)

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setView(view)
        alertDialog.create()
        val dialog = alertDialog.show()

        btnSubmit.setOnClickListener {
            val commentText = edtComment.text.toString().trim()
            if (commentText.isNotEmpty()) {
                val commentEntity =
                    auth.currentUser?.uid?.let { it1 -> CommentEntity(userId = it1, comment = commentText) }
                CoroutineScope(Dispatchers.IO).launch {
                    if (commentEntity != null) {
                        vishwakDb.getCommentDao().insertComment(commentEntity)
                    }
                }
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Comment should not be empty", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        auth.signOut()
    }
}
