package Index;


import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface IndexInterface {



        void removeDir (Path path)  throws IOException;
        List<Path> list() throws IOException;
        void purge() throws IOException;
        void indexFiles(Path path,boolean update) throws IOException;
        void indexDirectory(Path path) throws IOException;
        void reindex() throws IOException;






}
