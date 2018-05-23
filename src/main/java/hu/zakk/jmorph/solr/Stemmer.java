/*
 * Copyright 2018 Zsolt Czinkos <czinkos@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package hu.zakk.jmorph.solr;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import net.sf.jhunlang.jmorph.Dictionaries;
import net.sf.jhunlang.jmorph.Rules;
import net.sf.jhunlang.jmorph.analysis.AnalyserContext;
import net.sf.jhunlang.jmorph.analysis.AnalyserControl;
import net.sf.jhunlang.jmorph.analysis.AnalyserImpl;
import net.sf.jhunlang.jmorph.analysis.AnalysisEntry;
import net.sf.jhunlang.jmorph.factory.Definition;
import net.sf.jhunlang.jmorph.factory.JMorphFactory;
import net.sf.jhunlang.jmorph.parser.ParseException;

/**
 *
 * @author Zsolt Czinkos <czinkos@gmail.com>
 */
public class Stemmer {
    private final net.sf.jhunlang.jmorph.analysis.Analyser jmorph;
    private final AnalyserContext context;
    
    public Stemmer(int depth) throws IOException, ParseException {
        JMorphFactory jmf = new JMorphFactory();
        
        ClassLoader cl = java.lang.Thread.currentThread().getContextClassLoader();
        URL defURL = cl.getResource("hu.def");
        
        if(defURL == null) {
            throw new IOException("Not found: hu.def");
        }
        
        Definition adef[] = jmf.readDef(defURL);
        
        AnalyserControl control = new AnalyserControl(depth);
        this.context = new AnalyserContext(control);
        
        Rules rules = jmf.buildRules(adef[0]);
        Dictionaries dic = jmf.buildDictionaries(adef);
        this.jmorph = new AnalyserImpl(rules, dic);
    }
        
    public List<AnalysisEntry> analyse(String word) {  
        return this.jmorph.analyse(word, context);
    }
    
    public static void main(String[] args) throws IOException, ParseException {
        Stemmer analyser = new Stemmer(3);
        if (args.length > 0 && args[0].equals("-d")) {
            for (AnalysisEntry e : analyser.analyse(args[1])) {
                System.out.println("string: " + e.toString());
                System.out.println("long string: " + e.toLongString());
                System.out.println("content string: " + e.contentString());
                System.out.println("absolute root word: " + e.getAbsoluteRootWord());
                System.out.println("relative root word: " + e.getRelativeRootWord());
                System.out.println("dictionary root word: " + e.getDictionaryRootWord());
                System.out.println("POS: " + e.getPOS());
                System.out.println("morph string: " + e.morphString());
                System.out.println("case ending: " + e.getCaseEnding());
                System.out.println("inflexed word: " + e.getInflexedWord());
                System.out.println("---------------------   -------------------");
            }
            return;
        }
        
        Scanner scanner = new Scanner(System.in, "UTF-8");
        while (scanner.hasNext()) {
            String word = scanner.next();
            List <AnalysisEntry> l = analyser.analyse(word);
            System.out.print(word);
            if (l.isEmpty()) {
                System.out.println("\t" + word + "_UNKNOWN");
                continue;
            }
            
            for (AnalysisEntry e : l) {
                System.out.print("\t" + e.getAbsoluteRootWord() + "_" + e.getPOS().toUpperCase());
            }
            System.out.println();
        }
    }
}
