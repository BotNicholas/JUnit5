package exceptions;

public class DuplicateObjectException extends Exception{
    public DuplicateObjectException(){
        super("You try to insert duplicate object!");
    }
}
