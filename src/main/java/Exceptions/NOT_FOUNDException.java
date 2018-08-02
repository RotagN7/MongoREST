package Exceptions;


public class NOT_FOUNDException extends Exception {
    private static final long serialVersionUID = 1L;
    private String errorMessage;

    public String getErrorMessage(){
        return errorMessage;
    }

    public NOT_FOUNDException(String errorMessage){
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public NOT_FOUNDException() {
        super();
    }
}
