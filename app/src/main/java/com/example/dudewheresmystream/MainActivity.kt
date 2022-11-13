package com.example.dudewheresmystream

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBar
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.example.dudewheresmystream.api.DiscoverVideoData
import com.example.dudewheresmystream.api.ShowType
import com.example.dudewheresmystream.databinding.ActionBarBinding
import com.example.dudewheresmystream.databinding.ActivityMainBinding
import com.example.dudewheresmystream.ui.*

class MainActivity : AppCompatActivity(){
    companion object {
        val globalDebug = true
        private const val mainFragTag = "mainFragTag"
        private const val settingsFragTag = "settingsFragTag"
        private const val searchFragTag = "searchFragTag"
        private const val trendingFragTag = "trendingFragTag"
        private const val favoritesFragTag = "favoritesFragTag"
        private const val aboutFragTag = "aboutFragTag"
    }
    private var actionBarBinding: ActionBarBinding? = null
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private val viewModel: MainViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        setSupportActionBar(activityMainBinding.toolbar)
        supportActionBar?.let{
            initActionBar(it)
        }
        activityMainBinding.apply{
            drawerToggle = ActionBarDrawerToggle(this@MainActivity,drawerLayout,R.string.open,R.string.close)
            drawerLayout.addDrawerListener(drawerToggle)
            drawerToggle.syncState()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navView.setNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.navOption1 -> {
                        //Home
                        launchHome()
                    }
                    R.id.navOption2 -> {
                        //Settings
                        launchSettings()
                    }
                    R.id.navOption3 -> {
                        //Trending
                        launchTrending()
                    }
                    R.id.navOption4 -> {
                        //Favorites
                        launchFavorites()
                    }
                    R.id.navOption5 -> {
                        //Search
                        launchSearch()
                    }
                    R.id.navOption6 -> {
                        //About
                        launchAbout()
                    }
                }
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
        }
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Inflate the menu; this adds items to the action bar if it is present.
                menuInflater.inflate(R.menu.menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle action bar item clicks here.
                return when (menuItem.itemId) {
                    android.R.id.home -> false // Handle in fragment
                    R.id.menuSettings ->{
                        launchSettings()
                        true
                    }
                    else -> true
                }
            }
        })
        addHomeFragment()
        actionBarSearch()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }

    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
        actionBarBinding!!.actionSearch.clearFocus()
    }
    private fun initActionBar(actionBar: ActionBar){
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBarBinding = ActionBarBinding.inflate(layoutInflater)
        actionBar.customView = actionBarBinding?.root
    }


    private fun actionBarSearch(){
        actionBarBinding!!.actionSearch.addTextChangedListener {
            //TODO implement search functionality here
            //TODO functionality should be the same across fragments and should have bar hidden in settings fragment
            if (it.toString().isEmpty()){
                hideKeyboard()
            }
        }
    }

    private fun addHomeFragment(){
        supportFragmentManager.commitNow{
            add(R.id.main_frame, HomeFragment.newInstance(), mainFragTag)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }
    }

    private fun clearBackstack(){
        val mainFrag = supportFragmentManager.findFragmentByTag(mainFragTag)
        if(mainFrag!!.isVisible){
            mainFrag.childFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        else{
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    private fun launchHome(){
        //Redundant but fits in with the naming scheme of the other nav methods
        clearBackstack()
    }

    private fun launchSettings(){
        clearBackstack()
        supportFragmentManager.commit {
            replace(R.id.main_frame, SettingsFragment.newInstance(), settingsFragTag)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            addToBackStack(settingsFragTag)
        }
    }

    private fun launchTrending(){
        clearBackstack()
        supportFragmentManager.commit {
            replace(R.id.main_frame, LargeTrendingFragment.newInstance(), trendingFragTag)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            addToBackStack(trendingFragTag)
        }
    }

    private fun launchFavorites(){
        clearBackstack()
        supportFragmentManager.commit {
            replace(R.id.main_frame, LargeFavoritesFragment.newInstance(), favoritesFragTag)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            addToBackStack(favoritesFragTag)
        }
    }

    private fun launchSearch(){
        clearBackstack()
        supportFragmentManager.commit {
            replace(R.id.main_frame, SearchFragment.newInstance(), searchFragTag)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            addToBackStack(searchFragTag)
        }
    }

    private fun launchAbout(){
        clearBackstack()
        supportFragmentManager.commit {
            replace(R.id.main_frame, AboutFragment.newInstance(), aboutFragTag)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            addToBackStack(aboutFragTag)
        }
    }
    //TODO RVs in the home fragment should be limited to a certain number of items and then add a display more button at the end that opens up the respective Large Fragment
}
