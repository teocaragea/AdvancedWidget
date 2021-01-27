package com.example.advancedwidget;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jjurrius on 2017/09/05.
 */

public class ExampleCoding
{

    ArrayList<String> descriptions;
    ArrayList<String> links;
    ArrayList<String> titles;

    private InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean connectionAvailable(Context context) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public class GetStoriesInBackground extends AsyncTask<Integer, Integer, String>
    {

        @Override
        protected String doInBackground(Integer... params) {

            try {

                URL url = new URL("http://feeds.news24.com/articles/news24/TopStories/rss");

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();

                // We will get the XML from an input stream
                xpp.setInput(getInputStream(url), "UTF_8");

			        /* We will parse the XML content looking for the "<title>" tag which appears inside the "<item>" tag.
			         * However, we should take in consideration that the rss feed name also is enclosed in a "<title>" tag.
			         * As we know, every feed begins with these lines: "<channel><title>Feed_Name</title>...."
			         * so we should skip the "<title>" tag which is a child of "<channel>" tag,
			         * and take in consideration only "<title>" tag which is a child of "<item>"
			         *
			         * In order to achieve this, we will make use of a boolean variable.
			         */
                boolean insideItem = false;

                // Returns the type of current event: START_TAG, END_TAG, etc..
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {

                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem)
                                titles.add(xpp.nextText()); //extract the headline
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem)
                                links.add(xpp.nextText()); //extract the link of article
                        }
                        else if (xpp.getName().equalsIgnoreCase("description")) {
                            if (insideItem)
                                descriptions.add(xpp.nextText()); //extract the link of article
                        }
                    }else if(eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                        insideItem=false;
                    }

                    eventType = xpp.next(); //move to next element
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }


        @Override
        protected void onPostExecute(String result) {


        }

    }

}
