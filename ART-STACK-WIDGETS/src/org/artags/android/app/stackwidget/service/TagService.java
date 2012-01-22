/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.artags.android.app.stackwidget.service;

import org.artags.android.app.stackwidget.util.HttpUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.artags.android.app.stackwidget.Constants;
import org.artags.android.app.stackwidget.model.Tag;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author pierre
 */
public class TagService
{
    public static List<Tag> getTagList( String url )
    {
        List<Tag> list = new ArrayList<Tag>();
        String jsonflow = HttpUtils.getUrl( url );

        try
        {

            JSONTokener tokener = new JSONTokener(jsonflow);
            JSONObject json = (JSONObject) tokener.nextValue();
            JSONArray jsonTags = json.getJSONArray("tags");

            int max = (jsonTags.length() < Constants.MAX_TAGS) ? jsonTags.length() : Constants.MAX_TAGS;
            for (int i = 0; i < max; i++)
            {
                JSONObject jsonTag = jsonTags.getJSONObject(i);
                Tag tag = new Tag();
                tag.setId(jsonTag.getString("id"));
                tag.setText(jsonTag.getString("title"));
                tag.setThumbnailUrl(jsonTag.getString("imageUrl"));
                tag.setRating(jsonTag.getString("rating"));
                list.add(tag);
            }
        }
        catch (JSONException e)
        {
            Log.e(Constants.LOG_TAG, "JSON Parsing Error : " + e.getMessage(), e);
        }
        Log.e(Constants.LOG_TAG, "Tags fetched : " + list.size() );
        return list;
    }
    
    
}
