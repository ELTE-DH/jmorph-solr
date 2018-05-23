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
import java.util.List;
import net.sf.jhunlang.jmorph.analysis.AnalysisEntry;
import net.sf.jhunlang.jmorph.parser.ParseException;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Zsolt.Czinkos@gmail.com
 */
public class AnalyserTest {
    
    @org.junit.Test
    public void testAnalyse() throws IOException, ParseException {
        System.out.println("analyse");
        String word = "házban";
        Stemmer instance = new Stemmer(3);
        String expResult = "ház";
        List<AnalysisEntry> result = instance.analyse(word);
        assertEquals(expResult, result.get(0).getDictionaryRootWord());
    }
}
