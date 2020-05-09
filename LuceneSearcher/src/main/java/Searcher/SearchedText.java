package Searcher;


// Class designed to store fragments of searched text in particular file

public class SearchedText {


    private String path;

    private String[] textFragments;

    public SearchedText(){}


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getTextFragments() {
        return textFragments;
    }

    public void setTextFragments(String[] textFragments) {
        this.textFragments = textFragments;
    }
}
