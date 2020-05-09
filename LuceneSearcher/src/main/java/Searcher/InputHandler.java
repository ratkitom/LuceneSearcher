package Searcher;

// A class designed to handle the arguments from command line

public class InputHandler {


    private RequestType requestType;
    private String string;
    private int intValue;

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }


    public enum RequestType
    {
        LANG, COLOR, DETAILS, LIMIT, TERM, PHRASE, FUZZY, QUERY


    }

    public class InvalidRequestException extends Exception{

        public InvalidRequestException(){

            super("Invalid request");
        }
    }




    public InputHandler ()
    {



    }

    public void processInput(String line) throws InvalidRequestException {

        if(line.length()>0)
        {
            line = line.trim();

            if(line.charAt(0) == '%')
            {
                String[] settings = line.split("\\s+");

                parseSettings(settings);


            }   else

            {
                string = line;
                requestType = RequestType.QUERY;


            }




        }   else

        {
            throw new InvalidRequestException();


        }




    }


    private void parseSettings(String[] settings) throws InvalidRequestException {
        if(settings.length == 2)
        {
            switch (settings[0])
            {
                case "%lang":
                    string = lang(settings[1]);
                    requestType = RequestType.LANG;
                    break;
                case "%details":
                    string = state(settings[1]);
                    requestType = RequestType.DETAILS;
                    break;
                case "%color":
                    string = state(settings[1]);
                    requestType = RequestType.COLOR;
                    break;
                case "%limit":
                    intValue = limit(Integer.parseInt(settings[1]));
                    requestType = RequestType.LIMIT;
                    break;

                default:

                    throw new InvalidRequestException();
            }


        }   else if(settings.length == 1)
        {
            switch (settings[0])
            {
                case "%term":
                    requestType = RequestType.TERM;
                    break;
                case "%phrase":
                    requestType = RequestType.PHRASE;
                    break;
                case "%fuzzy":
                    requestType = RequestType.FUZZY;
                    break;

                default:

                    throw new InvalidRequestException();


            }


        }   else

        {
            throw new InvalidRequestException();

        }



    }


    private String lang(String lang) throws InvalidRequestException {
       switch (lang)
       {
           case "pl":
           case "en":
               return lang;

           default: break;

       }

        throw new InvalidRequestException();

    }

    private String state(String state) throws InvalidRequestException {

        switch (state)
        {
            case "on":
            case "off":
                return state;

            default: break;



        }

        throw new InvalidRequestException();



    }

    private int limit(int limit) throws InvalidRequestException {
        if(limit >0)
        {

            return limit;
        }   else if(limit == 0){

            return Integer.MAX_VALUE;

        }

        throw new InvalidRequestException();



    }









}
