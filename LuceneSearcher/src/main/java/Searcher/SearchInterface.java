package Searcher;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import java.io.IOException;
import java.util.List;

public interface SearchInterface {

    public List<Document> searchedDocuments(String query) throws IOException, ParseException;
    public  List<SearchedText> searchDocumetsWithTextDisplay(String query) throws ParseException, IOException, InvalidTokenOffsetsException;




}
