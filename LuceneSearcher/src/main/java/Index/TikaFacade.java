package Index;
import org.apache.tika.Tika;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;


// Class responsible for extraction text from files

public class TikaFacade {

    private Tika tika;
    private LanguageDetector detector;

    public TikaFacade(){

        tika=new Tika();
        detector=new OptimaizeLangDetector().loadModels();


    }

    public String extractText(Path file){

        try (InputStream stream = Files.newInputStream(file)) {


            return tika.parseToString(stream);

        } catch (Exception ex){

            // ignore files unable to extract


        }

        return null;
    }

    public String languageDetect (String content) {



        LanguageResult result = detector.detect(content);

        return result.getLanguage();

    }


}
