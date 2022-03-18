package com.mistershorr.birthdaytracker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.backendless.Backendless
import com.mistershorr.birthdaytracker.databinding.ActivityLoginBinding
import com.backendless.exceptions.BackendlessFault

import com.backendless.BackendlessUser

import com.backendless.async.callback.AsyncCallback




class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // any time you need what would have been a "static" variable in java, you use
    // companion object in Kotlin. You access with ClassName.VARIABLE_NAME like Math.PI
    companion object {
        // keys for the key-value pairs for the intent extras
        val EXTRA_USERNAME = "username"
        val EXTRA_PASSWORD = "password"
    }

    // starting an activity for a result
    // Step 1: Register the Activity with a contract
    val startRegistrationForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        // decide what to do if the result is ok (if it was successful)
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            // Handle the Intent
            // note: editTexts are different from textViews in that you have to call setText
            binding.editTextLoginUsername.setText(intent?.getStringExtra(EXTRA_USERNAME))
            binding.editTextLoginPassword.setText(intent?.getStringExtra(EXTRA_PASSWORD))
        }
        // decide what to do if it was unsuccessful with a RESULT_CANCELED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Backendless.initApp( this, Constants.APP_ID, Constants.API_KEY );

        binding.buttonLoginLogin.setOnClickListener {
            val username = binding.editTextLoginUsername.text.toString()
            val password = binding.editTextLoginPassword.text.toString()

            Backendless.UserService.login(
                username,
                password,
                object : AsyncCallback<BackendlessUser?> {
                    override fun handleResponse(user: BackendlessUser?) {
                        // user has been logged in
                        Log.d("Login Activity", "handleResponse: ${user?.email}")
                        Toast.makeText(this@LoginActivity, "${user?.getProperty("username")}", Toast.LENGTH_SHORT).show()
                    }

                    override fun handleFault(fault: BackendlessFault) {
                        // login failed, to get the error code call fault.getCode()
                        Log.d("Login Activity", "handleFault: ${fault.message}")
                    }
                })
        }

        binding.textViewLoginCreateAccount.setOnClickListener {
            val username = binding.editTextLoginUsername.text.toString()
            val password = binding.editTextLoginPassword.text.toString()

            // 2. create the intent and package the extras
            // the context is the activity you are in (here we can use this)
            val registrationIntent
            = Intent(this, RegistrationActivity::class.java).apply {
                putExtra(EXTRA_USERNAME, username)
                putExtra(EXTRA_PASSWORD, password)
            }
            // 3. launch the activity
            startActivity(registrationIntent)
            // 3b. Alternate: Could launch the activity for a result instead
            // use the variable from the register for result contract above
            startRegistrationForResult.launch(registrationIntent)

        }
    }
}