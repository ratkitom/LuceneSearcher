package Searcher;


// Class designed to store browser settings

public class SearchSettings {

    private String lang;
    private String color;
    private int limit;
    private String details;
    private QueryType queryType;

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    public enum QueryType
    {

        TERM, PHRASE, FUZZY


    }







    public SearchSettings(){

        lang="en";
        color="of";
        details="of";
        limit=Integer.MAX_VALUE;
        queryType=QueryType.TERM;



    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }





}
