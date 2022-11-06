package com.example.dudewheresmystream

import android.app.Notification.Action
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBar
import android.os.Bundle
import android.text.SpannableString
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.text.buildSpannedString
import androidx.core.text.toSpannable
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commitNow
import com.example.dudewheresmystream.api.VideoData
import com.example.dudewheresmystream.databinding.ActionBarBinding
import com.example.dudewheresmystream.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        val globalDebug = true
        private const val mainFragTag = "mainFragTag"
        private const val optionsFragTag = "optionsFragTag"
        private const val searchFragTag = "searchFragTag"
        private const val showFragTag = "showFragTag"
    }
    //TODO setup actionbarbinding see hw4
    private var actionBarBinding: ActionBarBinding? = null
    private val viewModel: MainViewModel by viewModels()

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

    private fun actionBarMenu(){
        actionBarBinding!!.actionMenu.setOnClickListener {
            //TODO open menu options
            //TODO Home, Trending, Favorites, Explore
        }
    }

    private fun actionBarSearch(){
        actionBarBinding!!.actionSearch.addTextChangedListener {
            //TODO implement search functionality here
            //TODO may be different depending on which fragment we're in
        }
    }

    private fun addHomeFragment(){
        supportFragmentManager.commitNow{
            add(R.id.main_frame, HomeFragment.newInstance(), mainFragTag)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        setSupportActionBar(activityMainBinding.toolbar)
        supportActionBar?.let{
            initActionBar(it)
        }
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Inflate the menu; this adds items to the action bar if it is present.
                //menuInflater.inflate(R.menu.menu_main, menu)
                //TODO might want to use this for the menu???
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle action bar item clicks here.
                return when (menuItem.itemId) {
                    android.R.id.home -> false // Handle in fragment
                    else -> true
                }
            }
        })
        addHomeFragment()
        if(globalDebug){
            val debugList = listOf<VideoData>(
                VideoData("Key-0", SpannableString("Crawlspace"),"https://www.themoviedb.org/t/p/w440_and_h660_face/qEu6qI5sVoIe10gD1BQBqxcNIW2.jpg",SpannableString("After witnessing a brutal murder in a cabin, a man hides in a crawlspace while the killers scour the property for a hidden fortune. As they draw nearer, he must decide if the crawlspace will be his tomb or the battleground in his fight for survival.")),
                VideoData("Key-1", SpannableString("Rick and Morty"),"https://www.themoviedb.org/t/p/w440_and_h660_face/cvhNj9eoRBe5SxjCbQTkh05UP5K.jpg",SpannableString("Rick is a mentally-unbalanced but scientifically gifted old man who has recently reconnected with his family. He spends most of his time involving his young grandson Morty in dangerous, outlandish adventures throughout space and alternate universes. Compounded with Morty's already unstable family life, these events cause Morty much distress at home and school.")),
                VideoData("Key-2", SpannableString("Secret Headquarters"),"https://www.themoviedb.org/t/p/w440_and_h660_face/8PsHogUfvjWPGdWAI5uslDhHDx7.jpg",SpannableString("While hanging out after school, Charlie and his friends discover the headquarters of the world’s most powerful superhero hidden beneath his home. When villains attack, they must team up to defend the headquarters and save the world.")),
                VideoData("Key-3", SpannableString("The Simpsons"),"https://www.themoviedb.org/t/p/w440_and_h660_face/zI3E2a3WYma5w8emI35mgq5Iurx.jpg",SpannableString("Set in Springfield, the average American town, the show focuses on the antics and everyday adventures of the Simpson family; Homer, Marge, Bart, Lisa and Maggie, as well as a virtual cast of thousands. Since the beginning, the series has been a pop culture icon, attracting hundreds of celebrities to guest star. The show has also made name for itself in its fearless satirical take on politics, media and American life in general.")),
                VideoData("Key-4", SpannableString("She-Hulk: Attorney at Law"),"https://www.themoviedb.org/t/p/w440_and_h660_face/hJfI6AGrmr4uSHRccfJuSsapvOb.jpg",SpannableString("Jennifer Walters navigates the complicated life of a single, 30-something attorney who also happens to be a green 6-foot-7-inch superpowered hulk.")),
                VideoData("Key-5", SpannableString("Terrifier 2"),"https://www.themoviedb.org/t/p/w440_and_h660_face/b6IRp6Pl2Fsq37r9jFhGoLtaqHm.jpg",SpannableString("After being resurrected by a sinister entity, Art the Clown returns to Miles County where he must hunt down and destroy a teenage girl and her younger brother on Halloween night. As the body count rises, the siblings fight to stay alive while uncovering the true nature of Art's evil intent.")),
                VideoData("Key-6", SpannableString("Grey's Anatomy"),"https://www.themoviedb.org/t/p/w440_and_h660_face/daSFbrt8QCXV2hSwB0hqYjbj681.jpg",SpannableString("Follows the personal and professional lives of a group of doctors at Seattle’s Grey Sloan Memorial Hospital.")),
                VideoData("Key-7", SpannableString("Chucky"),"https://www.themoviedb.org/t/p/w440_and_h660_face/kY0BogCM8SkNJ0MNiHB3VTM86Tz.jpg",SpannableString("After a vintage Chucky doll turns up at a suburban yard sale, an idyllic American town is thrown into chaos as a series of horrifying murders begin to expose the town’s hypocrisies and secrets. Meanwhile, the arrival of enemies — and allies — from Chucky’s past threatens to expose the truth behind the killings, as well as the demon doll’s untold origins.")),
                VideoData("Key-8", SpannableString("Coco"),"https://www.themoviedb.org/t/p/w440_and_h660_face/gGEsBPAijhVUFoiNpgZXqRVWJt2.jpg",SpannableString("Despite his family’s baffling generations-old ban on music, Miguel dreams of becoming an accomplished musician like his idol, Ernesto de la Cruz. Desperate to prove his talent, Miguel finds himself in the stunning and colorful Land of the Dead following a mysterious chain of events. Along the way, he meets charming trickster Hector, and together, they set off on an extraordinary journey to unlock the real story behind Miguel's family history.")),
                VideoData("Key-9", SpannableString("Halloween Ends"),"https://www.themoviedb.org/t/p/w440_and_h660_face/h1FGQ6FRW6kNx4ACxjCJ18ssW3Y.jpg",SpannableString("Four years after the events of Halloween in 2018, Laurie has decided to liberate herself from fear and rage and embrace life. But when a young man is accused of killing a boy he was babysitting, it ignites a cascade of violence and terror that will force Laurie to finally confront the evil she can’t control, once and for all.")),)
            viewModel.postFavorite(debugList[9])
            viewModel.postFavorite(debugList[1])
            viewModel.postFavorite(debugList[6])
            viewModel.postTrending(debugList)
        }
    }
}
//TODO RVs in the home fragment should be limited to a certain number of items and then add a display more button at the end that opens up something like the search fragment