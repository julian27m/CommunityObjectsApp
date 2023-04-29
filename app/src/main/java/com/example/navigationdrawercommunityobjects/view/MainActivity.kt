package com.example.navigationdrawercommunityobjects.view

import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.navigationdrawercommunityobjects.R
import com.example.navigationdrawercommunityobjects.databinding.ActivityMainBinding
import com.example.navigationdrawercommunityobjects.databinding.BottomsheetlayoutBinding
import com.example.navigationdrawercommunityobjects.model.NetworkConnection
import com.example.navigationdrawercommunityobjects.viewmodel.ProfileViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mFBanalytics: FirebaseAnalytics
    private var fallbackBoolean by Delegates.notNull<Boolean>()
    val viewModel = ProfileViewModel
    val instance = viewModel.getInstance()
    val user = instance.getUser().value



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Im using this to unable landscape mode
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        fallbackBoolean = false

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {
                if (fallbackBoolean) {
                    //Log.d("MainActivity", "NetworkConnection: isConnected")
                    showNetworkDialog()
                    //dismissNetworkDialog()
                    fallbackBoolean = false
                }
                //Log.d("MainActivity", "NetworkConnection: isConnected")
                //showNetworkDialog()
            } else {
                //Log.d("MainActivity", "NetworkConnection: isNotConnected")
                showNetworkDisconnectedDialog()
                fallbackBoolean = true
            }
        })

        mFBanalytics = FirebaseAnalytics.getInstance(this)



        setSupportActionBar(binding.toolbar)


        binding.navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
            binding.navView.setCheckedItem(R.id.nav_home)
        }


        replaceFragment(HomeFragment())



        binding.bottomNavigationView.setBackground(null)
        binding.bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                //redirect to activityProfile with the LoginActivity data
                R.id.profile -> replaceFragment(ProfileFragment())

            }
            true
        })
        binding.fab.setOnClickListener(View.OnClickListener { showBottomDialog() })

        val header = binding.navView.getHeaderView(0)
        //View Binding for the header
        val navName = header.findViewById<View>(R.id.navName) as TextView
        val navEmail = header.findViewById<View>(R.id.navEmail) as TextView

        val viewModel = ProfileViewModel.getInstance()

        viewModel.getUser().observe(this, Observer { user ->
            Log.d("ProfileFragment", "getUser().observe() called")
            if (user != null) {
                navName.text = user.name
                navEmail.text = user.email
            }
        })

    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            R.id.nav_top -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TopFragment()).commit()
            R.id.nav_about -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AboutFragment()).commit()
            R.id.nav_support -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SupportFragment()).commit()

            //logout
            R.id.nav_logout -> {
                if(instance.user.value != null){
                    val networkConnection = NetworkConnection(applicationContext)
                    networkConnection.observe(this, Observer { isConnected ->
                        if (!isConnected) {
                            showNetworkDisconnectedDialog()
                            fallbackBoolean = true
                        }else{
                            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail()
                                .build()
                            val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
                            mGoogleSignInClient.signOut()
                            instance.setUser(null)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    )}else{
                    showLoginDialog()
                }
            }

        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }



    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }



    private fun showBottomDialog() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //View Binding for the bottom sheet
        val bottomSheetBinding = BottomsheetlayoutBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)
        bottomSheetBinding.layoutDonation.setOnClickListener {
            dialog.dismiss()
            //Toast.makeText(this, "Upload donation is clicked", Toast.LENGTH_SHORT).show()
            //set the donate fragment
            if (user != null){
                replaceFragment(DonateFragment())
            }else
                showLoginDialog()

        }
        bottomSheetBinding.layoutRequest.setOnClickListener {
            dialog.dismiss()
            //Toast.makeText(this, "Request donation is clicked", Toast.LENGTH_SHORT).show()
            //set the request fragment
            if (user != null){
                Toast.makeText(this@MainActivity, "I have a request is Clicked", Toast.LENGTH_SHORT)
                    .show()
            }else
                showLoginDialog()

        }

        bottomSheetBinding.cancelButton.setOnClickListener { dialog.dismiss() }

        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }

}







