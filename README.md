# RSS Reader

A simple RSS Reader application that allows you to add, remove, and display recent posts from RSS feeds. The feeds are stored in a `data.txt` file.

## Usage
This RSS Reader application can be used to keep track of updates from your favorite websites and blogs. By simply providing the URL of a website, the application will automatically extract the RSS feed and keep you updated with the latest posts. This can be particularly useful for:

- **News Aggregation:** Collect and read news from multiple sources in one place.
- **Content Monitoring:** Stay updated with the latest posts from your favorite blogs and websites.
- **Research:** Gather information from various sources efficiently.

## Features

- Add new RSS feed by providing a website URL.
- Remove an existing RSS feed.
- Display recent posts from the added RSS feeds.
- Save and load feeds from a file (`data.txt`).

## Requirements

- Java Development Kit (JDK) 8 or higher
- JSoup library (for HTML parsing)

## Code Structure
- **RSS.java:** The main class containing the logic for adding, removing, displaying feeds, and menu interaction.
- **RssFeed.java:** A class representing an RSS feed with properties like name, website URL, and RSS URL.
- **data.txt:** A file to store the list of RSS feeds. Each feed is stored in a new line in the format name;websiteUrl;rssUrl.
### Methods
#### 1. RSS Class
   - **main(String[] args):** Main method to start the application and display menu options.
   - **loadFeeds():** Loads feeds from data.txt.
   - **saveFeeds():** Saves feeds to data.txt.
   - **addFeed():** Adds a new feed by extracting the RSS URL from the provided website URL.
   - **removeFeed():** Removes a feed based on the provided website URL.
   - **ShowUpdates():** Displays recent posts from the selected feed or all feeds.
   - **extractPageTitle(String html):** Extracts the page title from the HTML content.
   - **fetchPageSource(String urlString):** Fetches the HTML content of the given URL.
   - **extractRssUrl(String url):** Extracts the RSS URL from the provided website URL.
   - **retrieveRssContent(String rssUrl):** Retrieves and displays the content of the RSS feed.
#### 2. RssFeed Class
   - **RssFeed(String name, String websiteUrl, String rssUrl):** Constructor to initialize an RSS feed.
   - **getCounter():** Returns the counter value.
   - **getName():** Returns the name of the feed.
   - **getWebsiteUrl():** Returns the website URL of the feed.
   - **getRssUrl():** Returns the RSS URL of the feed.
