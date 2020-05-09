package Index;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

// Class designed to monitor changes in directories added to index

public class WatchDir {

    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private final IndexInterface iindex;

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }


    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

        keys.put(key, dir);
    }



    private void registerAll(final Path path) throws IOException {

        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }



    WatchDir(IndexInterface iindex) throws IOException {
        this.watcher = FileSystems.getDefault()
                .newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
        this.iindex=iindex;

        List<Path> dirlist=new ArrayList<>();
        dirlist=iindex.list();

        if(!dirlist.isEmpty()){

            for (Path dir:dirlist) {
                registerAll(dir);
            }

        }










    }






    void processEvents() {
        for (;;) {


            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {

                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                System.out.println(kind);
                if (kind == OVERFLOW) {
                    continue;
                }


                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);


                if (kind == ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);

                        }

                        iindex.indexFiles(child,false);
                    } catch (IOException x) {

                    }
                }

                    if (kind == ENTRY_MODIFY) {
                        try {



                            iindex.indexFiles(child,true);

                        } catch (IOException x) {

                        }
                }

                if (kind == ENTRY_DELETE) {
                    try {



                        iindex.removeDir(child);






                    } catch (IOException x) {

                    }
                }



            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

            }
                if (keys.isEmpty()) {
                    break;
                }
            }

    }



}}