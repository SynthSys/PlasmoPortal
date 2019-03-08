package uk.ac.ed.plasmo.service;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

//import org.apache.ws.security.WSPasswordCallback;

import uk.ac.ed.plasmo.assembler.SecurityAssembler;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.exception.InvalidUserException;

/**
 * The {@code PWCBHandler} is used as part of the WS-security layer for web services.
 * It checks the userName and password received from the SOAP message header against
 * the database to see if they are valid. If so, the SOAP request can proceed, otherwise
 * an error message is sent back to the requesting client 
 * @author ctindal
 *
 */
public class PWCBHandler implements CallbackHandler  {

	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
                    throw new UnsupportedOperationException("Commented off during library clearance");
                    /*WSPasswordCallback pwcb = (WSPasswordCallback)callbacks[i];
                    String userName = pwcb.getIdentifier();
                    String password = pwcb.getPassword();

                    SecurityAssembler assembler = new SecurityAssembler();

                    //check the userName and password against users in the database
                    User user = assembler.getUser(userName, password);
                    if(user == null) {
                        throw new InvalidUserException("Invalid user name or password.");
                    }
                    else {
                        return;
                    }
                    */
		}
		
	}

}
