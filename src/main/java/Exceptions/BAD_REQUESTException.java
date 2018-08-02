package Exceptions;


public class BAD_REQUESTException extends Exception {
    private static final long serialVersionUID = 1L;
    private String errorMessage;

    public String getErrorMessage(){
        return errorMessage;
    }

    public BAD_REQUESTException(String errorMessage){
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public BAD_REQUESTException(){ super();}
}
