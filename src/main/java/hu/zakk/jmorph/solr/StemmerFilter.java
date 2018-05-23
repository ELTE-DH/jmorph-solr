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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.sf.jhunlang.jmorph.analysis.AnalysisEntry;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

/**
 *
 * @author Zsolt Czinkos <czinkos@gmail.com>
 */
public class StemmerFilter extends TokenFilter {
    private final Set<String> terms;
    protected CharTermAttribute charTermAttribute =
        addAttribute(CharTermAttribute.class);
    protected PositionIncrementAttribute positionIncrementAttribute =
        addAttribute(PositionIncrementAttribute.class);
    
    private final Stemmer stemmer;
    
    public StemmerFilter(TokenStream ts, Stemmer stemmer) {
        super(ts);
        this.stemmer = stemmer;
        this.terms = new HashSet<>();
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (!terms.isEmpty()) {
          Iterator i = terms.iterator();
          char[] buffer = ((String) i.next()).toCharArray();
          i.remove();
          charTermAttribute.setEmpty();
          charTermAttribute.copyBuffer(buffer, 0, buffer.length);
          positionIncrementAttribute.setPositionIncrement(0);
          return true;
        }
        
        if (! input.incrementToken()) return false;
        
        String word = this.input.getAttribute(CharTermAttribute.class)
                          .toString().trim();
        
        for (AnalysisEntry entry : this.stemmer.analyse(word)) {
            String stem = entry.getDictionaryRootWord();
            if (!word.equals(stem)) terms.add(stem);
        }
        if (terms.size() > 0) terms.add(word); // boost original form
        
        positionIncrementAttribute.setPositionIncrement(1);
        return true;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
    }

    
    
}
