package Searcher;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static Common.Config.*;
import static Common.Const.*;


public class LuceneSearch implements SearchInterface {

   private SearchSettings settings;
   private Analyzer analyzer;


    public LuceneSearch(SearchSettings settings)
    {
        this.settings = settings;
        analyzer = languageAnalyzer();

    }

    private IndexSearcher createSearcher()
    {

        try {
            Directory dir = FSDirectory.open(getIndexerPath());
            IndexReader indexReader = DirectoryReader.open(dir);

            return new IndexSearcher(indexReader);

        }   catch (Exception ex)
        {
            System.out.println("There was a problem with opening a directory with an index. Check the location user_directory\\index");
            System.exit(1);

        }


        return null;

    }

    private String bodyField()
    {
        if(settings.getLang().equals("en"))
        {
            return CONTENT_EN;

        }   else
        {
            return CONTENT_PL;

        }


    }

    private Query createQuery(String query) throws ParseException {

        String contentField = bodyField();


        switch (settings.getQueryType())
        {
            case TERM:
                Query termQuery = new QueryParser(contentField,analyzer).parse(query);

                return termQuery;
            case PHRASE:
                Query phraseQuery = new TermQuery(new Term(contentField,query));

                return phraseQuery;
            case FUZZY:
                Query fuzzyQuery = new FuzzyQuery(new Term(contentField,query));

                return fuzzyQuery;



        }



            return null;


    }




    public List<Document> searchedDocuments(String query) throws IOException, ParseException {
            IndexSearcher indexSearcher = createSearcher();

            Query searchedQuery = createQuery(query);

            TopDocs topDocs = indexSearcher.search(searchedQuery, settings.getLimit());



            List<Document> documents = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            documents.add(indexSearcher.doc(scoreDoc.doc));
            }

            return documents;

    }



    public  List<SearchedText> searchDocumetsWithTextDisplay(String query) throws ParseException, IOException, InvalidTokenOffsetsException {

        List<Document> documents = searchedDocuments(query);

        List<SearchedText> matches= new ArrayList<>();

        Formatter formatter = new SimpleHTMLFormatter("<",">");

        Query searchedQuery = createQuery(query);

        QueryScorer scorer = new QueryScorer(searchedQuery);

        Highlighter highlighter = new Highlighter(formatter, scorer);

        for(Document doc: documents)
        {

            SearchedText searchedText = new SearchedText();


            String text = doc.get(bodyField());




            TokenStream tokenStream = analyzer.tokenStream(bodyField(),text);

            String[] frags = highlighter.getBestFragments(tokenStream, text, 3);

            searchedText.setPath(doc.get(PATH_FILE));

            searchedText.setTextFragments(frags);

            matches.add(searchedText);


        }


            return matches;

    }




}
