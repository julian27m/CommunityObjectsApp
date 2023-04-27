package com.example.navigationdrawercommunityobjects.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.example.navigationdrawercommunityobjects.R
import com.example.navigationdrawercommunityobjects.model.LoginActivity
import com.example.navigationdrawercommunityobjects.model.NetworkConnection
import com.example.navigationdrawercommunityobjects.viewmodel.ProfileViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var fab: FloatingActionButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mFBanalytics: FirebaseAnalytics
    private var fallbackBoolean by Delegates.notNull<Boolean>()
    val viewModel = ProfileViewModel
    val instance = viewModel.getInstance()
    val user = instance.getUser().value



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

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

        fab = findViewById(R.id.fab)
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)


        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }


        replaceFragment(HomeFragment())



        bottomNavigationView.setBackground(null)
        bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                //redirect to activityProfile with the LoginActivity data
                R.id.profile -> replaceFragment(ProfileFragment())

            }
            true
        })
        fab.setOnClickListener(View.OnClickListener { showBottomDialog() })

        val header = navigationView.getHeaderView(0)
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
            R.id.nav_help -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HelpFragment()).commit()
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
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }



    public fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }



    private fun showBottomDialog() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheetlayout)
        val donationLayout = dialog.findViewById<LinearLayout>(R.id.layoutDonation)
        val requestLayout = dialog.findViewById<LinearLayout>(R.id.layoutRequest)
        val cancelButton = dialog.findViewById<ImageView>(R.id.cancelButton)
        donationLayout.setOnClickListener {
            dialog.dismiss()
            //Toast.makeText(this@MainActivity, "Upload donation is clicked", Toast.LENGTH_SHORT).show()
//            set the donate fragment
            if (user != null){
                replaceFragment(DonateFragment())
            }else
                showLoginDialog()

        }
        requestLayout.setOnClickListener {
            dialog.dismiss()
            if (user != null){
                Toast.makeText(this@MainActivity, "I have a request is Clicked", Toast.LENGTH_SHORT)
                    .show()
            }else
                showLoginDialog()

        }
        cancelButton.setOnClickListener { dialog.dismiss() }
        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }

    private fun showLoginDialog() {
        val loginConstraintLayout = findViewById<ConstraintLayout>(R.id.LoginConstraintLayout)
        val view = LayoutInflater.from(this@MainActivity).inflate(R.layout.login_dialog, loginConstraintLayout, false)
        val btnLogin = view.findViewById<Button>(R.id.issueDone)
        val btnNotNow = view.findViewById<Button>(R.id.notNow)
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setView(view)
        val alertDialog = builder.create()
        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            alertDialog.dismiss()
        }
        btnNotNow.setOnClickListener { alertDialog.dismiss() }
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()
    }


    private fun showNetworkDisconnectedDialog() {
        val loginConstraintLayout = findViewById<ConstraintLayout>(R.id.NetworkConstraintLayout)
        val view = LayoutInflater.from(this@MainActivity).inflate(R.layout.dialog_network, loginConstraintLayout, false)
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setView(view)
        val alertDialog = builder.create()
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()

        //dissmiss the dialog after 5 seconds
        val handler = Handler()
        handler.postDelayed({ alertDialog.dismiss() }, 5000)


    }

    private fun showNetworkDialog() {
        val loginConstraintLayout = findViewById<ConstraintLayout>(R.id.NetworkConnectedConstraintLayout)
        val view = LayoutInflater.from(this@MainActivity).inflate(R.layout.dialog_network_connected, loginConstraintLayout, false)
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setView(view)
        val alertDialog = builder.create()
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()

        val handler = Handler()
        handler.postDelayed({ alertDialog.dismiss() }, 5000)

    }

}







