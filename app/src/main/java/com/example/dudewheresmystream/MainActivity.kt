package com.example.dudewheresmystream

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
//TODO options will include:
// light/dark mode selector
// streaming app preferences
// country of origin
// ...

//TODO RVs in the home fragment should be limited to a certain number of items and then add a display more button at the end that opens up something like the search fragment