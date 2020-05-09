package Index;




import javax.swing.text.Document;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class Indexing {

    private InputHandler inputHandler;
    private IndexInterface indexInterface;

public Indexing(InputHandler inputHandler, IndexInterface indexInterface) throws IOException
{

    this.inputHandler=inputHandler;
    this.indexInterface=indexInterface;


}



    private void process() throws IOException {

        switch (inputHandler.getCommand()){

        case ADD:
            indexInterface.indexDirectory(inputHandler.getDirpath());
            indexInterface.indexFiles(inputHandler.getDirpath(),false);

            break;
        case LIST:
            //List indexed directories

            for(Path path:indexInterface.list())
            {

                System.out.println(path);

            }


            break;
        case PURGE:

            indexInterface.purge();

            break;
        case REINDEX:
            //remove all directories from index and add passed path
            indexInterface.reindex();

            break;

        case REMOVE:
            //remove passed path from index
            indexInterface.removeDir(inputHandler.getDirpath());

            break;
        case NONE:
            //continue watchi directory
            WatchDir watchDir=new WatchDir(indexInterface);
            watchDir.processEvents();





    }

    }


    public static void main(String[] args) throws InputHandler.WrongParametr, InputHandler.WrongNumberOfParameres, InputHandler.WrongPathException, IOException {

        args=new String[1];
        args[0]="list";


        InputHandler inputHandler = new InputHandler(args);

        IndexInterface indexInterface = new LuceneIndex();

        Indexing indexing = new Indexing(inputHandler,indexInterface);

        indexing.process();






    }
}
