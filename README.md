# Dude-Wheres-My-Stream
Final Project for Android Programming

##v1.0.0
everything is implemented and tested for planned initial feature rollout
Next steps: some minor tidying up and then possibly move on to v2 features

##11/15/2022:2015
settings are working and api calls are updated to include provider preferences and watch_region
Next steps: search function!!!

##11/15/2022:1845
settings fragment working except for dark mode (dark mode functionality likely to get axed from submission) settings are saved as livedata in viewmodel
Next steps: modify api calls to make use of watch providers and region codes where possible, search function

##11/13/2022:2255
settings fragment xml layout has been updated to include all planned settings
Next steps: create adapter for popup settings RV also create the actual fragments for region and provider settings, livedata in viewmodel to hold setting values, search function

##11/13/2022:1735
updated xml layouts for minioneshow and large show to include full network data
Next steps: settings and search

##11/13/2022:1540
fragment_minioneshow.xml updated

##11/13/2022:1305
API calls all appear to be working, trending and favorites tab are working.
Next steps: update xml for minioneshow and largeoneshow to incorporate all available data. settings tab, search function

##11/12/2022:2305
API calls for scraping streaming providers is up and running. all other api calls are implemented but untested
Next steps test the other api calls for details and credits and update views to make use of this info, settings tab, search function 

## 11/10/2022:1707
THIS VERSION WILL NOT RUN
Base structure for api calls is written. VideoData class has been updated to include all possible info from existing api calls
Next steps implement network calls in TMDBRepo, settings tab, search function(including new api calls)

## 11/10/2022:1430
With the exception of search feature the app functionality is now complete
Next steps setup API calls to get real data, settings tab is nonsense atm, implement search function

## 11/9/2022:1756
Implemented a web scraping method that will be able to pull links directly from the TMDb pages provided by the TMDb api
Next steps are putting together the API calls from TMDb

## 11/9/2022:1308
Basic layout/functionality is complete. Can navigate through the app similar to what the final version should be.
Next steps are to get network functionality implemented and then add in the search function.

## 11/8/2022:1529
Trending and Favorites list are working along with the pop up minioneview and oneview fragments.
Next steps are to get settings and menu overlay working and then move on to API calls
                         