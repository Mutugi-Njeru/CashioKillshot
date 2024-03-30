package com.mycompany.cashiokillshot.ruleEngine.interfaces;

import org.json.JSONObject;

public interface IRule <I>{
    boolean matches(I input);
    JSONObject apply (I input);
}
