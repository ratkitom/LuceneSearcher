package Index;
import Common.Const;
import Common.Config;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import org.apache.lucene.document.Document;


public class LuceneIndex implements IndexInterface {


   private TikaFacade tika;
   private IndexWriter indexWriter;

   public LuceneIndex(){


       tika=new TikaFacade();
       try {
           indexWriter = indexWriter();
       } catch (Exception ex){


       }
   }


   private Directory openIndexDir() throws IOException{

       Directory indexdir = FSDirectory.open(Config.getIndexerPath());
       return indexdir;

   }




    public void indexFiles(Path path, boolean udpate) throws IOException {



       if(Files.isDirectory(path)) {



           Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
               @Override
               public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {


                   try {

                       if (udpate) removeFile(file);


                           indexFile(file);


                   } catch (Exception ex) {



                   }


                   return FileVisitResult.CONTINUE;


               }


           });
       } else {
           try {

               if (udpate) removeFile(path);
               indexFile(path);

           }    catch (Exception ex){


           }

       }

                indexWriter.commit();
    }



    private void indexFile(Path file) throws IOException {

        Document doc=createDoc(file);


        if(!(doc==null) && Files.isRegularFile(file)) {

            indexWriter.updateDocument(new Term(Const.PATH_FILE, file.toString()), doc);

        }


    }
    public void indexDirectory(Path path) throws IOException{


       Document doc=createDirDoc(path);

       if (!(doc==null)) {

           indexWriter.updateDocument(new Term(Const.PATH_DIR, path.toString()), doc);
           indexWriter.commit();
       }



    }
    private Document createDoc(Path file){

        Document doc=new Document();

        doc.add(new StringField(Const.PATH_FILE,file.toString(), Field.Store.YES));



        String content=tika.extractText(file);



        if(!(content==null)) {

            String lang=tika.languageDetect(content);

            if (lang.equals("en")) {
                doc.add(new TextField(Const.CONTENT_EN, content, Field.Store.YES));
            } else if (lang.equals("pl")) {

                doc.add(new TextField(Const.CONTENT_PL, content, Field.Store.YES));
            }

            return doc;
        }


        return null;
    }

    private Document createDirDoc(Path path){

       Document doc=new Document();


        doc.add(new StringField(Const.PATH_DIR,path.toString(),Field.Store.YES));

        return doc;

    }

    private IndexWriter indexWriter() throws IOException {



       return new IndexWriter(openIndexDir(),Config.indexConfig());

}



    private void removeFile(Path path) throws IOException {

       indexWriter.deleteDocuments(new Term(Const.PATH_FILE,path.toString()));

        }

     private void removeDicrectoryDoc(Path path) throws IOException {

         indexWriter.deleteDocuments(new Term(Const.PATH_DIR, path.toString()));

     }

    @Override
    public void removeDir(Path path) throws IOException {


            // If path is pointing a file
            removeFile(path);

            // If path is pointing a direcory

            removeDicrectoryDoc(path);
            Query dirQuery = new WildcardQuery(new Term(Const.PATH_FILE, path.toString() + "/*"));
            indexWriter.deleteDocuments(dirQuery);

            indexWriter.commit();


        }

    public List<Path> list() {
        List<Path> list=new ArrayList<>();

       try {
           IndexReader reader=DirectoryReader.open(openIndexDir());
           IndexSearcher searcher=new IndexSearcher(reader);
           Query query=new WildcardQuery(new Term(Const.PATH_DIR,"*"));
           TopDocs docs=searcher.search(query,Integer.MAX_VALUE);
           ScoreDoc[] hits = docs.scoreDocs;

           for (ScoreDoc scoreDoc:hits)
           {
               Document document=searcher.doc(scoreDoc.doc);
               Path path=Paths.get(document.get(Const.PATH_DIR));
               list.add(path);

           }



       } catch (Exception ex){

           // Return empty list
       }






        return list;
    }

    public void purge() throws IOException {


           indexWriter.deleteAll();
           indexWriter.commit();




    }



    public void reindex(){


       try {

           List<Path> indexerdDirs = list();

           purge();

           for (Path path : indexerdDirs) {

               indexFiles(path, false);

           }

       } catch (Exception ex){

           // ignore errors

       }

    }



}
