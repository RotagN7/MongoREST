package Exceptions;


public class PERMISSION_FAILUREException extends Exception {
    private static final long serialVersionUID = 1L;
    private String errorMessage;

    public String getErrorMessage(){
        return errorMessage;
    }

    public PERMISSION_FAILUREException(String errorMessage){
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public PERMISSION_FAILUREException() {
        super();
    }
}
