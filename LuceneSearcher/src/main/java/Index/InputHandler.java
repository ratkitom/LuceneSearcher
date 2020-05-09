package Index;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// A class designed to handle the arguments from command line

public class InputHandler {

    private Path path;
    private CommandType commandType;

    public class WrongNumberOfParameres extends Exception {

        public WrongNumberOfParameres(){

            super("Incorrect number of parametrs\n"+helper());

        }

    }

    public class WrongParametr extends Exception {

        public WrongParametr(){

            super("Incorrect parametr\n"+helper());


        }

    }

    public class WrongPathException extends Exception{

        public WrongPathException(){

            super("The passed path is not a directory or regular file");
        }

    }



    public enum CommandType
    {
        ADD,
        REINDEX,
        REMOVE,
        LIST,
        PURGE,
        NONE


    }

    public InputHandler(String[] args) throws WrongParametr, WrongNumberOfParameres, WrongPathException {



            setCommandType(args);


    }


    private void setCommandType(String[] args) throws WrongNumberOfParameres, WrongParametr, WrongPathException {

        if (args.length > 2) {


            throw new WrongNumberOfParameres();

        } else if (args.length == 0) {

            commandType = CommandType.NONE;


        } else {


            if (args.length == 2) {
                switch (args[0]) {
                    case "add":
                        commandType = CommandType.ADD;

                        break;
                    case "rm":
                        commandType = CommandType.REMOVE;
                        break;

                    default:

                        throw new WrongParametr();
                }

                setDirpath(args[1]);
            } else {
                switch (args[0]) {
                    case "reindex":
                        commandType = CommandType.REINDEX;
                        break;
                    case "list":
                        commandType = CommandType.LIST;
                        break;
                    case "purge":
                        commandType = CommandType.PURGE;
                    default:

                        throw new WrongParametr();

                }
            }


        }

    }

    private String helper() {

        String helper="Parametrs:\n-add <dir>\n-rm <dir>\n-reindex\n-list\n-purge";

        return helper;
    }

    private void setDirpath(String path) throws WrongPathException {


        if (Files.isDirectory(Paths.get(path)) || Files.isRegularFile(Paths.get(path))) {


            this.path = Paths.get(path);


        } else {

            throw new WrongPathException();

        }


    }

    public Path getDirpath() {
        return path;
    }

    public CommandType getCommand() {

        return commandType;
    }



}
