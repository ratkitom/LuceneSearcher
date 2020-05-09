package Searcher;


import Common.Const;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class Searching

{


    private InputHandler inputHandler;
    private SearchInterface searchInterface;
    private SearchSettings searchSettings;
    private Terminal terminal;

    Searching(InputHandler inputHandler) throws IOException
    {

        this.inputHandler = inputHandler;
        searchSettings = new SearchSettings();
        searchInterface = new LuceneSearch(searchSettings);
        buildTerminal();


    }

    private void buildTerminal() throws IOException {


            terminal = TerminalBuilder.builder()
                   .dumb(true)
                   .jna(false)
                   .jansi(true)
                   .build();







    }

    public Terminal getTerminal()
    {
        return this.terminal;

    }

    private void process() throws ParseException, InvalidTokenOffsetsException, IOException {


        switch (inputHandler.getRequestType())
        {
            case LANG:
                searchSettings.setLang(inputHandler.getString());
                break;

            case COLOR:
                searchSettings.setColor(inputHandler.getString());
                break;

            case DETAILS:
                searchSettings.setDetails(inputHandler.getString());
                break;

            case LIMIT:
                searchSettings.setLimit(inputHandler.getIntValue());
                break;

            case FUZZY:
                searchSettings.setQueryType(SearchSettings.QueryType.FUZZY);
                break;

            case TERM:
                searchSettings.setQueryType(SearchSettings.QueryType.TERM);
                break;

            case PHRASE:
                searchSettings.setQueryType(SearchSettings.QueryType.PHRASE);
                break;

            default:

                printResults();




        }




    }

    private void printResults() throws ParseException, InvalidTokenOffsetsException, IOException {



        if(isDetailsOn())
        {

            List<SearchedText> results = searchInterface.searchDocumetsWithTextDisplay(inputHandler.getString());

            terminal.writer().println(results.size());

            Pattern pattern =Pattern.compile("<(.+?)>");

            for (SearchedText searchedText: results)
            {

                terminal.writer().println(new AttributedStringBuilder().style(AttributedStyle.DEFAULT.bold())
                        .append(searchedText.getPath())
                        .toAnsi());



                for (String frag : searchedText.getTextFragments())
                {








                    if(searchSettings.getColor().equals("on"))
                    {

                        terminal.writer().println(new AttributedStringBuilder()
                            .append(frag)
                            .styleMatches(pattern,AttributedStyle.DEFAULT.foreground(1))
                                .toAnsi());


                    } else {

                        terminal.writer().println(new AttributedStringBuilder()
                                .append(frag)
                                .styleMatches(pattern,AttributedStyle.DEFAULT.bold())
                                .toAnsi());

                    }





                }



            }


        } else
        {

            List<Document> results=searchInterface.searchedDocuments(inputHandler.getString());

            terminal.writer().println(results.size());

            for(Document document: results)
            {

                terminal.writer().println(new AttributedStringBuilder().style(AttributedStyle.DEFAULT.bold())
                        .append(document.get(Const.PATH_FILE))
                        .toAnsi());





            }


        }



    }


    private boolean isDetailsOn()
    {
        if(searchSettings.getDetails().equals("on"))
        {

            return true;
        }

        return false;


    }





    public static void main(String[] args) throws ParseException, InvalidTokenOffsetsException, IOException, InputHandler.InvalidRequestException {




        InputHandler inputHandler = new InputHandler();

        Searching searching = new Searching(inputHandler);

        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(searching.getTerminal())
                .build();
        String line;

        for (; ;)
        {

            line = lineReader.readLine("> ");
            inputHandler.processInput(line);
            searching.process();



        }






    }




}
