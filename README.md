# Flickr viewer app
## Overview
This app uses the Flickr API which displays recent photos. The app allows for the
searching of photos as well as displays images in a grid view by default.

## Functionality
### Search
- Single screen with a Text box that will let users enter text and search for images.
- Search is performed when enter/button is hit.
- if user does not enter any text, it should grab recent photos, otherwise query based on the text
### Grid
- Images are displayed in a grid view with 3 columns. 
- Images should be square.
### Error handling
- Error handling for network calls and loading UI
- Loading indicated by a spinning circular progress
- Errors should be displayed in a snackbar.
### Pagination
- the grid supports pagination (Not through a paging library)
### Extras
- Click a photo to see it's true aspect ratio and any meta data available.
- Basic unit testing 

## Code Structure
### UI layer
- ImagesScreen + ImagesScreenViewModel
- ImagesScreenUiState (loading, error, content)

### Domain layer
- ImageEntity (ui data representation of the image object)
- SearchRepository (interface)

### Data layer
- Image (data class)
- SearchRepositoryImpl
- SearchRemoteDataSource (connect to network dispatcher)
- SearchLocalDataSource (use rooms)

### Network
- FlickrApiService (relates to Retrofit)
- NetworkDispatcher

## API Reference
Get recent photos:
Documentation: https://www.flickr.com/services/api/flickr.photos.getRecent.html
Example query:
https://www.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=a0222db49599
9c951dc33702500fdc4d&format=json&nojsoncallback=1

Search:
Documentation: https://www.flickr.com/services/api/flickr.photos.search.html
Example query:
https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=a0222db495999c
951dc33702500fdc4d&text=spiderman&format=json&nojsoncallback=1

Image Loading:
Documentation: https://www.flickr.com/services/api/misc.urls.html
Format: https://live.staticflickr.com/{server-id}/{id}_{secret}.jpg

## Next Steps
- keep previous photos while loading is happening to prevent empty screen
- Close keyboard when user scrolls / after hitting enter to search
- Squares are provided by Flickr. Add safety layer to square them by the app as well.
- Consider different layout when rotating to landscape
