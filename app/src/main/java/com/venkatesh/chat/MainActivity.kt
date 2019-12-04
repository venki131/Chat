package com.venkatesh.chat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.database.FirebaseListAdapter
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.venkatesh.chat.MainActivity.Companion.TAG
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val SIGN_IN_REQUEST_CODE = 200
        const val TAG = "MainActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)
        fabSend.setOnClickListener {
            FirebaseDatabase.getInstance().reference.push().setValue(
                ChatMessage(
                    etMessage.text.toString(),
                    FirebaseAuth.getInstance().currentUser?.displayName
                )
            )
            etMessage.setText("")
            Log.d(TAG, "FAB clicked!" + FirebaseDatabase.getInstance().reference)
        }

        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(), SIGN_IN_REQUEST_CODE
            )
        } else {

            //user is already singed in
            Toast.makeText(
                this, "Welcome! " +
                        (FirebaseAuth.getInstance().currentUser?.displayName ?: "User Name Empty"),
                Toast.LENGTH_LONG
            ).show()

            displayChatMessages()
        }
    }

    private fun displayChatMessages() {

        val adapter = object : FirebaseListAdapter<ChatMessage>(
            this,
            ChatMessage::class.java,
            R.layout.chat_row_item,
            FirebaseDatabase.getInstance().reference
        ) {

            override fun populateView(v: View?, model: ChatMessage?, position: Int) {
                v?.findViewById<TextView>(R.id.txtUserName)?.text = model?.messageUser
                v?.findViewById<TextView>(R.id.txtMessageReceiving)?.text = model?.messageText
                v?.findViewById<TextView>(R.id.txtMsgTime)?.text =
                    DateFormat.format("dd-mm-yyyy (HH:mm:ss)", model?.messageTime!!)

            }

        }
        listView.adapter = adapter
        //recyclerView.adapter = ChatMessageAdapter(adapter,this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Successfully signed in. Welcome!", Toast.LENGTH_LONG).show()
                displayChatMessages()
            } else {
                Toast.makeText(
                    this,
                    "We couldn't sign you in. Please try again later.",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                .addOnCompleteListener()
            finish()
        }
        return true
    }
}

private fun <TResult> Task<TResult>.addOnCompleteListener() {
    Log.d(TAG, "Logout success")
}
