/* Copyright (c) 2010-2014 ARTags Project owners (see http://www.artags.org)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.artags.android.app.stackwidget.service;

import org.artags.android.app.stackwidget.util.HttpUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Date;
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
        long start = new Date().getTime();
        Log.i( Constants.LOG_TAG, "Tag Service : begin tag fetching" );
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
        long end = new Date().getTime();
        Log.i(Constants.LOG_TAG, "Tag Service : " + list.size() + " tags fetched in " + ( start - end ) + "ms"  );
        return list;
    }
    
    
}
