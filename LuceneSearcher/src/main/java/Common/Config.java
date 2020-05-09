package Common;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.pl.PolishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private final static Path indexerPath = Paths.get(System.getProperty("user.home")+"\\index");

    public static  Path getIndexerPath() {
        return indexerPath;
    }

    public static  IndexWriterConfig indexConfig()  {

        Analyzer analyzer = languageAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        return iwc;
    }




    public static PerFieldAnalyzerWrapper languageAnalyzer(){

        Map<String, Analyzer> analyzerPerField = new HashMap<>();
        analyzerPerField.put(Common.Const.CONTENT_PL, new PolishAnalyzer());
        analyzerPerField.put(Common.Const.CONTENT_EN, new EnglishAnalyzer());

        PerFieldAnalyzerWrapper aWrapper =
                new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerPerField);

        return aWrapper;



    }

}
