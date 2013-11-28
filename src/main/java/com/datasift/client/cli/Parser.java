package com.datasift.client.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class Parser {
    public static CliArguments parse(String[] args, List<CliSwitch> switches) {
        CliArguments res = new CliArguments(switches);
        String currentSwitch = null;
        List<String> acc = new ArrayList<>();
        for (String arg : args) {
            if (arg.indexOf('-') == 0) {
                //if current switch isn't null stop accumulating params and reset for the next switch
                if (currentSwitch != null) {
                    res.put(currentSwitch, acc);
                    acc = new ArrayList<>();
                }
                currentSwitch = arg;
                continue;
            }
            //accumulate values for the current instance of the current switch
            acc.add(arg);
        }
        return res;
    }

    public static class CliSwitch {
        protected String shortForm, longForm;

        public CliSwitch(String shortForm, String longForm) {
            this.shortForm = shortForm;
            this.longForm = longForm;
        }
    }

    public static class CliArguments {
        protected List<CliSwitch> switches;
        protected Map<String, List<String[]>> res = new HashMap<>();

        public CliArguments(List<CliSwitch> switches) {
            this.switches = switches;
        }

        public void put(String currentSwitch, List<String> value) {
            if (currentSwitch.indexOf("--") == 0) {
                currentSwitch = currentSwitch.substring(2);
            } else {
                currentSwitch = currentSwitch.substring(1);
            }
            List<String[]> val = res.get(currentSwitch);
            String shortForm = shortForm(currentSwitch);
            if (val == null && shortForm != null) {
                val = new ArrayList<>();
                res.put(shortForm, val);
            }
            if (val != null) {
                val.add(value.toArray(new String[value.size()]));
            }
        }

        /**
         * Get  a list of values passed in for the given param
         *
         * @param arg the param to get
         * @return a list of options or null if no params were passed for the arg
         */
        public List<String[]> get(String arg) {
            return res.get(shortForm(arg));
        }

        private String shortForm(String currentSwitch) {
            for (CliSwitch cliSwitch : switches) {
                if (cliSwitch.shortForm.equalsIgnoreCase(currentSwitch)
                        || cliSwitch.longForm.equalsIgnoreCase(currentSwitch)) {
                    return cliSwitch.shortForm;
                }
            }
            return null;
        }
    }
}
