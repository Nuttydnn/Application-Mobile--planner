package org.classapp.whatsup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, "Welcome to WhatsUp!!", Toast.LENGTH_LONG).show()

        val signInButton = findViewById<Button>(R.id.signInBtn)
        val signInPanel = findViewById<LinearLayout>(R.id.signInPanel)

        val usernameTxt = findViewById<EditText>(R.id.usernameTxt)
        val pwdTxt = findViewById<EditText>(R.id.pwdTxt)
        val enterBtn = findViewById<Button>(R.id.enterBtn)

        signInButton.setOnClickListener {
            signInPanel.visibility = View.VISIBLE
        }

        enterBtn.setOnClickListener {
            if (usernameTxt.text.toString().equals("android") && pwdTxt.text.toString().equals("google"))
            {
                Toast.makeText(this, "Welcome to Member Area", Toast.LENGTH_LONG).show()



                var t:Intent = Intent(this, EventsTabsActivity::class.java)
                t.putExtra("username", usernameTxt.text.toString())
                startActivity(t)

                usernameTxt.setText("")
                pwdTxt.setText("")
                signInPanel.visibility = View.GONE


            }
            else {
                Toast.makeText(this, "Please Try Again!", Toast.LENGTH_LONG).show()
            }
        }
    }
}