package alpha;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RedditPost {

    private String domain;
    private String subreddit;
    private String subreddit_id;

    private String id;
    private String author;

    private String selftext;
    private String title;
    private String url;

    private long score;
    private long ups;
    private long downs;
    private long created_utc;
    private long num_comments;

    private boolean over_18;
    private boolean is_self;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getSubreddit_id() {
        return subreddit_id;
    }

    public void setSubreddit_id(String subreddit_id) {
        this.subreddit_id = subreddit_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSelftext() {
        return selftext;
    }

    public void setSelftext(String selftext) {
        this.selftext = selftext;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getUps() {
        return ups;
    }

    public void setUps(long ups) {
        this.ups = ups;
    }

    public long getDowns() {
        return downs;
    }

    public void setDowns(long downs) {
        this.downs = downs;
    }

    public long getCreated_utc() {
        return created_utc;
    }

    public void setCreated_utc(long created_utc) {
        this.created_utc = created_utc;
    }

    public long getNum_comments() {
        return num_comments;
    }

    public void setNum_comments(long num_comments) {
        this.num_comments = num_comments;
    }

    public boolean isOver_18() {
        return over_18;
    }

    public void setOver_18(boolean over_18) {
        this.over_18 = over_18;
    }

    public boolean isIs_self() {
        return is_self;
    }

    public void setIs_self(boolean is_self) {
        this.is_self = is_self;
    }

    private String capitalize(String string) {
        String caps = string.substring(0, 1)
                .toUpperCase()
                + string.substring(1).toLowerCase();
        return caps;
    }

    private Method retrieveSetterName(Field field) throws NoSuchMethodException {
        StringBuilder methodNameBuilder = new StringBuilder(
                (field.getType() == boolean.class) ? "is" : "get");
        methodNameBuilder.append(capitalize(field.getName()));
        Method method = this.getClass()
                .getMethod(methodNameBuilder.toString());
        return method;
    }

    public String insertSql() {
        Class clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Class> fieldTypeMap = new HashMap<>();
        for (Field field: fields) {
            fieldTypeMap.put(field.getName(), field.getType());
        }
        StringBuilder statement = new StringBuilder("INSERT INTO POSTS VALUES(");

        String quoteChar;
        try {
            for (int i = 0; i < fields.length; i++) {
                Method getter = retrieveSetterName(fields[i]);
                quoteChar = (fields[i].getType() == long.class
                        || fields[i].getType() == boolean.class) ? "" : "\"";
                statement.append(String.format("%s=%s%s%s",
                        fields[i].getName(),
                        quoteChar,
                        getter.invoke(this),
                        quoteChar));
                if (i < fields.length-1) {
                    statement.append(", ");
                } else {
                    statement.append(");");
                }
            }
        } catch (NoSuchMethodException|
                IllegalAccessException|
                InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return statement.toString();
    }

    public static void main(String[] args) {
        RedditPost post = new RedditPost();
        System.out.println(post.insertSql());
    }
}
