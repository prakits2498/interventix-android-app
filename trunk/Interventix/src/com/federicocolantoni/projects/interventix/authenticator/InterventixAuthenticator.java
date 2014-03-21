package com.federicocolantoni.projects.interventix.authenticator;

import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;
import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;

public class InterventixAuthenticator extends AbstractAccountAuthenticator {

    Context context;

    public InterventixAuthenticator(Context context) {

	super(context);

	this.context = context;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {

	// final Intent intent = new Intent(context, MainActivity_.class);
	// intent.putExtra(Constants.ACCOUNT_TYPE_INTERVENTIX, accountType);
	// intent.putExtra(Constants.AUTHENTICATOR_TOKEN, authTokenType);
	// intent.putExtra(Constants.ADDING_NEW_ACCOUNT, true);
	// intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
	// response);
	//
	// final Bundle bundle = new Bundle();
	// bundle.putParcelable(AccountManager.KEY_INTENT, intent);
	return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse arg0, Account arg1, Bundle arg2) throws NetworkErrorException {

	return null;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {

	return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {

	// if (!authTokenType.equals(Constants.AUTHENTICATOR_TOKEN)) {
	// final Bundle result = new Bundle();
	// result.putString(AccountManager.KEY_ERROR_MESSAGE,
	// "invalid authTokenType");
	// return result;
	// }
	//
	// final AccountManager am = AccountManager.get(context);
	//
	// String authToken = am.peekAuthToken(account, authTokenType);
	//
	// if (!TextUtils.isEmpty(authToken)) {
	// final Bundle result = new Bundle();
	// result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
	// result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
	// result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
	// return result;
	// }
	//
	// final Intent intent = new Intent(context, MainActivity_.class);
	// intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
	// response);
	// intent.putExtra(Constants.ACCOUNT_TYPE_INTERVENTIX, account.type);
	// intent.putExtra(Constants.AUTHENTICATOR_TOKEN, authTokenType);
	// intent.putExtra(Constants.ACCOUNT_NAME, account.name);
	// final Bundle bundle = new Bundle();
	// bundle.putParcelable(AccountManager.KEY_INTENT, intent);
	return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {

	return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {

	final Bundle result = new Bundle();
	result.putBoolean(KEY_BOOLEAN_RESULT, false);
	return result;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {

	return null;
    }
}
