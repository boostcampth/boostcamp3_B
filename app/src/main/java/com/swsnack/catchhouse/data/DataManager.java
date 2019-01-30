package com.swsnack.catchhouse.data;

import com.swsnack.catchhouse.data.userdata.APIManager;
import com.swsnack.catchhouse.data.userdata.UserDataManager;

public interface DataManager extends APIManager, UserDataManager {

    APIManager getAPIManager();

    UserDataManager getUserDataManager();
}
