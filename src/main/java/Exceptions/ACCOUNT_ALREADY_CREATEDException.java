package Exceptions;


public class ACCOUNT_ALREADY_CREATEDException extends Exception{
    private static final long serialVersionUID = 1L;
    private String errorMessage;

    public String getErrorMessage(){
        return errorMessage;
    }

    public ACCOUNT_ALREADY_CREATEDException(String errorMessage){
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public ACCOUNT_ALREADY_CREATEDException() {
        super();
    }
}
