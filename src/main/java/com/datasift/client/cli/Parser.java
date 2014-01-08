package com.datasift.client.cli;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class Parser {
    protected Parser() {
    }

    public static CliArguments parse(String[] args, List<CliSwitch> switches) {
        CliArguments res = new CliArguments(switches);
        String currentSwitch = null, name = null, value = null;

        if (args.length == 0) {
            System.out.println("There are no arguments passed.");
            return null;
        }

        for (String arg : args) {
            if (arg.indexOf('-') == 0) {
                //if current switch isn't null stop accumulating params and reset for the next switch
                if (currentSwitch != null) {
                    res.put(currentSwitch, name, value);
                    name = null;
                    value = null;
                }
                currentSwitch = arg;
                continue;
            }
            //the first param is treated as argument name and the last is taken as the value
            //this means we only support arguments of the form -x value OR -x name value
            //arguments of the form -x name val val ... (val)n have intermediate values ignored and only (val)n used
            if (name == null) {
                name = arg;
            } else {
                value = arg;
            }
        }
        //last param wouldn't have been added
        res.put(currentSwitch, name, value);
        for (CliSwitch s : switches) {
            if (s.isRequired()) {
                if (res.get(s.shortForm) == null) {
                    System.out.println(s.longForm +" - "+
                            (s.message == null || s.message.isEmpty() ? " is a required parameter" : s.message));
                    System.exit(0);
                }
            }

            if (s.hasDefault() && res.get(s.shortForm) == null) {
                res.put(s.shortForm, s.defaultName, s.defaultValue);
            }
        }
        return res;
    }

    public static class CliSwitch {
        private final String message;
        private final boolean required;
        protected String shortForm, longForm;
        private String defaultValue;
        private String defaultName;

        public CliSwitch(String shortForm, String longForm) {
            this(shortForm, longForm, false, "");
        }

        public CliSwitch(String shortForm, String longForm, boolean required, String message) {
            this.shortForm = shortForm;
            this.longForm = longForm;
            this.required = required;
            this.message = message;
        }

        public CliSwitch(String shortForm, String longForm, boolean required) {
            this(shortForm, longForm, required, "");
        }

        public boolean isRequired() {
            return required;
        }

        public void setDefault(String defaultName) {
            setDefault(defaultName, null);
        }

        public void setDefault(String defaultName, String defaultVal) {
            this.defaultName = defaultName;
            this.defaultValue = defaultVal;
        }

        public boolean hasDefault() {
            return this.defaultValue != null || this.defaultName != null;
        }
    }

    public static class CliArguments {
        protected List<CliSwitch> switches;
        protected Map<String, Object> res = new HashMap<>();

        public CliArguments(List<CliSwitch> switches) {
            this.switches = switches;
        }

        public void put(String currentSwitch, String name, String value) {
            if (currentSwitch.indexOf("--") == 0) {
                currentSwitch = currentSwitch.substring(2);
            }
            if (currentSwitch.indexOf("-") == 0) {
                currentSwitch = currentSwitch.substring(1);
            }
            Object val = res.get(currentSwitch);
            String shortForm = shortForm(currentSwitch);
            if (val == null && shortForm != null) {
                if (value != null) {
                    //if value is not null then current switch creates a map
                    val = new HashMap<>();
                }
                res.put(shortForm, val);
            }
            if (value == null) {
                //if value is null then current switch accepts a single string argument
                res.put(currentSwitch, name);
            } else {
                if (val != null) {
                    //otherwise put name value as kv pair under the current switch
                    ((HashMap<String, String>) val).put(name, value);
                }
            }
        }

        /**
         * Get  a set of key value pairs passed in for the given param
         * <p/>
         * e.g. if the command "parser -p a b -p c d -p e f" is given then the map returned will have
         * a,c and e as keys with b,d and f as respective values
         *
         * @param arg the param to get
         * @return a set of options or null if no params were passed for the arg
         * or null if the argument doesn't have a mapping
         */
        public HashMap<String, String> map(String arg) {
            try {
                return (HashMap<String, String>) res.get(shortForm(arg));
            } catch (ClassCastException cce) {
                return null;
            }
        }

        /**
         * Get the value of an argument
         *
         * @param arg the arg to get the value for
         * @return the value or null
         */
        public String get(String arg) {
            try {
                return (String) res.get(shortForm(arg));
            } catch (ClassCastException cce) {
                return null;
            }
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
