package put.sk.publish;

import java.util.ArrayList;

/**
 * Single topic in API
 */
public class Topic {
    /**
     * Topic name
     */
    private String name;
    /**
     * Array list with articles in topic
     */
    private ArrayList<Article> articles;

    /**
     * Topic constructor - without articles
     * @param name Topic name
     */
    public Topic(String name) {
        this.articles = new ArrayList();
        this.name = name;
    }

    /**
     * Custom toString()
     * @return Topic name
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Getter - Topic name
     * @return Topic name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set articles in topic
     * @param articles Articles as ArrayList
     */
    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }

    /**
     * Add article to Topic
     * @param article Article to add
     * @return Operation status
     */
    public boolean addArticle(Article article) {
        return this.articles.add(article);
    }
}
