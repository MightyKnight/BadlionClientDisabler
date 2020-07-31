package me.alexanderritter02.blcdisabler;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Config {

    private Map<String, DisallowedMods> modsDisallowed =  new HashMap<String, DisallowedMods>();

    public Map<String, DisallowedMods> getModsDisallowed() {
        return this.modsDisallowed;
    }

    private class DisallowedMods {
        private boolean disabled;
        private JsonObject extra_data;
    }

}
