package alpha;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Crawler {
    private static final String target = "http://www.reddit.com/r/all.json";
    public static void main(String[] args) {
        try {
            URL url = new URL(target);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent",
                    "Crawler for reddit search project by /u/abhijat0");
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer page = new StringBuffer();
            String tmp;
            while ((tmp = reader.readLine()) != null) {
                page.append(tmp);
            }
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
            JsonParser parser = new JsonParser();
            JsonObject root = parser
                    .parse(page.toString())
                    .getAsJsonObject();
            JsonObject data = root.get("data").getAsJsonObject();
            JsonArray children = data.get("children").getAsJsonArray();
            for (JsonElement child: children) {
                RedditPost post = new RedditPost();
                JsonObject o = child
                        .getAsJsonObject()
                        .get("data")
                        .getAsJsonObject();

                post.setDomain(o.get("domain").getAsString());
                post.setSubreddit(o.get("subreddit").getAsString());
                post.setSelftext(o.get("selftext").getAsString());

                post.setId(o.get("id").getAsString());
                post.setAuthor(o.get("author").getAsString());
                post.setScore(o.get("score").getAsLong());
                post.setSubreddit_id(o.get("subreddit_id").getAsString());
                post.setTitle(o.get("title").getAsString());
                post.setUrl(o.get("url").getAsString());

                post.setDowns(o.get("downs").getAsLong());
                post.setUps(o.get("ups").getAsLong());
                post.setCreated_utc(o.get("created_utc").getAsLong());
                post.setNum_comments(o.get("num_comments").getAsLong());

                post.setOver_18(o.get("over_18").getAsBoolean());
                post.setIs_self(o.get("is_self").getAsBoolean());

                //System.out.println(gson.toJson(o));
                System.out.println(post.insertSql());
                //return;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
