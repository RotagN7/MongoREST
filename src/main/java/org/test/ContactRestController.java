package org.test;


import Exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import static org.test.AccessRights.*;

@CrossOrigin(origins = "http://localhost:63343")
@RequestScope
@RestController
@RequestMapping(value = "/jobPositions")
public class ContactRestController {

    private Account loggedInUser;
    private boolean loggedIn = false;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private PositionRepository positionRepo;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public Account login(@RequestBody String userNameOrEmail, String password) throws NOT_FOUNDException {
        for (Account existingAccount : accountRepo.findAll()) {
            if ((existingAccount.getUserName().equals(userNameOrEmail) || existingAccount.getEmail().equals(userNameOrEmail)) && (existingAccount.getPassword().equals(password))) {
                loggedInUser = existingAccount;
                loggedIn = true;
                return loggedInUser;
            }
        }
        throw new NOT_FOUNDException("No accounts with that username or password found");
    }

    //  Gets all positions
    @RequestMapping(method = RequestMethod.GET, value = "/positions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Position>> getAllPositions() throws NOT_FOUNDException {
        if (positionRepo.findAll().size() != 0 && positionRepo.findAll().size() >= 1) {
            return new ResponseEntity<>(positionRepo.findAll(), HttpStatus.OK);
        } else {
            throw new NOT_FOUNDException("No positions could be found");
        }
    }

//    Gets a single position using the requested Id
    @RequestMapping(method = RequestMethod.GET, value = "/positions/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Position> getSinglePosition(@PathVariable String id) throws NOT_FOUNDException {
            if (positionRepo.exists(id) != true) {
                throw new NOT_FOUNDException("Could not find requested position, " + id + ".");
            }
            return new ResponseEntity<>(positionRepo.findOne(id), HttpStatus.OK);
    }

    //  Gets all accounts
    @RequestMapping(method = RequestMethod.GET, value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Account>> getAllAccounts() throws NOT_FOUNDException {
        if (accountRepo.findAll().size() != 0 && accountRepo.findAll().size() >= 1) {
            return new ResponseEntity<>(accountRepo.findAll(), HttpStatus.OK);
        } else {
            throw new NOT_FOUNDException("No accounts could be found");
        }
    }

//    Gets a single account using the requested Id
    @RequestMapping(method = RequestMethod.GET, value = "/accounts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getSingleAccount(@PathVariable String id) throws NOT_FOUNDException, LOGIN_FAILEDException, PERMISSION_FAILUREException {
//        if (loggedIn == true) {
//            if (loggedInUser.getAccessRights() == manager || loggedInUser.getAccessRights() == owner || loggedInUser.getId() == id) {
//                if (accountRepo.exists(id) != true) {
//                    throw new NOT_FOUNDException("Could not find requested account, " + id + ".");
//                }
                return new ResponseEntity<>(accountRepo.findOne(id), HttpStatus.OK);
//            } else {
//                throw new PERMISSION_FAILUREException("User does not have the correct access rights to view this account's details");
//            }
//        } else{
//            throw new LOGIN_FAILEDException("No user is logged in, please log in before continuing");
//        }
    }

    //  Creates a new position
    @RequestMapping(method = RequestMethod.POST, value = "/positions", produces = MediaType.APPLICATION_JSON_VALUE)
    public Position createPosition(@RequestBody Position position) throws LOGIN_FAILEDException, PERMISSION_FAILUREException {
//        if (loggedIn == true) {
//            if (loggedInUser.getAccessRights() != user) {
                return positionRepo.save(position);
//            } else {
//                throw new PERMISSION_FAILUREException("User does not have the correct access rights to create a position");
//            }
//        }else{
//            throw new LOGIN_FAILEDException("No user is logged in, please log in before continuing");
//        }
    }

    //  Creates a new account
    @RequestMapping(method = RequestMethod.POST, value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public Account createAccount(@RequestBody Account account) throws ACCOUNT_ALREADY_CREATEDException {
        for (Account existingAccount : accountRepo.findAll()) {
            if ((existingAccount.getUserName().equals(account.getUserName()))) {
                throw new ACCOUNT_ALREADY_CREATEDException("There is an account already created with that userName");
            }
            if ((existingAccount.getEmail().equals(account.getEmail()))) {
                throw new ACCOUNT_ALREADY_CREATEDException("There is an account already created with that email");
            }
        }
            account.setAccessRights(AccessRights.user);
            return accountRepo.save(account);
    }

    //  Updates a position
    @RequestMapping(method = RequestMethod.PUT, value = "positions/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Position> updatePosition(@PathVariable String id, @RequestBody Position position) throws NOT_FOUNDException, PERMISSION_FAILUREException, LOGIN_FAILEDException {
        if (positionRepo.exists(id) != true) {
            throw new NOT_FOUNDException("Could not find the position, " + id + ", to be updated");
        }
//        if (loggedIn == true) {
            if (loggedInUser.getAccessRights() != user) {
                Position updatePosition = positionRepo.findOne(id);
                updatePosition.setTitle(position.getTitle());
                updatePosition.setDescription(position.getDescription());
                updatePosition.setLevel(position.getLevel());
                updatePosition.setLocation(position.getLocation());
                updatePosition.setRecommendedExperience(position.getRecommendedExperience());
                updatePosition.setSalary(position.getSalary());
                return new ResponseEntity<>(positionRepo.save(updatePosition), HttpStatus.OK);
            } else {
                throw new PERMISSION_FAILUREException("User does not have the correct access rights to change this position's details");
            }
//        }else{
//            throw new LOGIN_FAILEDException("User is not logged in, please log in before continuing");
//        }
    }

    //  Updates an account
    @RequestMapping(method = RequestMethod.PUT, value = "accounts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> updateAccount(@PathVariable String id, @RequestBody Account account) throws NOT_FOUNDException, PERMISSION_FAILUREException, LOGIN_FAILEDException {
        if (accountRepo.exists(id) != true) {
            throw new NOT_FOUNDException("Could not find the account, " + id + ", to be updated");
        }
        if (loggedIn == true) {
            if (loggedInUser.getAccessRights() == owner || loggedInUser.getId() == id) {
                Account updateAccount = accountRepo.findOne(id);
                updateAccount.setPhoneNumber(account.getPhoneNumber());
                updateAccount.setAge(account.getAge());
                updateAccount.setDegree(account.getDegree());
                updateAccount.setEmail(account.getEmail());
                updateAccount.setFirstName(account.getFirstName());
                updateAccount.setLastName(account.getLastName());
                updateAccount.setPassword(account.getPassword());
                updateAccount.setUserName(account.getUserName());
                updateAccount.setAccessRights(account.getAccessRights());
                return new ResponseEntity<>(accountRepo.save(updateAccount), HttpStatus.OK);
            }
            throw new PERMISSION_FAILUREException("User does not have the correct access rights to change this account's details");
        }else{
            throw new LOGIN_FAILEDException("User is not logged in, please log in before continuing");
        }
    }

    //  Deletes a position
    @RequestMapping(method = RequestMethod.DELETE, value = "positions/{id}")
    public void deletePosition(@PathVariable String id) throws NOT_FOUNDException, PERMISSION_FAILUREException, LOGIN_FAILEDException {
        if (positionRepo.exists(id) != true) {
            throw new NOT_FOUNDException("Could not find the account, " + id + ", to be deleted");
        }
//        if (loggedIn == true) {
            if(loggedInUser.getAccessRights() != user) {
                positionRepo.delete(id);
            }else{
                throw new PERMISSION_FAILUREException("User does not have the correct access rights to delete this position");
            }
//        }else{
//            throw new LOGIN_FAILEDException("User is not logged in, please log in before continuing");
//        }
    }

    //  Deletes an account
    @RequestMapping(method = RequestMethod.DELETE, value = "accounts/{id}")
    public void deleteAccount(@PathVariable String id) throws NOT_FOUNDException, PERMISSION_FAILUREException, LOGIN_FAILEDException{
        if (accountRepo.exists(id) != true) {
            throw new NOT_FOUNDException("Could not find the account, " + id + ", to be deleted");
        }
        if (loggedIn == true) {
            if(loggedInUser.getAccessRights() == manager || loggedInUser.getAccessRights() == owner || loggedInUser.getId() == id) {
                accountRepo.delete(id);
            }else{
                throw new PERMISSION_FAILUREException("User does not have the correct access rights to delete this account");
            }
        }else{
            throw new LOGIN_FAILEDException("User is not logged in, please log in before continuing");
        }
    }

    //  Adds a position to an account that the user applied for.
    @RequestMapping(method = RequestMethod.PUT, value = "accounts/{id}/{positionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> addPosition(@PathVariable String id, @PathVariable String positionId) throws NOT_FOUNDException, PERMISSION_FAILUREException, LOGIN_FAILEDException {
        if (positionRepo.exists(positionId) != true && !accountRepo.exists(id)) {
            throw new NOT_FOUNDException("Could not find account, " + id + ", and couldn't find position, " + positionId + " to be updated");
        } else if (positionRepo.exists(positionId) != true && accountRepo.exists(id) == true) {
            throw new NOT_FOUNDException("Could not find the position, " + positionId + ", to be added to the account " + id + ".");
        } else if (accountRepo.exists(id) != true && positionRepo.exists(id) == true) {
            throw new NOT_FOUNDException("Could not find account, " + id + " to add position, " + positionId + ".");
        } else {
            if (loggedIn == true) {
                if(loggedInUser.getAccessRights() == owner || loggedInUser.getId() == id) {
                    Account updateAccount = accountRepo.findOne(id);
                    Position addedPosition = positionRepo.findOne(positionId);
                    updateAccount.addPosition(addedPosition);
                    return new ResponseEntity<>(accountRepo.save(updateAccount), HttpStatus.OK);
                }else{
                    throw new PERMISSION_FAILUREException("User does not have the correct access rights to apply for positions with this account");
                }
            }else{
                throw new LOGIN_FAILEDException("User is not logged in, please log in before continuing");
            }
        }
    }

    @ExceptionHandler(NOT_FOUNDException.class)
    public ResponseEntity<ErrorResponse> NOT_FOUNDExceptionHandler(Exception ex){
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.NOT_FOUND.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @ExceptionHandler(BAD_REQUESTException.class)
    public ResponseEntity<ErrorResponse> BAD_REQUESTExceptionHandler(Exception ex){
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @ExceptionHandler(LOGIN_FAILEDException.class)
    public ResponseEntity<ErrorResponse> LOGIN_FAILEDExceptionHandler(Exception ex){
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @ExceptionHandler(PERMISSION_FAILUREException.class)
    public ResponseEntity<ErrorResponse> PERMISSION_FAILEDExceptionHandler(Exception ex){
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.NOT_ACCEPTABLE.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @ExceptionHandler(ACCOUNT_ALREADY_CREATEDException.class)
    public ResponseEntity<ErrorResponse> ACCOUNT_ALREADY_CREATEDExceptionHandler(Exception ex){
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.ALREADY_REPORTED.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

}
