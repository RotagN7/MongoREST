package Exceptions;

public class LOGIN_FAILEDException extends Exception{
    private static final long serialVersionUID = 1L;
    private String errorMessage;

    public String getErrorMessage(){
        return errorMessage;
    }

    public LOGIN_FAILEDException(String errorMessage){
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public LOGIN_FAILEDException() {
        super();
    }
}
