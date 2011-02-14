package org.talend.designer.dbmap.language.generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public class MapExpressionParser {

    private Perl5Matcher matcher = new Perl5Matcher();

    private Perl5Compiler compiler = new Perl5Compiler();

    private Pattern pattern;

    private String locationPattern;

    private PatternMatcherInput patternMatcherInput;

    public MapExpressionParser(String locationPattern) {
        this.locationPattern = locationPattern;
    }

    public void setLocationPattern(String locationPattern) {
        this.locationPattern = locationPattern;
    }

    public List<Map<String, String>> parseInTableEntryLocations(String expression) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        if (expression != null) {
            matcher.setMultiline(true);
            if (patternMatcherInput == null) {
                patternMatcherInput = new PatternMatcherInput(expression);
            } else {
                patternMatcherInput.setInput(expression);
            }

            recompilePatternIfNecessary(locationPattern);

            while (matcher.contains(patternMatcherInput, pattern)) {
                MatchResult matchResult = matcher.getMatch();
                Map<String, String> map = new HashMap<String, String>();

                map.put(matchResult.group(2), matchResult.group(1));
                result.add(map);
            }
        }
        return result;// .toArray(new TableEntryLocation[0]);
    }

    private void recompilePatternIfNecessary(String regexpPattern) {
        if (pattern == null || !regexpPattern.equals(pattern.getPattern())) {
            try {
                pattern = compiler.compile(regexpPattern);
            } catch (MalformedPatternException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
