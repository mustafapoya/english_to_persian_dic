package net.golbarg.engtoper.models;

public class PhraseEnglish {
    private int id;
    private String fromLanguage;
    private String toLanguage;
    private int favorite;
    private String article;
    private int type;

    public PhraseEnglish() {
    }

    public PhraseEnglish(int id, String fromLanguage, String toLanguage, int favorite, String article, int type) {
        this.id = id;
        this.fromLanguage = fromLanguage;
        this.toLanguage = toLanguage;
        this.favorite = favorite;
        this.article = article;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromLanguage() {
        return fromLanguage;
    }

    public void setFromLanguage(String fromLanguage) {
        this.fromLanguage = fromLanguage;
    }

    public String getToLanguage() {
        return toLanguage;
    }

    public void setToLanguage(String toLanguage) {
        this.toLanguage = toLanguage;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PhraseEnglish{" +
                "id=" + id +
                ", fromLanguage='" + fromLanguage + '\'' +
                ", toLanguage='" + toLanguage + '\'' +
                ", favorite=" + favorite +
                ", article='" + article + '\'' +
                ", type=" + type +
                '}';
    }
}
