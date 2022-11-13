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
        if(globalDebug){
            //TODO creating this list just got pretty hard lets circle back here
            val debugList = listOf<DiscoverVideoData>(
                //VideoData("Key-0", SpannableString("Crawlspace"),"https://image.tmdb.org/t/p/original/qEu6qI5sVoIe10gD1BQBqxcNIW2.jpg",SpannableString("After witnessing a brutal murder in a cabin, a man hides in a crawlspace while the killers scour the property for a hidden fortune. As they draw nearer, he must decide if the crawlspace will be his tomb or the battleground in his fight for survival."),"https://www.themoviedb.org/movie/879538-crawlspace/watch?language=en-US"),
                //VideoData("Key-1", SpannableString("Rick and Morty"),"https://image.tmdb.org/t/p/original/cvhNj9eoRBe5SxjCbQTkh05UP5K.jpg",SpannableString("Rick is a mentally-unbalanced but scientifically gifted old man who has recently reconnected with his family. He spends most of his time involving his young grandson Morty in dangerous, outlandish adventures throughout space and alternate universes. Compounded with Morty's already unstable family life, these events cause Morty much distress at home and school."),"https://www.themoviedb.org/tv/60625-rick-and-morty/watch?language=en-US"),
                //VideoData("Key-2", SpannableString("Secret Headquarters"),"https://image.tmdb.org/t/p/original/8PsHogUfvjWPGdWAI5uslDhHDx7.jpg",SpannableString("While hanging out after school, Charlie and his friends discover the headquarters of the world’s most powerful superhero hidden beneath his home. When villains attack, they must team up to defend the headquarters and save the world."),"https://www.themoviedb.org/movie/791155-secret-headquarters/watch?language=en-US"),
                //VideoData("Key-3", SpannableString("The Simpsons"),"https://image.tmdb.org/t/p/original/zI3E2a3WYma5w8emI35mgq5Iurx.jpg",SpannableString("Set in Springfield, the average American town, the show focuses on the antics and everyday adventures of the Simpson family; Homer, Marge, Bart, Lisa and Maggie, as well as a virtual cast of thousands. Since the beginning, the series has been a pop culture icon, attracting hundreds of celebrities to guest star. The show has also made name for itself in its fearless satirical take on politics, media and American life in general."),"https://www.themoviedb.org/tv/456-the-simpsons/watch?language=en-US"),
                //VideoData("Key-4", SpannableString("She-Hulk: Attorney at Law"),"https://image.tmdb.org/t/p/original/hJfI6AGrmr4uSHRccfJuSsapvOb.jpg",SpannableString("Jennifer Walters navigates the complicated life of a single, 30-something attorney who also happens to be a green 6-foot-7-inch superpowered hulk."),"https://www.themoviedb.org/tv/92783-she-hulk-attorney-at-law/watch?language=en-US"),
                //VideoData("Key-5", SpannableString("Terrifier 2"),"https://image.tmdb.org/t/p/original/b6IRp6Pl2Fsq37r9jFhGoLtaqHm.jpg",SpannableString("After being resurrected by a sinister entity, Art the Clown returns to Miles County where he must hunt down and destroy a teenage girl and her younger brother on Halloween night. As the body count rises, the siblings fight to stay alive while uncovering the true nature of Art's evil intent."),"https://www.themoviedb.org/movie/663712-terrifier-2/watch?language=en-US"),
                //VideoData("Key-6", SpannableString("Grey's Anatomy"),"https://image.tmdb.org/t/p/original/daSFbrt8QCXV2hSwB0hqYjbj681.jpg",SpannableString("Follows the personal and professional lives of a group of doctors at Seattle’s Grey Sloan Memorial Hospital."),"https://www.themoviedb.org/tv/1416-grey-s-anatomy/watch?language=en-US"),
                //VideoData("Key-7", SpannableString("Chucky"),"https://image.tmdb.org/t/p/original/kY0BogCM8SkNJ0MNiHB3VTM86Tz.jpg",SpannableString("After a vintage Chucky doll turns up at a suburban yard sale, an idyllic American town is thrown into chaos as a series of horrifying murders begin to expose the town’s hypocrisies and secrets. Meanwhile, the arrival of enemies — and allies — from Chucky’s past threatens to expose the truth behind the killings, as well as the demon doll’s untold origins."),"https://www.themoviedb.org/tv/90462-chucky/watch?language=en-US"),
                //VideoData("Key-8", SpannableString("Coco"),"https://image.tmdb.org/t/p/original/gGEsBPAijhVUFoiNpgZXqRVWJt2.jpg",SpannableString("Despite his family’s baffling generations-old ban on music, Miguel dreams of becoming an accomplished musician like his idol, Ernesto de la Cruz. Desperate to prove his talent, Miguel finds himself in the stunning and colorful Land of the Dead following a mysterious chain of events. Along the way, he meets charming trickster Hector, and together, they set off on an extraordinary journey to unlock the real story behind Miguel's family history."),"https://www.themoviedb.org/movie/354912-coco/watch?language=en-US"),
                //VideoData("Key-9", SpannableString("Halloween Ends"),"https://image.tmdb.org/t/p/original/h1FGQ6FRW6kNx4ACxjCJ18ssW3Y.jpg",SpannableString("Four years after the events of Halloween in 2018, Laurie has decided to liberate herself from fear and rage and embrace life. But when a young man is accused of killing a boy he was babysitting, it ignites a cascade of violence and terror that will force Laurie to finally confront the evil she can’t control, once and for all."),"https://www.themoviedb.org/movie/616820-halloween-ends/watch?language=en-US")
                )
            //viewModel.postTrending(debugList)
            viewModel.tmdbTrendingRefresh("1")
            //viewModel.tmdbDetailRefresh(DiscoverVideoData("","", listOf(),"","",1402,0F,0F,"",ShowType.TV))
        }
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
